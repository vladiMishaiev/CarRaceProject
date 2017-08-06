package vladi_yaki_project.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import com.mysql.jdbc.CachedResultSetMetaData;

import vladi_yaki_project.domain.Bet;
import vladi_yaki_project.domain.CarData;
import vladi_yaki_project.domain.Gambler;
import vladi_yaki_project.domain.RaceData;
import vladi_yaki_project.domain.RaceState;

public class CarRaceJDBCImp implements CarRaceDao {
	private Connection connection;
    private PreparedStatement statement;
	private ResultSet resultSet;
	
	@Override
	public void createGambler(Gambler gambler) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = " insert into Gambler (ID, name, password, credit, balance)"
                    + " values (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1,gambler.getID());
            statement.setString(2, gambler.getName());
            statement.setString(3, gambler.getPassword());
            statement.setDouble(4, gambler.getCredit());
            statement.setDouble(5, gambler.getBalance());
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
	}
	@Override
	public void deleteGambler(Gambler gambler) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteGambler(String gamblerName) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = "DELETE FROM Gambler WHERE Name=?";
            statement = connection.prepareStatement(query);
            statement.setString(1,gamblerName);
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }	
		
	}
	@Override
	public Gambler retrieveGambler(String gamblerName) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = "SELECT * FROM Gambler WHERE name=?;";
            statement = connection.prepareStatement(query);
            statement.setString(1,gamblerName);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
            	Gambler gambler = new Gambler(resultSet.getInt(1),
            			resultSet.getString(2),resultSet.getString(3),
            			resultSet.getDouble(4));
            	gambler.setBalance(resultSet.getDouble(5));
            	return gambler;
            }
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(resultSet);
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		return null;
	}
	@Override
	public Gambler retrieveGambler(int gamblerID) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = "SELECT * FROM Gambler WHERE ID=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1,gamblerID);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
            	Gambler gambler = new Gambler(resultSet.getInt(1),
            			resultSet.getString(2),resultSet.getString(3),
            			resultSet.getDouble(4));
            	gambler.setBalance(resultSet.getDouble(5));
            	return gambler;
            }
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(resultSet);
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		return null;
	}
	@Override
	public void updateGambler(Gambler gambler) {
		//"UPDATE Race SET Date = ?, State = ?, BetsSum = ?, Profit = ?, NumOfBets = ?, Winner = ? WHERE ID = ?"
		try {
			connection = ConnectionFactory.getConnection();
            String query = " UPDATE Gambler SET name = ?, password = ?, credit = ?, balance = ? WHERE ID = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, gambler.getName());
            statement.setString(2, gambler.getPassword());
            statement.setDouble(3, gambler.getCredit());
            statement.setDouble(4, gambler.getBalance());
            statement.setInt(5,gambler.getID());
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		
	}

	//counters implementations  
	@Override
	public int getRaceCounter() {
		try {
			connection = ConnectionFactory.getConnection();
            String query = "SELECT * FROM Counters WHERE Property=?;";
            statement = connection.prepareStatement(query);
            statement.setString(1,"RaceCount");
            resultSet = statement.executeQuery();
            while (resultSet.next()){
            	return resultSet.getInt(2);
            }
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(resultSet);
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		return 0;
	}
	@Override
	public int getGamblersCounter() {
		try {
			connection = ConnectionFactory.getConnection();
            String query = "SELECT * FROM Counters WHERE Property=?;";
            statement = connection.prepareStatement(query);
            statement.setString(1,"GamblersCount");
            resultSet = statement.executeQuery();
            while (resultSet.next()){
            	return resultSet.getInt(2);
            }
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(resultSet);
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		return 0;
	}
	@Override
	public void saveRaceCounter(int count) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = " UPDATE Counters SET Value=? WHERE Property=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1,count);
            statement.setString(2,"RaceCount");
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }	
	}
	@Override
	public void saveGamblersCounter(int count) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = " UPDATE Counters SET Value=? WHERE Property=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1,count);
            statement.setString(2,"GamblersCount");
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
	}
	@Override
	public int getBetCounter() {
		try {
			connection = ConnectionFactory.getConnection();
            String query = "SELECT * FROM Counters WHERE Property=?;";
            statement = connection.prepareStatement(query);
            statement.setString(1,"BetCount");
            resultSet = statement.executeQuery();
            while (resultSet.next()){
            	return resultSet.getInt(2);
            }
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(resultSet);
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		return 0;
	}
	@Override
	public void saveBetsCounter(int count) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = " UPDATE Counters SET Value=? WHERE Property=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1,count);
            statement.setString(2,"BetCount");
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		
	}
	
	
	@Override
	public void saveRace(RaceData data) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = " insert into Race (ID, Date, State, BetsSum, Profit, NumOfBets, Winner)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1,data.getRaceID());
            if (data.getDate()==null)
                statement.setTimestamp(2,null);
            else
            	statement.setTimestamp(2,new java.sql.Timestamp(data.getDate().getTime()));
            statement.setString(3,data.getState().toString());
            statement.setDouble(4,data.getBetSum());
            statement.setDouble(5,data.getEarnings());
            statement.setInt(6,data.getNumOfBets());
            statement.setInt(7,data.getWinner());
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		
	}
	@Override
	public void updateRace(RaceData data) {
		//" UPDATE Counters SET Value=? WHERE Property=?"
		try {
			connection = ConnectionFactory.getConnection();
            String query = "UPDATE Race SET Date = ?, State = ?, BetsSum = ?, Profit = ?, NumOfBets = ?, Winner = ? WHERE ID = ?";
            statement = connection.prepareStatement(query);
            //statement.setInt(1,data.getRaceID());
            if (data.getDate()==null)
                statement.setTimestamp(1,null);
            else
            	statement.setTimestamp(1,new java.sql.Timestamp(data.getDate().getTime()));
            statement.setString(2,data.getState().toString());
            statement.setDouble(3,data.getBetSum());
            statement.setDouble(4,data.getEarnings());
            statement.setInt(5,data.getNumOfBets());
            statement.setInt(6,data.getWinner());
            statement.setInt(7,data.getRaceID());
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
	}
	@Override
	public void saveBet(Bet data) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = " insert into Bet (ID, GamblerID, RaceID, CarID, BetSum, WiningSum)"
                    + " values (?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1,data.getID());
            statement.setInt(2,data.getGamblerID());
            statement.setInt(3,data.getRaceID());
            statement.setInt(4,data.getCarID());
            statement.setDouble(5,data.getSum());
            statement.setDouble(6,data.getWiningSum());
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		
	}
	@Override
	public void updateBet(Bet data) {
		//"UPDATE Race SET Date = ?, State = ?, BetsSum = ?, Profit = ?, NumOfBets = ?, Winner = ? WHERE ID = ?"
		try {
			connection = ConnectionFactory.getConnection();
            String query = " UPDATE Bet SET GamblerID = ?, RaceID = ?, CarID = ?, BetSum = ?, WiningSum = ? WHERE ID = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1,data.getGamblerID());
            statement.setInt(2,data.getRaceID());
            statement.setInt(3,data.getCarID());
            statement.setDouble(4,data.getSum());
            statement.setDouble(5,data.getWiningSum());
            statement.setInt(6,data.getID());
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		
	}
	/*@Override
	public RaceData getRace(int raceID) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = "SELECT * FROM Race WHERE ID=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1,raceID);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
            	//Date date = 
            	RaceData race = new RaceData(resultSet.getInt(1),resultSet.getTimestamp(2),RaceState.valueOf(resultSet.getString(3)));
            	return race;
            }
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(resultSet);
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		return null;
	}*/
	@Override
	public RaceData getRace(int raceID) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void saveWiningTable(int[] raceResults, int raceID) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = " insert into RaceResults (RaceID, CarID, Place)"
                    + " values (?, ?, ?)";
            statement = connection.prepareStatement(query);
            for (int i=0 ; i<raceResults.length;i++){
            	statement.setInt(1,raceID);
            	statement.setInt(2,i);
            	statement.setInt(3,raceResults[i]);
            	statement.executeUpdate();
            }
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		
	}
	@Override
	public Bet getBet(int ID) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = "SELECT * FROM Bet WHERE ID=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1,ID);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
            	Bet bet = new Bet(resultSet.getInt(1),
            			resultSet.getInt(4),resultSet.getInt(2),
            			resultSet.getInt(3),resultSet.getDouble(5),resultSet.getDouble(6));
            	return bet;
            }
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(resultSet);
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		return null;
	}
	@Override
	public void saveCar(CarData data) {
		try {
			connection = ConnectionFactory.getConnection();
            String query = " insert into Car (ID, Name, Type, Size, Shape, Color, RaceID)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1,data.getId());
            statement.setString(2,data.getName());
            statement.setString(3,data.getType());
            statement.setString(4,data.getSize());
            statement.setString(5,data.getShape());
            statement.setString(6,data.getColor());
            statement.setInt(7,data.getRaceID());
            statement.executeUpdate();
        }catch (SQLException e) {
        	e.printStackTrace();
		}
		finally {
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
		
	}
	@Override
	public CarData getCar(int carID, int raceID) {
		// TODO Auto-generated method stub
		return null;
	}
	public static void main(String[] args) {
		CarRaceDao dao = new CarRaceJDBCImp();
		//dao.deleteGambler("avi");
		
		//Gambler gambler = dao.retrieveGambler("katya");
		//System.out.println(gambler);
		//int count = dao.getBetCounter();
		//dao.saveBetsCounter(100);
		//int count = dao.getGamblersCounter();
		//Date date = new Date();
		//java.sql.Timestamp t = new java.sql.Timestamp(date.getTime());
		//System.out.println(t);
		//System.out.println(sqlDate.toString());
		//System.out.println("Count is: "+count);
		//RaceData d = new RaceData(2);
		//dao.saveRace(d);
		//d.setEarnings(3000);
		//d.setDate(new Date());
		//d.setState(RaceState.RUNING);
		//dao.updateRace(d);
		//Bet b = new Bet(1, 1, 2, 2, 700);
		//dao.saveBet(b);
		//Bet b = dao.getBet(1);
		//b.setSum(500);
		//dao.updateBet(b);
		//CarData c = new CarData(1, "car1race1", "bmw", "big", "cool", "red", 2);
		//dao.saveCar(c);
		int[] arr = new int[5];
		arr[0]=1;
		arr[1]=2;
		arr[2]=3;
		arr[3]=4;
		arr[4]=5;
		dao.saveWiningTable(arr, 1);
	}
	
	
}
