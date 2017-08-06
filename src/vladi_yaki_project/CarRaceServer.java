package vladi_yaki_project;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import vladi_yaki_project.com.BetMsg;
import vladi_yaki_project.com.BetMsgType;
import vladi_yaki_project.com.GamblerCom;
import vladi_yaki_project.com.GamblerMsg;
import vladi_yaki_project.com.GamblerMsgType;
import vladi_yaki_project.com.RaceCom;
import vladi_yaki_project.com.RaceMsg;
import vladi_yaki_project.com.RaceMsgType;
import vladi_yaki_project.database.CarRaceDao;
import vladi_yaki_project.database.CarRaceJDBCImp;
import vladi_yaki_project.database.CreateDB;
import vladi_yaki_project.domain.Bet;
import vladi_yaki_project.domain.Gambler;
import vladi_yaki_project.domain.RaceData;
import vladi_yaki_project.domain.RaceState;

public class CarRaceServer extends Application {
	private ArrayList<RaceCom> raceComeList = new ArrayList<>();
	private ArrayList<GamblerCom> gamblerComeList = new ArrayList<>();
	private int raceCounter = 0; //uploaded from DB
	private int gamblerCounter = 0; //uploaded from DB
	private static int betCounter=0; //uploaded from DB
	private static TextArea ta;
	private ArrayList<RaceData> raceDataList = new ArrayList<>(); //active races list
	private boolean raceLock = false;
	private boolean numOfRacesConstrains = false;
	private int numOfRacesConstrainsLimit = 3;
	private CarRaceDao dao = new CarRaceJDBCImp();

	@Override
	public void start(Stage primaryStage) throws Exception {
		//initiate counters
		gamblerCounter = dao.getGamblersCounter();
		raceCounter = dao.getRaceCounter();
		betCounter = dao.getBetCounter();
		BorderPane pane = new BorderPane();
		ta = new TextArea();
		pane.setCenter(ta);
		Scene scene = new Scene(pane, 450, 200);
		primaryStage.setTitle("Server Window");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				//save counters to DB
				dao.saveGamblersCounter(gamblerCounter);
				dao.saveRaceCounter(raceCounter);
				dao.saveBetsCounter(betCounter);
				verifyActiveRaces();
				Platform.exit();
				System.exit(0);
			}

