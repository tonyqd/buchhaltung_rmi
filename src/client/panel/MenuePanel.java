package client.panel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import base.User;
import client.manager.ServerSystemManager;
import cookxml.cookswing.CookSwing;

public class MenuePanel extends TemplatePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  JFrame menuePanelFrame;
	private  JFrame parentFrame;
	private ResourceBundle bundle;
	private CookSwing cookSwing;
	
	public JLabel WelcomeText = null;
	
	public JButton   buttonNeueBuchung;
	public JButton   buttonKontos;
	public JButton   buttonalleBuchungen;
	
	public MenuePanel(ResourceBundle bundle, JFrame parent, int userid)
	{
		super();
		this.parentFrame = parent;
		this.bundle = bundle;
		this.userid = userid;
		cookSwing = new CookSwing(this);
		cookSwing.setResourceBundle(bundle);
		init();
	}
	
	public void init()
	{
		initGUI();
	}

	private void initGUI()
	{
		parentFrame.setEnabled(false);
		menuePanelFrame = (JFrame)cookSwing.render("src/client/panel/ext/menuePanel.xml");
		menuePanelFrame.setVisible(true);
		menuePanelFrame.setFocusable(true);
		WelcomeText.setText("Willkommen Herr " + getUserObject().getNachname() + "!");
	}	
	
	
	
	
	
	//Controller
	public WindowListener frameListener = new WindowAdapter ()
	{
		public void windowClosing (WindowEvent e)
		{
			final boolean leave = notifyForLeaving(menuePanelFrame, bundle,bundle.getString("LeavingMessage"));
			if (leave)
			{
				menuePanelFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				menuePanelFrame.dispose();
			}
			else
			{
				menuePanelFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			}
		}
	};
	
	public MouseListener buchungPanel = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			new BuchungPanel(bundle, menuePanelFrame);
		}
	};
	
	
	
}
