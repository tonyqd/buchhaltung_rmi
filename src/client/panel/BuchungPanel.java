package client.panel;

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

public class BuchungPanel extends TemplatePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  JFrame buchungPanelFrame;
	private  JFrame parentFrame;
	private ResourceBundle bundle;
	private CookSwing cookSwing;
	
	public JPanel buchungPanel = null;
	public JPanel buchungInnerPanel = null;
	
	
	public JButton   buttonNeueBuchung;
	public JButton   buttonKontos;
	public JButton   buttonalleBuchungen;
	
	public BuchungPanel(ResourceBundle bundle, JFrame parent)
	{
		this.parentFrame = parent;
		this.bundle = bundle;
		cookSwing = new CookSwing(this);
		cookSwing.setResourceBundle(bundle);
		init();
	}
	
	public void init()
	{
		//Get UserObject
		initGUI();
		
		
	}

	private void initGUI()
	{
		parentFrame.setEnabled(true);
		buchungPanelFrame = (JFrame)cookSwing.render("src/client/panel/ext/buchungPanel.xml");
		buchungPanelFrame.setVisible(true);
		buchungPanelFrame.setFocusable(true);
	}	
	
	
	
	//Controller
	public WindowListener frameListener = new WindowAdapter ()
	{
		public void windowClosing (WindowEvent e)
		{
			final boolean leave = notifyForLeaving(buchungPanelFrame, bundle,bundle.getString("LeavingMessage"));
			if (leave)
			{
				buchungPanelFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				buchungPanelFrame.dispose();
			}
			else
			{
				buchungPanelFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			}
		}
	};
	
	
}
