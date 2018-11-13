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

import cookxml.cookswing.CookSwing;
import server.datenbank.DatenbankConnect;

public class LoginPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  JFrame mainFrame;
	public  DatenbankConnect DBConnection;
	private ResourceBundle bundle;

	private CookSwing cookSwing;

	public JTextField	textFieldBenutzer = null;
	public JPasswordField  textFieldPasswort = null;
	public JButton   buttonOK;
	public JButton   buttonAbrechen;
	public JLabel    textRegistieren;

	
	public int usernumber = -1;
	
	public LoginPanel(JFrame mainFrame)
	{
		this.mainFrame = mainFrame;
		Locale de_DE = new Locale("de", "DE");
		bundle = ResourceBundle.getBundle("client.bundles.buchhaltung", de_DE);
		cookSwing = new CookSwing(this);
		cookSwing.setResourceBundle(bundle);
	}

	public void init()
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

	public ActionListener loginCloseAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			final boolean leave = notifyForLeaving();
			if (leave)
			{
				mainFrame.dispose();
				DBConnection.closeConnection();
			}
		}
	};
	
	public WindowListener frameListener = new WindowAdapter ()
	{
		public void windowClosing (WindowEvent e)
		{
			final boolean leave = notifyForLeaving();
			if (leave)
			{
				mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				DBConnection.closeConnection();
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
//        	AnmeldePanel anmeldePanel	= new AnmeldePanel(mainFrame, DBConnection);
//        	anmeldePanel.init();
        	
        	System.out.println("Registieren!");
        	
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
			usernumber = DBConnection.userLogin(benutzerString, passwortString);
			
			if (usernumber == -1)
			{
				JOptionPane.showMessageDialog(mainFrame, anmeldungErrorMessage);
				return;
			}
			else
			{
				mainFrame.dispose();
			}
			return;
		}
	};
	

	protected boolean notifyForLeaving()
	{
		String titelStr = null;
		String yesString = null;
		String noString = null;
		String messageString = null;

		titelStr = bundle.getString("Leavingtitle");
		messageString = bundle.getString("LeavingMessage");
		yesString = bundle.getString("Ja");
		noString = bundle.getString("Nein");

		final Object[] options={yesString, noString};
		final int n = JOptionPane.showOptionDialog(this, messageString, titelStr, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
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

	class MyWindowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {}

		@Override
		public void windowClosed(WindowEvent arg0) {}

		@Override
		public void windowClosing(WindowEvent arg0) {
			final boolean leave = notifyForLeaving();
			if (leave)
			{
				mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				DBConnection.closeConnection();
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

