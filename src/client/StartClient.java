package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import base.ConnectionManager;
import base.DatenbankManager;
import base.RMIStarter;
import client.manager.ServerSystemManager;
import client.panel.LoginPanel;


public class StartClient extends RMIStarter{
	String host = "";
	JFrame mainFrame;
	public static Registry registry;

	public StartClient(String host, int port, JFrame mainFrame) {
		super(ConnectionManager.class, port);
		this.host = host;
		this.mainFrame = mainFrame;

	}



	public void doCustomRmiHandling(int port) {

		try {
			registry = LocateRegistry.getRegistry(host, port);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		new ServerSystemManager(registry);
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
		new StartClient(host, port, mainFrame);

		LoginPanel loginPanel = new LoginPanel(mainFrame);
		loginPanel.init();

	}


}
