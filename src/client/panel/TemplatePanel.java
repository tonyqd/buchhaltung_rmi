package client.panel;

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import base.ConnectionManager;
import base.DatenbankManager;
import base.User;
import client.manager.ServerSystemManager;
import cookxml.cookswing.CookSwing;

public class TemplatePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ResourceBundle bundle;
	private CookSwing cookSwing;

	protected DatenbankManager datenbankStub = null; 
	protected ConnectionManager connectionStub = null;
	private User userObject = null;
	protected int userid = -1;



	public TemplatePanel()
	{
		Locale de_DE = new Locale("de", "DE");
		bundle = ResourceBundle.getBundle("client.bundles.buchhaltung", de_DE);
		cookSwing = new CookSwing(this);
		cookSwing.setResourceBundle(bundle);
	}

	//******************* View **************************************// 


	// **************** Controller *******************************************// 


	protected boolean notifyForLeaving(JFrame frame, ResourceBundle bundle, String LeavingMessage)
	{
		String titelStr = null;
		String yesString = null;
		String noString = null;
		String messageString = null;

		titelStr = bundle.getString("Leavingtitle");
		messageString = LeavingMessage;
		yesString = bundle.getString("Ja");
		noString = bundle.getString("Nein");

		final Object[] options={yesString, noString};
		final int n = JOptionPane.showOptionDialog(frame, messageString, titelStr, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
				null,options, options[1]);

		switch(n)
		{
		case JOptionPane.YES_OPTION: 
			return true;
		case JOptionPane.NO_OPTION:
			return false;
		case JOptionPane.CLOSED_OPTION:
			return false;	
		}

		return false;
	}


	protected ConnectionManager getConnectionManager()
	{
		if (connectionStub == null)
		{
			connectionStub = ServerSystemManager.getConnectionManager();
		}
		return connectionStub;
	}

	protected DatenbankManager getDatenbankManager()
	{
		if (datenbankStub == null)
		{
			datenbankStub = ServerSystemManager.getDatenbankManager();
		}
		return datenbankStub;
	}

	protected User getUserObject()
	{
		if (userObject == null)
		{
			//Get UserObject
			try {
				userObject = ServerSystemManager.getDatenbankManager().getUser(userid);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return userObject;
	}

}

