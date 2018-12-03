package client.panel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import base.ConnectionManager;
import base.DatenbankManager;
import client.manager.ServerSystemManager;
import cookxml.cookswing.CookSwing;

public class LoginPanel extends TemplatePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  JFrame mainFrame;
	private ResourceBundle bundle;
	private CookSwing cookSwing;

	public JTextField	textFieldBenutzer = null;
	public JPasswordField  textFieldPasswort = null;
	public JButton   buttonOK;
	public JButton   buttonAbrechen;
	public JLabel    textRegistieren;

	protected int usernumber = -1;



	public LoginPanel(JFrame mainFrame)
	{
		this.mainFrame = mainFrame;
		Locale de_DE = new Locale("de", "DE");
		bundle = ResourceBundle.getBundle("client.bundles.buchhaltung", de_DE);
		cookSwing = new CookSwing(this);
		cookSwing.setResourceBundle(bundle);
	}

	//******************* View **************************************// 
	public void init()
	{
		initGUI();
	}

	private void initGUI()
	{
		mainFrame = (JFrame)cookSwing.render("src/client/panel/ext/loginPanel.xml");

		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		int w = mainFrame.getSize().width;
		int h = mainFrame.getSize().height;
		int x = (dim.width-w)/2;
		int y = (dim.height-h)/2;

		// Move the window
		mainFrame.setLocation(x, y);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}


	// **************** Controller *******************************************// 
	public ActionListener loginCloseAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			final boolean leave = notifyForLeaving(mainFrame, bundle,bundle.getString("LeavingMessage"));
			if (leave)
			{
				mainFrame.dispose();
			}
		}
	};

	public WindowListener frameListener = new WindowAdapter ()
	{
		public void windowClosing (WindowEvent e)
		{
			final boolean leave = notifyForLeaving(mainFrame, bundle,bundle.getString("LeavingMessage"));
			if (leave)
			{
				mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				mainFrame.dispose();
			}
			else
			{
				mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			}
		}
	};

	public MouseListener registierenListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			new RegistierenPanel(bundle, mainFrame);
		}
	};

	public ActionListener loginOkAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			char[] passwort =  textFieldPasswort.getPassword();
			String benutzerErrorMessage = bundle.getString("benutzerErrorMessage");
			String passwortErrorMessage = bundle.getString("passwortErrorMessage");
			String anmeldungErrorMessage = bundle.getString("anmeldungErrorMessage");

			String benutzerString = textFieldBenutzer.getText();
			String passwortString = "";
			if (benutzerString.equals(""))
			{
				JOptionPane.showMessageDialog(mainFrame, benutzerErrorMessage);
				return;
			}

			if (passwort.length == 0)
			{
				JOptionPane.showMessageDialog(mainFrame, passwortErrorMessage);
				return;
			}

			// Wenn Benutzername und Passwort eingegeben sind
			for(int i = 0; i < passwort.length; i++)
			{
				passwortString = passwortString + passwort[i];
			}
			try {
				usernumber = getDatenbankManager().userLogin(benutzerString, passwortString);
			} catch (RemoteException e1) {
				System.out.println("Datenbank connection error!");
			} 

			if (usernumber == -1)
			{
				JOptionPane.showMessageDialog(mainFrame, anmeldungErrorMessage);
				textFieldPasswort.setText("");
				return;
			}
			else
			{
				new MenuePanel(bundle, mainFrame, usernumber);
				mainFrame.dispose();
			}
			return;
		}
	};

	class MyWindowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {}

		@Override
		public void windowClosed(WindowEvent arg0) {
			mainFrame.dispose();
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			final boolean leave = notifyForLeaving(mainFrame, bundle, bundle.getString("LeavingMessage"));
			if (leave)
			{
				mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				mainFrame.dispose();
			}
			else
			{
				mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			}
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {}

		@Override
		public void windowDeiconified(WindowEvent arg0) {}

		@Override
		public void windowIconified(WindowEvent arg0) {}

		@Override
		public void windowOpened(WindowEvent arg0) {}

	}

}

