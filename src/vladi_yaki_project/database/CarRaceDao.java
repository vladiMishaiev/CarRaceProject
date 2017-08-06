package vladi_yaki_project.database;

import vladi_yaki_project.domain.Bet;
import vladi_yaki_project.domain.CarData;
import vladi_yaki_project.domain.Gambler;
import vladi_yaki_project.domain.RaceData;
/**
 * @author Team
 * @version 1.0
 * @since 2017-04-02
 */

public interface CarRaceDao {
	//Counters Dao
	/**
	 * Get race counter from DB
	 * @return Nothing
	 */
	public int getRaceCounter();
	/**
	 * Get gamblers counter from DB
	 * @return int This returns updated number of races
	 */
	public int getGamblersCounter();
	/**
	 * Get bets counter from DB
	 * @return int This returns updated counter of bets
	 */
	public int getBetCounter();
	/**
	 * Save race counter in DB
	 * @param int count current value of race counter to save in DB
	 * @return Nothing
	 */
	public void saveRaceCounter(int count);
	/**
	 * Save gamblers counter in DB
	 * @param int count current value of gamblers counter to save in DB
	 * @return Nothing
	 */
	public void saveGamblersCounter(int count);
	/**
	 * Save bets counter in DB
	 * @param int count current value of bets counter to save in DB
	 * @return Nothing
	 */
	public void saveBetsCounter(int count);
	
	//Gambler Dao
	/**
	 * Create new gambler in DB
	 * @param Gambler receive a gambler object to save
	 * @return Nothing
	 */
	public void createGambler(Gambler gambler);
	/**
	 * Delete gambler from DB
	 * @param Gambler receive a gambler object to delete
	 * @return Nothing
	 */
	public void deleteGambler(Gambler gambler);
	/**
	 * Delete gambler from DB
	 * @param gamblerName receive a gambler name to delete
	 * @return Nothing
	 */
	public void deleteGambler(String gamblerName);
	/**
	 * Retrieve gambler from DB
	 * @param gamblerName receive a gambler name to retrieve from DB
	 * @return Nothing
	 */
	public Gambler retrieveGambler(String gamblerName);
	/**
	 * Retrieve gambler from DB
	 * @param gamblerID receive a gambler ID to retrieve record from DB
	 * @return Nothing
	 */
	public Gambler retrieveGambler(int gamblerID);
	/**
	 * Update gambler record in DB
	 * @param gambler receive a gambler object to update a record in DB
	 * @return Nothing
	 */
	public void updateGambler(Gambler gambler);
	
	//Race Dao
	/**
	 * Create new race record in DB
	 * @param raceData receive a race data object to save in DB
	 * @return Nothing
	 */
	public void saveRace (RaceData data);
	/**
	 * Update race record in DB
	 * @param raceData receive a race data object to update in DB
	 * @return Nothing
	 */
	public void updateRace (RaceData data);
	/**
	 * Retrieve a race record from DB
	 * @param raceID retrieve a race record from DB
	 * @return RaceData return the requested object
	 */
	public RaceData getRace (int raceID);
	/**
	 * Create race results data in DB
	 * @param int[] race results records to save in DB
	 * @param int Race ID 
	 * @return Nothing
	 */
	public void saveWiningTable (int[] raceResults , int raceID);
	
	//Bets Dao
	/**
	 * Create a bet record in DB
	 * @param Bet get a bet object to save in DB
	 * @return Nothing
	 */
	public void saveBet (Bet data);
	/**
	 * Update a bet record in DB
	 * @param Bet get a bet object to update in DB
	 * @return Nothing
	 */
	public void updateBet (Bet data);
	/**
	 * Retrieve a bet record from DB
	 * @param int ID get a bet object to save in DB
	 * @return Nothing
	 */
	public Bet getBet(int ID);
	
	//Car Dao
	/**
	 * Save car in DB
	 * @param CarData recieve object to save in DB
	 * @return Nothing
	 */
	public void saveCar (CarData car);
	/**
	 * Retrieve car from DB
	 * @param CarID recieve carID of record in DB
	 * @param CarRaceID recieve RaceID of record in DB
	 * @return Nothing
	 */
	public CarData getCar (int carID, int raceID);

}
