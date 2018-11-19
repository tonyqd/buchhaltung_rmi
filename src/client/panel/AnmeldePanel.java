package client.panel;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cookxml.cookswing.CookSwing;



public class AnmeldePanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  JFrame mainFrame;
	private ResourceBundle bundle;
	private CookSwing cookSwing;
	
	public JTextField	textFieldBenutzer = null;
	public JPasswordField  textFieldPasswort1 = null;
	public JPasswordField  textFieldPasswort2 = null;	
	
	
	public AnmeldePanel(ResourceBundle bundle)
	{
//		this.mainFrame = mainFrame;
		this.bundle = bundle;
		cookSwing = new CookSwing(this);
		cookSwing.setResourceBundle(bundle);
	}
	
	public void init()
	{
		mainFrame = (JFrame)cookSwing.render("src/client/panel/ext/anmeldePanel.xml");
		mainFrame.setVisible(true);
		mainFrame.setFocusable(true);
//		mainFrame.addWindowListener(new MyWindowListener());
	}
	
	public void showPanel()
	{
		this.setVisible(true);
	}
}
