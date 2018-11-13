package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JFrame;

import base.ConnectionManager;
import base.DatenbankManager;
import base.RMIStarter;
import client.panel.LoginPanel;


public class StartClient extends RMIStarter{
    String host = "";
	
	public StartClient(String host, int port) {
		super(ConnectionManager.class, port);
		this.host = host;
	}

	 public static void main(String[] args) {
		 JFrame mainFrame = new JFrame();
	     if (args.length != 2)
	     {
	    	 System.err.println("usage: java Client host port");
	    	 System.exit(1);
	     }
	     
	     int index = 0;
	     String host = args[index++];
	     int port = Integer.parseInt(args[index++]);
		 new StartClient(host, port);
		 LoginPanel loginPanel = new LoginPanel(mainFrame);
		 
	    }
	 
	public void doCustomRmiHandling(int port) {
		try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            ConnectionManager connectionStub = (ConnectionManager)registry.lookup(ConnectionManager.SERVICE_NAME);
            connectionStub.printMsg();
            
            DatenbankManager datenbankStub = (DatenbankManager)registry.lookup(DatenbankManager.SERVICE_NAME);
            datenbankStub.printMsg();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
		
	}
	
	

}
