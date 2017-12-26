package util;

import java.io.IOException;
import java.sql.*;

import com.jcraft.jsch.Session;
public class DBConnection{
	static Statement stmt;
	static Connection umsConnection = null;
	static Connection groupConnection = null;
	static Connection blConnection = null;
	static Connection schoolConnection = null;
	static Session session = null;
	static int id=0,irs=0;
	static LoadProperty load;

	public static void connectUmsDB(){
		load = new LoadProperty();
		String dbhost=(String) load.getProperty("UMS_DB_HOST");
		String DbName = "ums_api";
		String username=(String) load.getProperty("UMS_DB_USERNAME");
		String password=(String) load.getProperty("UMS_DB_PASSWORD");
		String dbUrl = "jdbc:mysql://"+dbhost+":3306/";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			umsConnection=DriverManager.getConnection(dbUrl+DbName,username,password);
			stmt = umsConnection.createStatement();
		} catch (Exception e) {}
	}    

	public static void connectGroupDB(){
		String dbhost=(String) load.getProperty("GROUP_DB_HOST");
		String DbName = "group_api";
		String username=(String) load.getProperty("GROUP_DB_USERNAME");
		String password=(String) load.getProperty("GROUP_DB_PASSWORD");
		String dbUrl = "jdbc:mysql://"+dbhost+":3306/";
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			groupConnection=DriverManager.getConnection(dbUrl+DbName,username,password);
			stmt = groupConnection.createStatement();
		} catch (Exception e) {}
	}  

	public static void connectbusinessLayerDB(){
		String dbhost=(String) load.getProperty("BL_DB_HOST");
		String DbName = "flip_bl";
		String username=(String) load.getProperty("BL_DB_USERNAME");
		String password=(String) load.getProperty("BL_DB_PASSWORD");
		String dbUrl = "jdbc:mysql://"+dbhost+":3306/";
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			blConnection=DriverManager.getConnection(dbUrl+DbName,username,password);
			stmt = blConnection.createStatement();
		} catch (Exception e) {}
	}  

	public static void connectSchoolDB(){
		String dbhost=(String) load.getProperty("SCHOOL_DB_HOST");
		String DbName = "school_api";
		String username=(String) load.getProperty("SCHOOL_DB_USERNAME");
		String password=(String) load.getProperty("SCHOOL_DB_PASSWORD");
		String dbUrl = "jdbc:mysql://"+dbhost+":3306/";
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			schoolConnection=DriverManager.getConnection(dbUrl+DbName,username,password);
			stmt = schoolConnection.createStatement();
		} catch (Exception e) {}
	}  

	public static void disconnectDBConnection()	{
		//session.disconnect();
		try {
			//con.close();
		} catch (Exception e) {}

	}

	public static void disconnectDB(String connection){
		switch (connection.toLowerCase()) {
		case "ums":
			try {
				umsConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "group":
			try {
				groupConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "school":
			try {
				schoolConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "bl":
			try {
				blConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		default:
			System.out.println("Incorrect DB name for stopping");
			break;
		}
	}


	public static ResultSet executeQuery(String query) throws SQLException
	{  
		ResultSet rs= stmt.executeQuery(query); 
		return rs;
	}

	public static int executeUpdate(String query) throws SQLException
	{   
		int rs= stmt.executeUpdate(query);
		return rs;	
	}
}