			private void verifyActiveRaces() {
				Iterator<RaceData> itr = raceDataList.iterator();
				while (itr.hasNext()){
					RaceData data = itr.next();
					if (data.getState()!=RaceState.FINISHED){
						data.setState(RaceState.CANCELED);
						dao.updateRace(data);
					}
				}
			}
		});
		
		//open new window for queries
		
		Platform.runLater(() -> ta.appendText("Server started at " + new Date() + '\n'));
		// handle race clients
		new Thread(() -> {
			try {
				@SuppressWarnings("resource")
				ServerSocket raceSocketA = new ServerSocket(8000);
				@SuppressWarnings("resource")
				ServerSocket raceSocketB = new ServerSocket(8001);
				while (true) {
					Socket socketA = raceSocketA.accept();
					Socket socketB = raceSocketB.accept();
					Platform.runLater(() -> ta.appendText("New race started " + "\n"));
					Platform.runLater(() -> ta.appendText("Client IP " + socketA.getInetAddress() + "\n"));
					// set communications data
					ObjectOutputStream outA = new ObjectOutputStream(socketA.getOutputStream());
					ObjectOutputStream outB = new ObjectOutputStream(socketB.getOutputStream());
					ObjectInputStream inA = new ObjectInputStream(socketA.getInputStream());
					ObjectInputStream inB = new ObjectInputStream(socketB.getInputStream());
					RaceCom newRaceCom = new RaceCom(outA, inA, outB, inB);
					newRaceCom.setID(++raceCounter);
					raceComeList.add(newRaceCom);
					// send race ID to client
					newRaceCom.getOutPortA().writeObject(raceCounter);
					// receive race data
					RaceData data = (RaceData) newRaceCom.getInPortA().readObject();
					//save race to DB
					dao.saveRace(data);
					dao.saveCar(data.getCarsList().get(0));
					dao.saveCar(data.getCarsList().get(1));
					dao.saveCar(data.getCarsList().get(2));
					dao.saveCar(data.getCarsList().get(3));
					dao.saveCar(data.getCarsList().get(4));
					raceDataList.add(data);
					distributeRaceData(data);
					raceScheduler();
				}
			} catch (Exception ex) {
				return;
			}
		}).start();

		// handle gambler clients
		new Thread(() -> {
			try {
				//CreateDB createDB = new CreateDB();
				//createDB.createDB();
				@SuppressWarnings("resource")
				ServerSocket gamblerSocketA = new ServerSocket(8080);
				@SuppressWarnings("resource")
				ServerSocket gamblerSocketB = new ServerSocket(8081);

				while (true) {
					Socket socketA = gamblerSocketA.accept();
					Socket socketB = gamblerSocketB.accept();
					Platform.runLater(() -> ta.appendText("New gambler connected " + "\n"));
					Platform.runLater(() -> ta.appendText("Client IP " + socketA.getInetAddress() + "\n"));
					ObjectOutputStream outA = new ObjectOutputStream(socketA.getOutputStream());
					ObjectOutputStream outB = new ObjectOutputStream(socketB.getOutputStream());
					ObjectInputStream inA = new ObjectInputStream(socketA.getInputStream());
					ObjectInputStream inB = new ObjectInputStream(socketB.getInputStream());
					GamblerCom newGamblerCom = new GamblerCom(outA, inA, outB, inB);
					// newGamblerCom.setID(gamblerCounter++);
					gamblerComeList.add(newGamblerCom);

					// wait for request and open new bets listener when
					// connected
					new Thread(() -> {
						GamblerMsg newRequest = null;
						do {
							try {
								newRequest = (GamblerMsg) newGamblerCom.getInPortA().readObject();
								switch (newRequest.getMsgType()) {
								case LOGIN: {
									String name = newRequest.getData().getName();
									String pass = newRequest.getData().getPassword();
									Gambler gambler = dao.retrieveGambler(name);
									if (gambler == null) {
										newRequest.setMsgType(GamblerMsgType.DENY);
										newRequest.setMsg("No such user in DB");
									}
									else if (gambler.getPassword().compareTo(pass) != 0) {
										newRequest.setMsgType(GamblerMsgType.DENY);
										newRequest.setMsg("Wrong Password");
									} else {
										newGamblerCom.setID(gambler.getID());
										newRequest.setData(gambler);
										
										newRequest.setMsgType(GamblerMsgType.APPROVE);
										newRequest.setMsg("Login approved");
										//new BetsListnerThread(newGamblerCom).start();
										new Thread(() -> {
											betsListner(newGamblerCom);
										}).start();
									}
									newGamblerCom.getOutPortA().writeObject(newRequest);
								}
									break;
								case REGISTER: {
									String name = newRequest.getData().getName();
									if (dao.retrieveGambler(name)!=null){
										newRequest.setMsgType(GamblerMsgType.DENY);
										newRequest.setMsg("User name taken");
									}
									else{
										//create new user
										Gambler newGambler = newRequest.getData();
										newGambler.setID(++gamblerCounter);
										newGamblerCom.setID(newGambler.getID());
										dao.createGambler(newGambler);
										//new BetsListnerThread(newGamblerCom).start();
										newRequest.setMsgType(GamblerMsgType.APPROVE);
										newRequest.setMsg("User created");
										new Thread(() -> {
											betsListner(newGamblerCom);
										}).start();
									}
									newGamblerCom.getOutPortA().writeObject(newRequest);
								}
									break;
								default:
									break;
								}
							} catch (ClassNotFoundException | IOException e) {
								e.printStackTrace();
							}
						} while (newRequest.getMsgType() != GamblerMsgType.APPROVE);
						distributeAllRaceData(newGamblerCom.getID());
					}).start();

				}
			} catch (Exception ex) {
				return;
			}
		}).start();
	}
	/**
	 * Critical method which responsible on scheduling all races
	 * Method is thread safe 
	 * @param Nothing
	 * @return Nothing
	 */
	public synchronized void raceScheduler() {
		if (raceLock)
			return;
		// check if first condition is met
		if (!numOfRacesConstrains) {
			if (raceDataList.size() >= numOfRacesConstrainsLimit) {
				numOfRacesConstrains=true;
			}else{
				return;
			}
		}
		
		//look up a ready race return -1 if none ready
		this.raceLock=true;
		int nextRace = 0;
		do {
			System.out.println("Got to schedular");
			nextRace = lookUpRace();
			int i = nextRace;
			//Platform.runLater(() -> ta.appendText("\nNext race = "+i));
			if (nextRace == -1){
				this.raceLock=false;
				return;
			}
			// start next race

			RaceData race = getRaceById(nextRace);
			race.setState(RaceState.RUNING);
			race.setDate(new Date());
			dao.updateRace(race);
			// send start flag
			RaceCom com = getRaceComByID(race.getRaceID());
			try {
				com.getOutPortA().writeObject(1);
				// wait for finish data (race winners)
				double[] arr = (double[]) com.getInPortA().readObject();
				// update data in raceData
				race.proccessResults(arr);
				System.out.println("Got race results");
				String str="";
				for (int j=0;j<race.getResultsArray().length;j++){
					str+="Car num "+i+" Finished "+race.getResultsArray()[i]+"\n";
				}
				String res = str;
				Platform.runLater(() -> ta.appendText("*** End Of Race "+race.getRaceID()+" *** \n"+res));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			
			// update winnings
			race.setState(RaceState.FINISHED);
			dao.updateRace(race);
			dao.saveWiningTable(race.getResultsArray(), race.getRaceID());
			updateWiningBets(race);
			// delete race from list
			// delete race com from list
		} while (nextRace != -1);
		raceLock = false;
	}
	/**
	 * Get a finished race and update all gamblers and bets records
	 * @param RaceData race - object of the finished race
	 * @return car ID
	 */
	public void updateWiningBets(RaceData race){
		double betsSum = race.getBetSum();
		double winingBetsSum=0;
		ArrayList<Bet> winingBets =race.getWinnerBetsList();
		Iterator<Bet> itr = winingBets.iterator();
		while (itr.hasNext()){
			winingBetsSum+=itr.next().getSum();
		}
		itr = winingBets.iterator();
		while (itr.hasNext()){
			Bet b = itr.next();
			double gamblerSum = b.getSum()/winingBetsSum*betsSum;
			b.setWiningSum(gamblerSum);
			dao.updateBet(b);
			Gambler g = dao.retrieveGambler(b.getGamblerID());
			g.setBalance(g.getBalance()+gamblerSum);
			dao.updateGambler(g);
		}
		
	}
	/**
	 * Lookup table decide what next race is going to run
	 * @param Nothing
	 * @return Nothing
	 */
	public int lookUpRace(){
		double maxSum = -1;
		int raceNum = -1;
		Iterator<RaceData> itr = raceDataList.iterator();
		while (itr.hasNext()){
			RaceData data = itr.next();
			if (data.getState()==RaceState.READY){
				if (data.getBetSum()>maxSum){
					maxSum=data.getBetSum();
					raceNum=data.getRaceID();
				}
			}
		}
		return raceNum;
	}
	/**
	 * Retrive a race from list by id
	 * @param int race Id
	 * @return Race Data
	 */
	public RaceData getRaceById(int raceID) {
		Iterator<RaceData> itr = raceDataList.iterator();
		RaceData data = null;
		while (itr.hasNext()){
			data = itr.next();
			if (data.getRaceID()==raceID)
				return data;
		}
		return data;
	}
	/**
	 * Get a race and distribute data to all active gamblers
	 * @param racedata race
	 * @return nothing
	 */
	public void distributeRaceData(RaceData data) {
		Iterator<GamblerCom> itr = gamblerComeList.iterator();
		while(itr.hasNext()){
			GamblerCom com = itr.next();
			try {
				//Platform.runLater(() -> ta.appendText("\nSending data "));
				RaceMsg raceMsg = new RaceMsg(RaceMsgType.NEW,data,"New race");
				com.getOutPortA().writeObject(raceMsg);
				//Platform.runLater(() -> ta.appendText("\nData sent "));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	//send data of all active races to gambler
	/**
	 * Recive a gambler and send him data about all the races
	 * @param int gambler id
	 * @return nothing
	 */
	public void distributeAllRaceData(int gamblerID){
		GamblerCom com = getGamblerComByID(gamblerID);
		Iterator<RaceData> itr = raceDataList.iterator();
		while (itr.hasNext()){
			try {
				RaceMsg raceMsg = new RaceMsg(RaceMsgType.NEW,itr.next(),"New race");
				com.getOutPortA().writeObject(raceMsg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//get gambler com by ID
	/**
	 * Retrive a gambler com by gambler id
	 * @param int gambler id
	 * @return gamblerCom
	 */
	public GamblerCom getGamblerComByID(int id){
		Iterator<GamblerCom> itr = gamblerComeList.iterator();
		while (itr.hasNext()){
			GamblerCom com = itr.next();
			if(com.getID()==id)
				return com;
		}
		return null;
	}
	/**
	 * Retrive a race com from list by id
	 * @param int race Id
	 * @return RaceCom com
	 */
	public RaceCom getRaceComByID(int id){
		Iterator<RaceCom> itr = raceComeList.iterator();
		while (itr.hasNext()){
			RaceCom com = itr.next();
			if(com.getID()==id)
				return com;
		}
		return null;
	}
	/**
	 * Inifinite loop that listen for bets and updated data
	 * @param GamblerCom - indicated to whom to listen
	 * @return nothing
	 */
	public void betsListner (GamblerCom myCom){
		while (true){
			try {
				BetMsg response = null;
				//changed from A to B
				Bet bet = (Bet) myCom.getInPortB().readObject();
				if (getRaceById(bet.getRaceID()).getState()==RaceState.RUNING || getRaceById(bet.getRaceID()).getState()==RaceState.FINISHED){
					response = new BetMsg(BetMsgType.DENY,null,"Too late, race in progress");
					myCom.getOutPortB().writeObject(response);
				}else{
					//new bet for this car from this gambler
					int betID = getRaceById(bet.getRaceID()).getBetID(bet.getCarID(), bet.getGamblerID());
					if (betID==-1){
						bet.setID(++betCounter);
						dao.saveBet(bet);
					}else{
						Bet b = dao.getBet(betID);
						b.setSum(b.getSum()+bet.getSum());
						dao.updateBet(b);
					}
					//update gamblers balance
					Gambler gambler = dao.retrieveGambler(bet.getGamblerID());
					gambler.setBalance(gambler.getBalance()-bet.getSum());
					dao.updateGambler(gambler);
					
					response = new BetMsg(BetMsgType.ACCEPT,null,"Your bet was accepted");
					myCom.getOutPortB().writeObject(response);
					
					getRaceById(bet.getRaceID()).placeBet(bet);
					dao.updateRace(getRaceById(bet.getRaceID()));
					if (getRaceById(bet.getRaceID()).getState()==RaceState.READY){
						System.out.println("Race ready starting scheduler");
						//raceScheduler();
						Platform.runLater(() -> {
							raceScheduler();
						});
					}
				}
				
				//Platform.runLater(() -> ta.appendText("\n"+bet));
			} catch (ClassNotFoundException | IOException e) {
				//e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
