package client.panel;

import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import base.User;
import client.manager.ServerSystemManager;
import cookxml.cookswing.CookSwing;

public class MenuePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  JFrame menuePanelFrame;
	protected int userid;
	public User userObject = null;
	private  JFrame parentFrame;
	private ResourceBundle bundle;
	private CookSwing cookSwing;
	
	public JLabel WelcomeText = null;
	
	
	public MenuePanel(ResourceBundle bundle, JFrame parent, int userid)
	{
		this.parentFrame = parent;
		this.bundle = bundle;
		this.userid = userid;
		cookSwing = new CookSwing(this);
		cookSwing.setResourceBundle(bundle);
		init();
	}
	
	public void init()
	{
		//Get UserObject
		try {
			userObject = ServerSystemManager.getDatenbankManager().getUser(userid);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		initGUI();
		
		
	}

	private void initGUI()
	{
		parentFrame.setEnabled(false);
		menuePanelFrame = (JFrame)cookSwing.render("src/client/panel/ext/menuePanel.xml");
		menuePanelFrame.setVisible(true);
		menuePanelFrame.setFocusable(true);
		WelcomeText.setText("Willkommen Herr " + userObject.getNachname() + "!");
		
	}	
	
}
