package vladi_yaki_project.domain;

import java.io.Serializable;

public class Bet implements Serializable{
	private int ID;
	private int carID;
	private int gamblerID;
	private int raceID;
	private double sum;
	private double winingSum;
	/**
	 * Bet constructor
	 * @param int id - id of bet
	 * @param int carID - id of the car
	 * @param int gamblerID - id of gambler
	 * @param int raceId - id of race
	 * @param double sum - sum of bet
	 * @return Nothing
	 */
	public Bet(int id,int carID, int gamblerID, int raceID, double sum) {
		this.ID=id;
		this.carID = carID;
		this.gamblerID = gamblerID;
		this.raceID = raceID;
		this.sum = sum;
		this.winingSum=0;
	}
	/**
	 * Bet constructor
	 * @param int id - id of bet
	 * @param int carID - id of the car
	 * @param int gamblerID - id of gambler
	 * @param int raceId - id of race
	 * @param double sum - sum of bet
	 * @return Nothing
	 */
	public Bet(int id,int carID, int gamblerID, int raceID, double sum, double winingSum) {
		this.ID=id;
		this.carID = carID;
		this.gamblerID = gamblerID;
		this.raceID = raceID;
		this.sum = sum;
		this.winingSum=winingSum;
	}
	/**
	 * Bet get car - recieve car
	 * @param Nothing
	 * @return car ID
	 */
	public int getCarID() {
		return carID;
	}
	/**
	 * Bet set car - set car
	 * @param int id
	 * @return nothing
	 */
	public void setCarID(int carID) {
		this.carID = carID;
	}
	/**
	 * Bet get gamblerID - recieve gambler id
	 * @param Nothing
	 * @return int gambler ID
	 */
	public int getGamblerID() {
		return gamblerID;
	}
	/**
	 * Bet set gambler id
	 * @param int gambler id
	 * @return nothing
	 */
	public void setGamblerID(int gamblerID) {
		this.gamblerID = gamblerID;
	}
	/**
	 * Bet get race id - recieve race id
	 * @param Nothing
	 * @return race ID
	 */
	public int getRaceID() {
		return raceID;
	}
	/**
	 * Bet set race id - set id
	 * @param int raceID
	 * @return nothing
	 */
	public void setRaceID(int raceID) {
		this.raceID = raceID;
	}
	
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public double getWiningSum() {
		return winingSum;
	}
	public void setWiningSum(double winingSum) {
		this.winingSum = winingSum;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	@Override
	public String toString() {
		return "Bet [ID=" + ID + ", carID=" + carID + ", gamblerID=" + gamblerID + ", raceID=" + raceID + ", sum=" + sum
				+ ", winingSum=" + winingSum + "]";
	}
	
	
	
	
}
