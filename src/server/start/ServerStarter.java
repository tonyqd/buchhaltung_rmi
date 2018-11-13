package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import base.ConnectionInterface;
import base.RMIStarter;

public class ServerStarter extends RMIStarter {

	public ServerStarter(int port) {
		super(ConnectionInterface.class, port);
	}

	public static void main(String[] args) {
		int index = 0;
		int port = Integer.parseInt(args[index++]);
		new ServerStarter(port);
	}

	@Override
	public void doCustomRmiHandling(int port) {
		try {
			ConnectionInterface connection = new ConnectionImp();
			ConnectionInterface connectionStub = (ConnectionInterface)UnicastRemoteObject.exportObject(connection, 0);

			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind(ConnectionInterface.SERVICE_NAME, connectionStub);
			System.out.println("Registered: " + ConnectionInterface.SERVICE_NAME + " -> " + connectionStub.getClass().getName() + 
					"[" +connectionStub + "]");
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


}
