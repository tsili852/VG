package utilities;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlConnector implements AutoCloseable {
	private Connection conn;
	private String databaseName;
	
	public SqlConnector(){
		try{
			Class.forName("org.sqlite.JDBC").newInstance();
		}
		catch (Exception e) {
			System.out.println(e.toString() + " " + e.getMessage());
		}
	}

	public SqlConnector(String dbName){
		databaseName = dbName;
		try{
			Class.forName("org.sqlite.JDBC").newInstance();
		}
		catch (Exception e) {
			System.out.println(e.toString() + " " + e.getMessage());
		}
	}
	
	public void setDbName(String dbName){
		databaseName = dbName;
	}
	
	public String getDbName(){
		return databaseName;
	}
	
	public Connection getConnection(){
		return conn;
	}
	
	public void connectToDatabase() throws SQLException{
		try{
			String userHome = System.getProperty("user.home");
			String outputFolder = userHome + File.separator + "VG Statistics";
			conn = DriverManager.getConnection("jdbc:sqlite:" + outputFolder + File.separator + databaseName,"","");
//			conn = DriverManager.getConnection("jdbc:sqlite:lib/" + databaseName,"","");
		} catch(SQLException e){
			throw e;
		}
	}
	
	public ResultSet executeResultSetQuery(String query) throws SQLException{
		try {
			Statement statement = conn.createStatement();
			
			ResultSet rs = statement.executeQuery(query);
			return rs;
			
		} catch(SQLException e){
			throw e;
		}
	}
	
	public int executeUpdateQuery(String query) throws SQLException{
		try {
			Statement statement = conn.createStatement();
			return statement.executeUpdate(query);		
		} catch(SQLException e){
			throw e;
		}
	}
	
	public boolean isConnectionOpen() throws SQLException{
		try {
			if(conn.isClosed()){
				return false;
			}else{
				return true;
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public void closeConnection() throws SQLException{
		try {
			conn.close();
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public void close() throws SQLException {
		try {
			closeConnection();
		} catch (SQLException e) {
			throw e;
		}
		
	}
}
