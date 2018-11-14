package client.manager;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import base.ConnectionManager;
import base.DatenbankManager;

public class ServerSystemManager {
	private static Registry registry;
	
	public ServerSystemManager(Registry registry)
	{
		ServerSystemManager.registry = registry;
	}
	
	public static ConnectionManager getConnectionManager()
	{
		ConnectionManager connectionStub = null;
		try {
			connectionStub = (ConnectionManager)registry.lookup(ConnectionManager.SERVICE_NAME);
		} catch (Exception e) {
			System.err.println("getConnectionManager() in ServerSystemManager ERROR!");
		} 
		return connectionStub;
	}
	
	public static DatenbankManager getDatenbankManager()
	{
		DatenbankManager datenbankStub = null;
		try {
			datenbankStub = (DatenbankManager)registry.lookup(DatenbankManager.SERVICE_NAME);
		} catch (Exception e) {
			System.err.println("getConnectionManager() in ServerSystemManager ERROR!");
		} 
		return datenbankStub;
	}
}
