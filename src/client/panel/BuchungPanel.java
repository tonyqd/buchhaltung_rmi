package client.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import base.BuchungType;
import base.Konto;
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
	
	public JComboBox<String> comboboxBuchungtypen;
	public JComboBox<String> comboboxKonto;
	public JButton   buttonBuchungSpeichern;
	public JButton   buttonBuchungAbrechen;
	public JFormattedTextField textFieldDatum = null;
	public JFormattedTextField textFieldUhrzeit = null;
	public JFormattedTextField textfieldBetrag = null;
	
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
		initGUI();
	}

	private void initGUI()
	{
		parentFrame.setEnabled(true);
		NumberFormat percentFormat = NumberFormat.getNumberInstance();
		percentFormat.setMinimumFractionDigits(2);
		textfieldBetrag = new JFormattedTextField(percentFormat);
		
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		textFieldDatum = new JFormattedTextField(format);
		
		Format timeFormat = new SimpleDateFormat("HH:mm");
		textFieldUhrzeit = new JFormattedTextField(timeFormat);
		
		buchungPanelFrame = (JFrame)cookSwing.render("src/client/panel/ext/buchungPanel.xml");
		buchungPanelFrame.setVisible(true);
		buchungPanelFrame.setFocusable(true);

		initComboboxBuchungstypen();
		initComboboxKonto();
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
	
	public ActionListener AbrechenAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			textfieldBetrag.setText("");
			textFieldDatum.setText("");
			textFieldUhrzeit.setText("");
		}
	};
	
	
	private void initComboboxBuchungstypen()
	{
		for(BuchungType temp : getBuchungTypen())
		{
			comboboxBuchungtypen.addItem(temp.getBuchungname());
		}
	}
	
	private void initComboboxKonto()
	{
		for(Konto temp : getKontonamen())
		{
			comboboxKonto.addItem(temp.getKontoname());
		}
	}
	
	
}
