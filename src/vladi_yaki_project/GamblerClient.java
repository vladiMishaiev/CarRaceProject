package vladi_yaki_project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jdk.internal.org.objectweb.asm.util.Textifiable;
import vladi_yaki_project.com.BetMsg;
import vladi_yaki_project.com.GamblerCom;
import vladi_yaki_project.com.GamblerMsg;
import vladi_yaki_project.com.GamblerMsgType;
import vladi_yaki_project.com.RaceMsg;
import vladi_yaki_project.domain.Bet;
import vladi_yaki_project.domain.Gambler;
import vladi_yaki_project.domain.RaceData;

public class GamblerClient extends Application {
	private String host = "localhost";
	private static TextArea ta;
	private Socket socketA = null;
	private Socket socketB = null;
	private Gambler myProfile;
	private GamblerCom myCom;
	private ArrayList<RaceData> raceList = new ArrayList<>();

	@Override
	public void start(Stage primaryStage) throws Exception {
		ta = new TextArea();
		BorderPane pane = new BorderPane();
		pane.setCenter(ta);
		Scene scene = new Scene(pane, 450, 200);
		primaryStage.setTitle("Gambler window");
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

		// set up communication with server
		socketA = new Socket(host, 8080);
		socketB = new Socket(host, 8081);
		ObjectOutputStream toServerB = new ObjectOutputStream(socketB.getOutputStream());
		ObjectOutputStream toServerA = new ObjectOutputStream(socketA.getOutputStream());
		ObjectInputStream fromServerB = new ObjectInputStream(socketB.getInputStream());
		ObjectInputStream fromServerA = new ObjectInputStream(socketA.getInputStream());
		myCom = new GamblerCom(toServerA, fromServerA, toServerB, fromServerB);

		Platform.runLater(() -> ta.appendText("Connected to server"));

		// login/register window
		Stage loginStage = new Stage();
		FlowPane loginPane = new FlowPane();
		TextField name = new TextField();
		TextField pass = new TextField();
		TextField credit = new TextField();
		Label ansField = new Label("Please enter user and pass to connect\n" + "or all data to register");
		ansField.setPrefWidth(400);
		Button login = new Button("Login");
		Button register = new Button("Register");
		loginPane.getChildren().addAll(new Label("name :"), name, new Label("pass :"), pass, new Label("credit :"),
				credit, login, register, ansField);

		Scene loginScene = new Scene(loginPane, 490, 120);
		loginStage.setTitle("Gambler Login");
		loginStage.setScene(loginScene);
		loginStage.show();
		loginStage.setAlwaysOnTop(true);
		loginStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				Platform.exit();
				// System.exit(0);
			}
		});

		login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Gambler g = new Gambler(0, name.getText(), pass.getText(), 0);
					GamblerMsg loginMsg = new GamblerMsg(GamblerMsgType.LOGIN, g, "");
					myCom.getOutPortA().writeObject(loginMsg);
					loginMsg = (GamblerMsg) myCom.getInPortA().readObject();
					ansField.setText(loginMsg.getMsg());

					// login success
					if (loginMsg.getMsgType() == GamblerMsgType.APPROVE) {
						myProfile = loginMsg.getData();
						loginStage.close();
						initBetsStage();
						// listen for races data thread
						myCom.setID(myProfile.getID());
						new RaceListnerThred(myCom, raceList).start();
					}
				} catch (NumberFormatException ex) {
					ansField.setText("Input format error");
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		register.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Gambler g = new Gambler(0, name.getText(), pass.getText(), Double.parseDouble(credit.getText()));
					GamblerMsg registerMsg = new GamblerMsg(GamblerMsgType.REGISTER, g, "");
					myCom.getOutPortA().writeObject(registerMsg);
					registerMsg = (GamblerMsg) myCom.getInPortA().readObject();
					ansField.setText(registerMsg.getMsg());

					// register success
					if (registerMsg.getMsgType() == GamblerMsgType.APPROVE) {
						myProfile = registerMsg.getData();
						loginStage.close();
						initBetsStage();
						// listen for races data thread
						myCom.setID(myProfile.getID());
						new RaceListnerThred(myCom, raceList).start();
					}
				} catch (NumberFormatException | IOException | ClassNotFoundException ex) {
					ansField.setText("Input format error");
				}
			}
		});

		// get all races data

		// place bets
		// listen for races

	}

	public void initBetsStage() {
		// login/register window
		Stage betsStage = new Stage();
		FlowPane betsPane = new FlowPane();

		Scene betsScene = new Scene(betsPane, 490, 120);
		betsStage.setTitle("Bets window");
		betsStage.setScene(betsScene);
		betsStage.show();
		betsStage.setAlwaysOnTop(true);
		betsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				Platform.exit();
				// System.exit(0);
			}
		});

		// btn that send bets on action
		TextField raceID = new TextField();
		TextField carID = new TextField();
		TextField betSum =new TextField();
		Button submitBet = new Button("Submit");
		Label msgFiled = new Label();
		betsPane.getChildren().addAll(new Label("Race Id: "),raceID
				,new Label("Car Id: "),carID
				,new Label("Bet sum: "),betSum
				,submitBet,msgFiled);
		
		submitBet.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//Platform.runLater(() -> ta.appendText());
				int race, car;
				double sum;
				try {
					race = Integer.parseInt(raceID.getText());
					car = Integer.parseInt(carID.getText());
					sum = Double.parseDouble(betSum.getText());
					if (!isAvailbleRace(race)) {
						msgFiled.setText("Error enter valid active race num");
						return;
					}
					if (car < 0 || car > 4) {
						msgFiled.setText("Error enter valid car num");
						return;
					}
					Bet bet = new Bet(0,car,myProfile.getID(), race,sum);
					//changed from A to B
					myCom.getOutPortB().writeObject(bet);
					msgFiled.setText("Bet successfuly sent");
					BetMsg response = (BetMsg) myCom.getInPortB().readObject();
					switch(response.getMsgType()){
					case ACCEPT:{
						//sub from balance
						msgFiled.setText(response.getMsg());
					}break;
					case DENY:{
						msgFiled.setText(response.getMsg());
					}break;
					default:break;
					}
				} catch (NumberFormatException e) {
					msgFiled.setText("Please enter numbers");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		});
		
	}

	public boolean isAvailbleRace(int raceID) {
		Iterator<RaceData> itr = raceList.iterator();
		while (itr.hasNext()) {
			if (itr.next().getRaceID() == raceID)
				return true;
		}
		return false;
	}

	// listen for races (create,end) - get table view
	public static class RaceListnerThred extends Thread {
		private GamblerCom myCom;
		private ArrayList<RaceData> raceList;

		public RaceListnerThred(GamblerCom myCom, ArrayList<RaceData> raceList) {
			this.myCom = myCom;
			this.raceList = raceList;
		}

		public void run() {
			while (true) {
				try {
					Platform.runLater(() -> ta.appendText("\nWaiting for race data"));
					RaceMsg raceMsg = (RaceMsg) myCom.getInPortA().readObject();
					switch (raceMsg.getMsgType()) {
					case NEW: {
						raceList.add(raceMsg.getData());
					}
						break;
					default:
						break;
					}
					Platform.runLater(() -> ta.appendText("\nGot data about race: " + raceMsg.getData().getRaceID()));

				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
