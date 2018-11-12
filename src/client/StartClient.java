package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import base.ConnectionInterface;
import base.RMIStarter;


public class StartClient extends RMIStarter{
    String host = "";
	
	public StartClient(String host, int port) {
		super(ConnectionInterface.class, port);
		this.host = host;
	}

	 public static void main(String[] args) {
		 
	     if (args.length != 2)
	     {
	    	 System.err.println("usage: java Client host port");
	    	 System.exit(1);
	     }
	     
	     int index = 0;
	     String host = args[index++];
	     int port = Integer.parseInt(args[index++]);
		 new StartClient(host, port);
	    }
	 
	public void doCustomRmiHandling(int port) {
		try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            ConnectionInterface connectionStub = (ConnectionInterface)registry.lookup(ConnectionInterface.SERVICE_NAME);
            connectionStub.printMsg();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
		
	}
	
	

}
