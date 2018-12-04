package client.panel;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import base.BuchungType;
import base.ConnectionManager;
import base.DatenbankManager;
import base.Konto;
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
	private List<BuchungType> buchungTypenObject = null;
	private List<Konto> KontonamenObject = null;
	protected HashMap<Integer, String> KontonameIntToString = new HashMap<Integer, String>();
	protected HashMap<String, Integer> KontonameStringToInt = new HashMap<String, Integer>();
	protected HashMap<Integer, String> BuchungtypenIntToString = new HashMap<Integer, String>();
	protected HashMap<String, Integer> BuchungtypenStringToInt = new HashMap<String, Integer>();
	protected int userid = -1;
	
	protected List<JFrame> runningJFrame = new ArrayList<JFrame>();



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
	
	protected List<BuchungType> getBuchungTypen()
	{
		if (buchungTypenObject == null)
		{
			try {
				buchungTypenObject = ServerSystemManager.getDatenbankManager().getBuchungTypen();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			BuchungtypenIntToString.clear();
			BuchungtypenStringToInt.clear();
			for(BuchungType temp : buchungTypenObject)
			{
				BuchungtypenIntToString.put(temp.getIndex(), temp.getBuchungname());
				BuchungtypenStringToInt.put(temp.getBuchungname(), temp.getIndex());
			}
		}
		
		return buchungTypenObject;
	}
	
	protected List<Konto> getKontonamen()
	{
		if (KontonamenObject == null)
		{
			try {
				KontonamenObject = ServerSystemManager.getDatenbankManager().getKontonamen();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			KontonameIntToString.clear();
			KontonameStringToInt.clear();
			for(Konto temp : KontonamenObject)
			{
				KontonameIntToString.put(temp.getIndex(), temp.getKontoname());
				KontonameStringToInt.put(temp.getKontoname(), temp.getIndex());
			}
		}
		
		return KontonamenObject;
	}


}

