package server.imp;

import java.rmi.RemoteException;

import base.DatenbankManager;


public class DatenbankManagerImp implements DatenbankManager{

	public void printMsg() throws RemoteException {
		System.out.println("Connection to Datenbank Server is established!");
	}
}
