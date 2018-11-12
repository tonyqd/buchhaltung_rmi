package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionInterface extends Remote{

	public static final String SERVICE_NAME = "Buchhaltung";
	
	void printMsg() throws RemoteException;
}
