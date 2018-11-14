package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DatenbankManager  extends Remote{
	public static final String SERVICE_NAME = "Datenbank";

	void printMsg() throws RemoteException;

	int userLogin(String username, String passwort) throws RemoteException;
	
}
