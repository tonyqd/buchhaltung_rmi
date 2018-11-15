package server.imp;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import base.DatenbankManager;


public class DatenbankManagerImp implements DatenbankManager{
	
	private Connection conn = null;
	public DatenbankManagerImp()
	{
		conn = connectDatenbankServer();
	}
	
	public void printMsg() throws RemoteException {
		System.out.println("Connection to Datenbank Server is established!");
	}
	
	public int userLogin(String username, String passwort) throws RemoteException{
		return checkUserLogin(username, passwort);
	}

	
	
	
	private int checkUserLogin(String username, String passwort)
	{
		Statement statement = null;
		String SQLStatement = null;
		ResultSet resultSet = null;
		int usernumber= -1;
		int found = 0;
		
		try {
			statement = conn.createStatement();
			SQLStatement = "SELECT * FROM `user` WHERE `username` = '" + username + "' AND `passwort` = '" + passwort + "' ;";
			resultSet = statement.executeQuery(SQLStatement);
			
			while(resultSet.next())
			{
				found ++;
				usernumber = resultSet.getInt("usernumber");
			}
			
			
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (found == 1)
		{
			return usernumber;
		}
		else
		{
			return -1;
		}
		
		
	}
	
	private Connection connectDatenbankServer()
	{
		Connection conn = null;
		try
		{
			Class.forName ("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e)
		{
			System.out.println(" Unable to load driver. ");
			return null;
		}

		try
		{
			String url = "jdbc:mysql://db4free.net:3306/buchhaltung";
			String username = "yunqing";
			String password = "buchhaltung";

			conn = DriverManager.getConnection (url, username, password);
			System.out.println ("Database connection established!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}
	
}
