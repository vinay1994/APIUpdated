package com.flipLearn.bl.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class DBFunctions {

	public String serverip;
	public String port;
	public String dbName;
	public String Username;
	public String Password;
	Connection connection1;
	Statement statement1;
	public  ResultSet rs;
	Session session;

	public void connectdatabase()
	{
		try {  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:23306/", "", "");
			statement1 = connection1.createStatement();
		} catch (Exception e) {  
			System.out.println("not connect");
			e.printStackTrace();  
		}  
	}	
	
	public void CreateSSHTunnel() throws JSchException
	{
		JSch jsch = new JSch();
		session = jsch.getSession("", "");
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword("");
		session.connect(10000);
		session.setPortForwardingL(23306, "127.0.0.1", 3306);
	}
	
	public void CloseSSHTunnel() throws JSchException
	{
		session.disconnect();
	}

	public ResultSet Selectstatement(String query)
	{
		try {
			rs=statement1.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public void Updatestatement(String query)
	{
		try {
			statement1.executeUpdate(query);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	public void InsertStatement(String query)
	{
		try {
			statement1.executeUpdate(query);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	public void DisconnectFromDataBase(){
		try {
			connection1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
}







