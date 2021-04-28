package com.flipLearn.BL.utilities;
import java.io.IOException;
import java.sql.*;

import com.jcraft.jsch.Session;
public class DBConnection{
	static Statement stmt;
	static Connection con = null;
	static Session session = null;
	static int id=0,irs=0;

	public static void CreateDBConnection()throws ClassNotFoundException, SQLException, IOException{
		//CreateSSHTunnel();
		String dbUrl = "jdbc:mysql://umsdbm.fliplearn.com:3306/";
		String username = "ums_read";String password = "ums_read$$514";String DbName = "ums_api";
		Class.forName("com.mysql.jdbc.Driver"); 
		try {
			con=DriverManager.getConnection(dbUrl+DbName,username,password);
			stmt = con.createStatement();
		} catch (Exception e) {}
	}    

	public static void disconnectDBConnection() throws ClassNotFoundException, SQLException
	{
		//session.disconnect();
		try {
			con.close();
		} catch (Exception e) {}

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