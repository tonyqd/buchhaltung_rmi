package server.imp;

import java.rmi.RemoteException;

import base.ConnectionManager;


public class ConnectionManagerImp implements ConnectionManager{

	public void printMsg() throws RemoteException {
		System.out.println("Connection to Server is established!");
	}
}
