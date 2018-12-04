package server.imp;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import base.BuchungType;
import base.DatenbankManager;
import base.Konto;
import base.User;


public class DatenbankManagerImp implements DatenbankManager{

	private Connection conn = null;
	private Statement statement = null;
	private String SQLStatement = null;
	private ResultSet resultSet = null;
	
	
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

	public int checkUserAvailable(String username) throws RemoteException{
		return checkUserAvailableImp(username);

	}

	public int createUser(String username, String passwort, String vorname, String nachname, int Geschlecht) 
			throws RemoteException{
		return createNewUser(username, passwort, vorname, nachname, Geschlecht);
	}
	
	public User getUser(int userid) throws RemoteException{
		return getUserObject(userid);
	}
	
	public List<BuchungType> getBuchungTypen() throws RemoteException{
		return getBuchungTypenPrivate();
	}
	
	public List<Konto> getKontonamen() throws RemoteException{
		return getKontonamenPrivate();
	}

	
	private List<Konto> getKontonamenPrivate()
	{
		List<Konto> Konto = new ArrayList<Konto>();
		
		String typename = null;
		int index = 0;
		
		try {
			statement = conn.createStatement();
			SQLStatement = "SELECT * FROM `Konto`;";
			resultSet = statement.executeQuery(SQLStatement);

			while(resultSet.next())
			{
				typename = resultSet.getString("KONTONAME");
				index = resultSet.getInt("number");
				Konto result = new Konto(index, typename);
				Konto.add(result);
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
		
		return Konto;
	}
	
	
	private List<BuchungType> getBuchungTypenPrivate()
	{
		List<BuchungType> Typen = new ArrayList<BuchungType>();
		
		String typename = null;
		int index = 0;
		
		try {
			statement = conn.createStatement();
			SQLStatement = "SELECT * FROM `Buchungtypen`;";
			resultSet = statement.executeQuery(SQLStatement);

			while(resultSet.next())
			{
				typename = resultSet.getString("TYPE");
				index = resultSet.getInt(1);
				BuchungType result = new BuchungType(index, typename);
				Typen.add(result);
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
		
		return Typen;
	}
	
	private User getUserObject(int userid)
	{
		User userobject = null;
	    String username;
		String vorname;
		String nachname;
		
		try {
			statement = conn.createStatement();
			SQLStatement = "SELECT * FROM `user` WHERE `usernumber` = " + userid + " ;";
			resultSet = statement.executeQuery(SQLStatement);

			while(resultSet.next())
			{
				username = resultSet.getString("USERNAME");
				vorname  = resultSet.getString("VORNAME");
				nachname = resultSet.getString("NACHNAME");
				userobject = new User(userid, username, vorname, nachname);
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
		
		return userobject;
	}
	
	
	
	
	
	
	
	
	

	private int createNewUser(String username, String passwort, String vorname, String nachname, int Geschlecht)
	{
		int usernumber = 0;
		int returncode = 0;


		try {
			statement = conn.createStatement();
			SQLStatement = "SELECT MAX(usernumber) FROM user ";
			resultSet = statement.executeQuery(SQLStatement);

			while(resultSet.next())
			{
				usernumber = resultSet.getInt(1);
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

				usernumber ++;

		try {
			statement = conn.createStatement();
			SQLStatement = "INSERT INTO `user`  (`usernumber`, `username`, `passwort`, `berechtigung`, `Vorname`, `Nachname`, `Geschlecht`) VALUES (" +  
					usernumber + ", \""+ username + "\", \""+ passwort + "\", 1, \""+ vorname + "\", \""+ nachname +"\", "+Geschlecht+" );";
			System.out.println(SQLStatement.toString());
			returncode = statement.executeUpdate(SQLStatement);

			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			return -1;
		}

		return returncode;
	}

	private int checkUserAvailableImp(String username)
	{
		int found = 0;

		try {
			statement = conn.createStatement();
			SQLStatement = "SELECT * FROM `user` WHERE `username` = '" + username + "' ;";
			resultSet = statement.executeQuery(SQLStatement);

			while(resultSet.next())
			{
				found ++;
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

		if (found == 0)
		{
			return -1;
		}
		else
		{
			return found;
		}


	}



	private int checkUserLogin(String username, String passwort)
	{
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}

}
