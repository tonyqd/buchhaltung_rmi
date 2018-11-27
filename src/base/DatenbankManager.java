package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DatenbankManager  extends Remote{
	public static final String SERVICE_NAME = "Datenbank";

	void printMsg() throws RemoteException;

	int userLogin(String username, String passwort) throws RemoteException;
	
	int checkUserAvailable(String username) throws RemoteException;
	
	int createUser(String username, String passwort, String vorname, String nachname, int Geschlecht) throws RemoteException;
	
	User getUser(int userid) throws RemoteException;
}
