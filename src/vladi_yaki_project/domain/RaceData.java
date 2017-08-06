package vladi_yaki_project.domain;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import vladi_yaki_project.raceMVC.Car;

public class RaceData implements Serializable{
	private int raceID;
	private Date date;
	private ArrayList<Bet> betsList;
	private ArrayList<CarData> carsList;
	private double betsSum;
	private double profit;
	private int numOfBets;
	private int[] resultsArray = new int[5];
	private int[] betsTracker = new int[5];
	private RaceState state;
	private int winner;
	
	public RaceData(int raceID) {
		this.raceID = raceID;
		this.betsList = new ArrayList<>();
		this.carsList = new ArrayList<>();
		this.betsSum = 0;
		this.profit = 0;
		this.state = RaceState.WAITING;
		this.winner=0;
	}
	
	public RaceData(int raceID, Date date, RaceState state, double betsSum, double profit, int numOfBets
		 ) {
		this.betsList = new ArrayList<>();
		this.carsList = new ArrayList<>();
		this.raceID = raceID;
		this.date = date;
		this.betsSum = betsSum;
		this.profit = profit;
		this.numOfBets = numOfBets;
		this.state = state;
	}
	/**
	 * Proccess the race results and calc cars place
	 * @param double[] results - distances each car drove
	 * @return nothing method updates the right member
	 */
	public void proccessResults(double[] results) {
		double max = -1;
		int index = -1;
		for (int j=1; j <= results.length; j++) {
			for (int i = 0; i < results.length; i++) {
				if (results[i] > max) {
					max = results[i];
					index = i;
				}
			}
			this.resultsArray[index] = j;
			results[index]=-1;
			max=-1;
			if (j==1)
				this.winner=index;
		}
	}
	/**
	 * Retrive winner bets list
	 * @param Nothing
	 * @return arrayList<Bet> bets that won a race
	 */
	public ArrayList<Bet> getWinnerBetsList(){
		ArrayList<Bet> winners = new ArrayList<>();
		Iterator<Bet> itr = betsList.iterator();
		while (itr.hasNext()){
			Bet bet = itr.next();
			if (bet.getCarID()==this.winner)
				winners.add(bet);
		}
		return winners;
	}
	public int getBetID(int carID,int gamblerID){
		Iterator<Bet> itr = betsList.iterator();
		while (itr.hasNext()){
			Bet bet = itr.next();
			if (bet.getCarID()==carID && bet.getGamblerID()==gamblerID)
				return bet.getID();
		}
		return -1;
	}
	/**
	 * place a bet - calc comition
	 * @param Bet - bet object to place
	 * @return nothing
	 */
	public void placeBet(Bet bet){
		betsList.add(bet);
		double sum = bet.getSum();
		bet.setSum(sum*0.95);
		betsSum+=sum*0.95;
		profit+=sum*0.05;
		if (betsTracker[bet.getCarID()]==0){
			betsTracker[bet.getCarID()]=1;
			numOfBets++;
			if (numOfBets>=3 && state==RaceState.WAITING)
				state=RaceState.READY;
		}
	}
	//public Bet getBetByID()
	public void addCar(CarData carData){
		carsList.add(carData);
	}
	public void addCar(Car car){
		carsList.add(new CarData(car));
	}

	public int getRaceID() {
		return raceID;
	}
	public void setRaceID(int raceID) {
		this.raceID = raceID;
	}
	public ArrayList<Bet> getBetsList() {
		return betsList;
	}
	public void setBetsList(ArrayList<Bet> betsList) {
		this.betsList = betsList;
	}
	public ArrayList<CarData> getCarsList() {
		return carsList;
	}
	public void setCarsNamesList(ArrayList<CarData> carsList) {
		this.carsList = carsList;
	}
	public double getBetSum() {
		return betsSum;
	}
	public void setBetSum(double betSum) {
		this.betsSum = betSum;
	}
	public double getEarnings() {
		return profit;
	}
	public void setEarnings(double earnings) {
		this.profit = earnings;
	}
	@Override
	public String toString() {
		return "RaceData [raceID=" + raceID + ", isReady=" + ", betsList=" + betsList + ", carsList="
				+ carsList + ", betSum=" + betsSum + ", earnings=" + profit + "]";
	}
	public Date getDate() {
		//LocalDate date;
		//java.sql.Date dat = java.sql.Date.valueOf(date);
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public int getNumOfBets() {
		return numOfBets;
	}
	public void setNumOfBets(int numOfBets) {
		this.numOfBets = numOfBets;
	}
	public int[] getResultsArray() {
		return resultsArray;
	}
	public void setResultsArray(int[] resultsArray) {
		this.resultsArray = resultsArray;
	}
	
	public RaceState getState() {
		return state;
	}

	public void setState(RaceState state) {
		this.state = state;
	}
	
	public int getWinner(){
		return this.winner;
	}
	public void setWinner(int winner){
		this.winner=winner;
	}
}
