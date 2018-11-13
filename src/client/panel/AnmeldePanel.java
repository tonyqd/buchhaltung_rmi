package client.panel;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cookxml.cookswing.CookSwing;
import server.datenbank.DatenbankConnect;



public class AnmeldePanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  JFrame mainFrame;
	public  DatenbankConnect DBConnection;
	private ResourceBundle bundle;
	private CookSwing cookSwing;
	
	
	
	public AnmeldePanel(JFrame mainFrame, DatenbankConnect DBConnection)
	{
		this.mainFrame = mainFrame;
		this.DBConnection = DBConnection;
		Locale de_DE = new Locale("de", "DE");
		bundle = ResourceBundle.getBundle("bundles.buchhaltung", de_DE);
		cookSwing = new CookSwing(this);
		cookSwing.setResourceBundle(bundle);
	}
	
	public void init()
	{
		mainFrame = (JFrame)cookSwing.render("src/ext/anmeldePanel.xml");
		mainFrame.setVisible(true);
		mainFrame.setFocusable(true);
//		mainFrame.addWindowListener(new MyWindowListener());
	}
	
	public void showPanel()
	{
		this.setVisible(true);
	}
}