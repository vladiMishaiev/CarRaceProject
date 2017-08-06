package vladi_yaki_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import vladi_yaki_project.com.RaceCom;
import vladi_yaki_project.domain.RaceData;
import vladi_yaki_project.raceMVC.CarRace;
import vladi_yaki_project.raceMVC.RaceController;
import vladi_yaki_project.raceMVC.RaceView;
import vladi_yaki_project.raceMVC.Song;

public class CarRaceClient extends Application {
	private String host = "localhost";
	private TextArea ta;
	private Socket socketA = null;
	private Socket socketB = null;
	private RaceCom myCom;
	private int raceID;
	private CarRace model;
	private RaceView view;
	private RaceController controller;
	private ArrayList<Song> songsList = new ArrayList<>();
	private Song mySong=null;
	private int interval = 30;
	String str = "";

	@Override
	public void start(Stage primaryStage) throws Exception {
		String fileName = "songs.txt";
		loadSongFromFiles(fileName);
		mySong = getRandomSong();
		
		//3 * 60 + 50
		//mySong = new Song("shape of you", (40), "ShapeOfYou.mp3");
		//songsList.add(mySong);
		Media sound = new Media(new File("Horn.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		Media song = new Media(new File(mySong.getUrl()).toURI().toString());
		MediaPlayer songMediaPlayer = new MediaPlayer(song);
		//mySong.setLenth(10);

		
		
		BorderPane pane = new BorderPane();
		ta = new TextArea();
		pane.setCenter(ta);
		Scene scene = new Scene(pane, 450, 200);
		primaryStage.setTitle("Race log window");
		primaryStage.setScene(scene);
		//primaryStage.show();
		primaryStage.hide();
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});

		socketA = new Socket(host, 8000);
		socketB = new Socket(host, 8001);

		ObjectOutputStream toServerB = new ObjectOutputStream(socketB.getOutputStream());
		ObjectOutputStream toServerA = new ObjectOutputStream(socketA.getOutputStream());
		ObjectInputStream fromServerB = new ObjectInputStream(socketB.getInputStream());
		ObjectInputStream fromServerA = new ObjectInputStream(socketA.getInputStream());
		myCom = new RaceCom(toServerA, fromServerA, toServerB, fromServerB);

		Platform.runLater(() -> ta.appendText("Connected to server"));

		raceID = (int) myCom.getInPortA().readObject();
		primaryStage.setTitle("Race "+raceID+" log window");
		model = new CarRace(raceID);
		view = new RaceView();
		controller = new RaceController(model, view);
		view.setModel(model);
		Stage raceStg = new Stage();
		Scene raceScene = new Scene(view.getBorderPane(), 750, 500);
		controller.setOwnerStage(raceStg);
		view.createAllTimelines();
		raceStg.setScene(raceScene);
		raceStg.setTitle("CarRaceView" + raceID);
		raceStg.setAlwaysOnTop(true);
		raceStg.show();
		raceScene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { // TODO
																													// //
																													// stub
				view.setCarPanesMaxWidth(newValue.doubleValue());
			}
		});
		raceStg.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});

		// generate song data

		new Thread(() -> {
			try {
				
				//send generated race data to server
				try {
					myCom.getOutPortA().writeObject(generateRaceData(model));
				} catch (IOException e) {
					e.printStackTrace();
				}

				// wait for start flag
				myCom.getInPortA().readObject();
				
				// start the race and song
				Platform.runLater(() -> {
					mediaPlayer.play();
					try {
						Thread.sleep(2*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mediaPlayer.stop();
					songMediaPlayer.play();
					controller.generateCarsSpeed();
				});

				// set timer to song length
				boolean stop = false;
				long counter = mySong.getLenth();
				do {
					if (counter > interval) {
						Thread.sleep(interval*1000);
						model.updateResults(interval);
						counter-=interval;
						Platform.runLater(() -> {
							controller.generateCarsSpeed();
						});
					}
					else{
						Thread.sleep(counter*1000);
						model.updateResults(counter);
						stop=true;
					}
				}while (!stop);
				
				Platform.runLater(() -> {
					songMediaPlayer.stop();
					controller.stopRace();
				});
				
				//print res array for debug
				double[] arr = model.getRaceResults();
				
				for (int i=0;i<arr.length;i++){
					str+="Car num "+i+" ..... "+arr[i]+"\n";	
					System.out.println("Car num "+i+" ..... "+arr[i]);
				}
				//send updated race data record to server 
				myCom.getOutPortA().writeObject(arr);
				
				
				
				//show results for 60 sec
				
				Platform.runLater(() -> {primaryStage.show();});
				Platform.runLater(() -> ta.setText("*** End of Race ***"+"\n"+str));
				Thread.sleep(60*1000);
				//Platform.runLater(() -> ta.setText("*** End of Race ***"+"\n"+str));
				
				//close win
				Platform.exit();
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

	}

	private Song getRandomSong() {
		int randomNum = ThreadLocalRandom.current().nextInt(0, songsList.size());
		return songsList.get(randomNum);
	}

	private void loadSongFromFiles(String fileName) {
		try {
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			//StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] splited = line.split(" ");
				//stringBuffer.append(line);
				//stringBuffer.append("\n");
				//System.out.println("** "+splited[0]+" "+splited[1]+" "+splited[2]);
				songsList.add(new Song(splited[0],Long.valueOf(splited[1]), splited[2]));
				//Song s = new Song(splited[0],Long.valueOf(splited[1]), splited[2]);
				//System.out.println(s);
			}
			fileReader.close();
			//System.out.println("Contents of file:");
			//System.out.println(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public RaceData generateRaceData(CarRace race) {
		RaceData data = new RaceData(race.getRaceCounter());
		data.addCar(race.getCarById(0));
		data.addCar(race.getCarById(1));
		data.addCar(race.getCarById(2));
		data.addCar(race.getCarById(3));
		data.addCar(race.getCarById(4));
		return data;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
