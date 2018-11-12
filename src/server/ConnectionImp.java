package server;

import java.rmi.RemoteException;

import base.ConnectionInterface;


public class ConnectionImp implements ConnectionInterface{

	public void printMsg() throws RemoteException {
		System.out.println("Greeting from Server!");
	}
	
}
