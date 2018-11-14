package server.start;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import base.ConnectionManager;
import base.DatenbankManager;
import base.RMIStarter;
import server.imp.ConnectionManagerImp;
import server.imp.DatenbankManagerImp;

public class ServerStarter extends RMIStarter {

	public ServerStarter(int port) {
		super(ConnectionManager.class, port);
	}

	public static void main(String[] args) {
		int index = 0;
		int port = Integer.parseInt(args[index++]);
		new ServerStarter(port);
//		DatenbankConnect dbConnection = new DatenbankConnect();
//		dbConnection.initConnection();
	}

	@Override
	public void doCustomRmiHandling(int port) {
		Registry registry = null;
		
		// ConnectionManager
		try {
			ConnectionManager connection = new ConnectionManagerImp();
			ConnectionManager connectionStub = (ConnectionManager)UnicastRemoteObject.exportObject(connection, 0);
			registry = LocateRegistry.createRegistry(port);
			registry.bind(ConnectionManager.SERVICE_NAME, connectionStub);
			System.out.println("Registered: " + ConnectionManager.SERVICE_NAME + " -> " + connectionStub.getClass().getName() + 
					"[" +connectionStub + "]");
		}
		catch(RemoteException e) {
			try{
				registry = LocateRegistry.getRegistry(port);
				ConnectionManager connection = new ConnectionManagerImp();
				ConnectionManager connectionStub = (ConnectionManager)UnicastRemoteObject.exportObject(connection, 0);
				registry.rebind(ConnectionManager.SERVICE_NAME, connectionStub);
				System.out.println("Registered: " + ConnectionManager.SERVICE_NAME + " -> " + connectionStub.getClass().getName() + 
						"[" +connectionStub + "]");
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
		catch(AlreadyBoundException e)
		{
			System.out.println("registry bind error!");
		}

		// DatenbankManager
		try {
			DatenbankManager datenbank = new DatenbankManagerImp();
			DatenbankManager datenbankStub = (DatenbankManager)UnicastRemoteObject.exportObject(datenbank, 0);
			registry = LocateRegistry.getRegistry(port);
			registry.bind(DatenbankManager.SERVICE_NAME, datenbankStub);
			System.out.println("Registered: " + DatenbankManager.SERVICE_NAME + " -> " + datenbankStub.getClass().getName() + 
					"[" +datenbankStub + "]");
		}
		catch(Exception e) {
			try{
			DatenbankManager datenbank = new DatenbankManagerImp();
			DatenbankManager datenbankStub = (DatenbankManager)UnicastRemoteObject.exportObject(datenbank, 0);
			registry = LocateRegistry.getRegistry(port);
			registry.rebind(DatenbankManager.SERVICE_NAME, datenbankStub);
			System.out.println("Registered: " + DatenbankManager.SERVICE_NAME + " -> " + datenbankStub.getClass().getName() + 
					"[" +datenbankStub + "]");
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}


	}


}
