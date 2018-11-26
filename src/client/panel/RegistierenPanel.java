package client.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import base.IconStore;
import client.manager.ServerSystemManager;
import cookxml.cookswing.CookSwing;



public class RegistierenPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  JFrame registierenPanelFrame;
	private  JFrame parentFrame;
	private ResourceBundle bundle;
	private CookSwing cookSwing;

	public JTextField	textFieldBenutzer = null;
	public JPasswordField  textFieldPasswort1 = null;
	public JPasswordField  textFieldPasswort2 = null;	
	public JLabel       textFieldBenutzerHinweis = null;
	public JLabel       benutzerIcon = null; 
	public JLabel       passwordIcon = null; 
	public JButton   buttonOK;
	public JButton   buttonAbrechen;
	private boolean usernameOk = false;
	private boolean passwordOk = false;

	public RegistierenPanel(ResourceBundle bundle, JFrame parent)
	{
		this.parentFrame = parent;
		this.bundle = bundle;
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
		registierenPanelFrame = (JFrame)cookSwing.render("src/client/panel/ext/registierenPanel.xml");
		registierenPanelFrame.setVisible(true);
		registierenPanelFrame.setFocusable(true);
		textFieldBenutzer.getDocument().addDocumentListener(documentListener);
	}	


	//Controller

	public ActionListener CloseAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			final boolean leave = LoginPanel.notifyForLeaving(registierenPanelFrame, bundle, bundle.getString("AnmeldungLeavingMessage"));
			if (leave)
			{
				registierenPanelFrame.dispose();
				parentFrame.setFocusable(true);
				parentFrame.setVisible(true);
				parentFrame.setEnabled(true);
			}
		}
	};

	public WindowListener frameListener = new WindowAdapter ()
	{
		public void windowClosing (WindowEvent e)
		{
			final boolean leave = LoginPanel.notifyForLeaving(registierenPanelFrame, bundle,bundle.getString("AnmeldungLeavingMessage"));
			if (leave)
			{
				registierenPanelFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				registierenPanelFrame.dispose();
				parentFrame.setFocusable(true);
				parentFrame.setVisible(true);
				parentFrame.setEnabled(true);
			}
			else
			{
				registierenPanelFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			}
		}
	};

	public FocusListener checktextFieldPasswort1 = new FocusAdapter() 
	{
		public void focusLost(FocusEvent e) {
			char[] password;

			password = textFieldPasswort1.getPassword();

			if (password.length == 0)
			{
				textFieldBenutzerHinweis.setText("Passwort darf nicht leer sein!" );
				passwordOk = false;
			}
		}
	};

	public FocusListener checktextFieldPasswort2 = new FocusAdapter() 
	{
		public void focusLost(FocusEvent e) {
			String pass1="", pass2="";

			if (textFieldPasswort2.getPassword().length == 0)
			{
				textFieldBenutzerHinweis.setText("Passwort darf nicht leer sein!" );
				passwordOk = false;
				return;
			}

			for(int i = 0; i < textFieldPasswort1.getPassword().length; i++)
			{
				pass1 = pass1 + textFieldPasswort1.getPassword()[i];
			}
			
			for(int i = 0; i < textFieldPasswort2.getPassword().length; i++)
			{
				pass2 = pass2 + textFieldPasswort2.getPassword()[i];
			}
			
			
			if (pass1.equals(pass2))
			{
				passwordIcon.setIcon(IconStore.getokButton());
				passwordOk= true;
				textFieldBenutzerHinweis.setText("");
				if (usernameOk)
				{
					buttonOK.setEnabled(true);
				}
			}
			else
			{
				textFieldBenutzerHinweis.setText("Passworts sind nicht identisch!" );
				passwordIcon.setIcon(IconStore.getabrechenButton());
				passwordOk = false;
			}
		}
	};

	public DocumentListener documentListener = new DocumentListener() {
	      public void changedUpdate(DocumentEvent documentEvent) {
	    	  check();
	      }
	      public void insertUpdate(DocumentEvent documentEvent) {
	    	  check();
	      }
	      public void removeUpdate(DocumentEvent documentEvent) {
	    	  check();
	      }
	      
	      private void check()
	      {
	    	  int return_value = 0;
				
				if (textFieldBenutzer.getText().length() == 0)
				{
					textFieldBenutzerHinweis.setText("Username darf nicht leer sein!");
					benutzerIcon.setIcon(IconStore.getabrechenButton());
					usernameOk = false;
					return;
				}
				
				try {
					return_value = ServerSystemManager.getDatenbankManager().checkUserAvailable(textFieldBenutzer.getText());
				} catch (RemoteException e1) {
					System.err.println("ServerSystemManager.getDatenbankManager().checkUserAvailable error!");
				}

				if (return_value < 0)
				{
					textFieldBenutzerHinweis.setText("Username: " + textFieldBenutzer.getText() + " ist verfügbar!");
					benutzerIcon.setIcon(IconStore.getokButton());
					usernameOk = true;
					if (passwordOk)
					{
						buttonOK.setEnabled(true);
					}
				}
				else
				{
					textFieldBenutzerHinweis.setText("Username: " + textFieldBenutzer.getText() + " ist nicht verfügbar!");
					benutzerIcon.setIcon(IconStore.getabrechenButton());
//					textFieldBenutzer.grabFocus();
					textFieldBenutzer.selectAll();
					usernameOk = false;
				}
	      }
	    };


	
	public FocusListener checkUserNameAvailable = new FocusAdapter() 
	{
		public void focusLost(FocusEvent e) {
			int return_value = 0;
			System.out.println("User entered " + textFieldBenutzer.getText());
			
			if (textFieldBenutzer.getText().length() == 0)
			{
				textFieldBenutzerHinweis.setText("Username darf nicht leer sein!");
				benutzerIcon.setIcon(IconStore.getabrechenButton());
				usernameOk = false;
				return;
			}
			
			try {
				return_value = ServerSystemManager.getDatenbankManager().checkUserAvailable(textFieldBenutzer.getText());
			} catch (RemoteException e1) {
				System.err.println("ServerSystemManager.getDatenbankManager().checkUserAvailable error!");
			}

			if (return_value < 0)
			{
				textFieldBenutzerHinweis.setText("Username: " + textFieldBenutzer.getText() + " ist verfügbar!");
				benutzerIcon.setIcon(IconStore.getokButton());
				usernameOk = true;
				if (passwordOk)
				{
					buttonOK.setEnabled(true);
				}
			}
			else
			{
				textFieldBenutzerHinweis.setText("Username: " + textFieldBenutzer.getText() + " ist nicht verfügbar!");
				benutzerIcon.setIcon(IconStore.getabrechenButton());
//				textFieldBenutzer.grabFocus();
				textFieldBenutzer.selectAll();
				usernameOk = false;
			}
		}
		
		public void focusGained(FocusEvent e) {
			textFieldBenutzerHinweis.setText("");
			benutzerIcon.setIcon(null);
			usernameOk = false;
		}
	};

	public ActionListener OkAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			char[] passwort =  textFieldPasswort2.getPassword();
			int usernumber = -1;
			String benutzerString = textFieldBenutzer.getText();
			String passwortString = "";

			// Wenn Benutzername und Passwort eingegeben sind
			for(int i = 0; i < passwort.length; i++)
			{
				passwortString = passwortString + passwort[i];
			}
			try {
				usernumber = ServerSystemManager.getDatenbankManager().createUser(benutzerString, passwortString);
				System.out.println("usernumber: " + usernumber);
			} catch (RemoteException e1) {
				System.out.println("Datenbank connection error!");
			} 

			if (usernumber == 1)
			{
				JOptionPane.showMessageDialog(registierenPanelFrame, "successful!");
				textFieldBenutzerHinweis.setText("");
				registierenPanelFrame.dispose();
				parentFrame.setFocusable(true);
				parentFrame.setVisible(true);
				parentFrame.setEnabled(true);
				return;
			}
			else
			{
				JOptionPane.showMessageDialog(registierenPanelFrame, "Failed!");
			}
			return;
		}
	};
	




}
