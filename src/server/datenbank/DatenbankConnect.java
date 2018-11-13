package server.datenbank;
import java.sql.*;


public class DatenbankConnect{

	private Connection conn = null;

	public void initConnection()
	{

		try
		{
			Class.forName ("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e)
		{
			System.out.println(" Unable to load driver. ");
			return;
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
	}

	
	public int userLogin(String username, String passwort)
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

	public void closeConnection()
	{
		try {
			if (conn != null) {
				conn.close();
			}
			System.out.println("DB Connection closed successfully! ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
