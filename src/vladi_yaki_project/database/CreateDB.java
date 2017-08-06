package vladi_yaki_project.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDB {
	private Connection connection;
    private Statement statement;
	private ResultSet rset;
	public void createDB(){
		try {
            connection = ConnectionFactory.getConnection();
            statement = connection.createStatement();
            
            System.out.println("Got here");
            String queryString = 
    				"SELECT * " + 
    				"FROM  gambler"
    				;
            String createRaceTable ="create table Department (deptId char(4) not null, name varchar(25) unique, chairId varchar(9), collegeId varchar(4));";
            
            rset = statement.executeQuery(createRaceTable);
            /*while (rset.next()){
            	System.out.println("ID: "+rset.getString(1) +
            			" name: "+ rset.getString(2)+" pass: "+ rset.getString(3));
            }*/
            
            
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(rset);
			DbUtil.close(statement);
            DbUtil.close(connection);
        }
	}
	public void insertData(){
		try {
            connection = ConnectionFactory.getConnection();
            statement = connection.createStatement();
            
            System.out.println("Got here");
            String queryString = 
    				"SELECT * " + 
    				"FROM  gambler"
    				;
            rset = statement.executeQuery(queryString);
            while (rset.next()){
            	System.out.println("ID: "+rset.getString(1) +
            			" name: "+ rset.getString(2)+" pass: "+ rset.getString(3));
            }
            
            
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(rset);
            DbUtil.close(statement);
            DbUtil.close(connection);
        }
	}
}
