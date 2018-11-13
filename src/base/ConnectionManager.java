package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionManager extends Remote{

	public static final String SERVICE_NAME = "Buchhaltung";
	
	void printMsg() throws RemoteException;
}
