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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import base.BuchungType;
import base.Konto;
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
	public JPanel vonPanel = null;
	public JPanel bisPanel = null;
	public JPanel currentPanel = null;

	public JComboBox<String> comboboxBuchungtypen;
	public JComboBox<String> comboboxKonto;
	public JButton   buttonBuchungSpeichern;
	public JButton   buttonBuchungAbrechen;
	public JButton   buttonZeitraumUpdate;
	public JFormattedTextField textFieldUhrzeit = null;
	public JFormattedTextField textfieldBetrag = null;

	public UtilDateModel model1 = null;
	public UtilDateModel model2 = null;
	public UtilDateModel model3 = null;
	public JDatePanelImpl datePanel1 = null;
	public JDatePanelImpl datePanel2 = null;
	public JDatePanelImpl datePanel3 = null;
	public JDatePickerImpl datePickerVon = null;
	public JDatePickerImpl datePickerBis = null;
	public JDatePickerImpl datePickerCurrent = null;



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

		Format timeFormat = new SimpleDateFormat("HH:mm");
		textFieldUhrzeit = new JFormattedTextField(timeFormat);


		buchungPanelFrame = (JFrame)cookSwing.render("src/client/panel/ext/buchungPanel.xml");
		buchungPanelFrame.setVisible(true);
		buchungPanelFrame.setFocusable(true);

		initComboboxBuchungstypen();
		initComboboxKonto();
		initDatePicker();
		initTimeField();

	}	

	// View
	public class DateLabelFormatter extends AbstractFormatter {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String datePattern = "yyyy-MM-dd";
		private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormatter.parseObject(text);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
				Calendar cal = (Calendar) value;
				return dateFormatter.format(cal.getTime());
			}

			return "";
		}

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
			Calendar cal = Calendar.getInstance();
			int bisDay = cal.get(Calendar.DAY_OF_MONTH);
			int bisMonth = cal.get(Calendar.MONTH);
			int bisYear = cal.get(Calendar.YEAR);
			model3.setDate(bisYear, bisMonth, bisDay);
			model3.setSelected(true);
			initTimeField();
		}
	};

	public ActionListener SpeichernAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			String betragString;
			String datumString;
			String uhrzeitString;
	        int buchungType;
	        int konto;
			
			// Get Betrag
			betragString = textfieldBetrag.getText();
			if (betragString.equals(""))
			{
				JOptionPane.showMessageDialog(buchungPanelFrame, "Betrag darf nicht leer sein!");
				return;
			}
			betragString = betragString.replace(",", "");
			betragString = betragString.replace(".", "");

			// Get Datum und Uhrzeit
			Date Datum = model3.getValue();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			datumString = formatter.format(Datum);
			uhrzeitString=textFieldUhrzeit.getText();
			uhrzeitString = uhrzeitString.concat(":00");
			
			// Get Buchungstype/Konto
			String Katalog = comboboxBuchungtypen.getSelectedItem().toString();
			buchungType = BuchungtypenStringToInt.get(Katalog);
			String Kontotype = comboboxKonto.getSelectedItem().toString();
			konto = KontonameStringToInt.get(Kontotype);
			
			System.out.println("Datum: " + datumString);
			System.out.println("Betrag: " + betragString);
			System.out.println("Uhrzeit: " + uhrzeitString);
			System.out.println("Katalog: " + buchungType);
			System.out.println("Konto: " + konto);
			System.out.println("uid: " + getUserObject().getUserid());
			
			try {
				ServerSystemManager.getDatenbankManager().insertBuchung(getUserObject().getUserid(), konto, buchungType, betragString, datumString, uhrzeitString);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			
		}
	};

	public ActionListener RefreshAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			Calendar cal =  Calendar.getInstance();
			cal.setTime(model1.getValue());
			int selectedVonDay = cal.get(Calendar.DAY_OF_MONTH);
			int selectedVonMonth = cal.get(Calendar.MONTH) +1;   // wegen bug in datepicker!!!
			int selectedVonYear = cal.get(Calendar.YEAR);

			cal.setTime(model2.getValue());
			int selectedBisDay =cal.get(Calendar.DAY_OF_MONTH);
			int selectedBisMonth = cal.get(Calendar.MONTH) +1;  // wegen bug in datepicker!!!
			int selectedBisYear = cal.get(Calendar.YEAR);
			//			System.out.println(model1.getValue().toString());
			//			System.out.println(model2.getValue().toString());
			//			System.out.println(selectedVonYear +""+ selectedVonMonth+"" +selectedVonDay );
			//			System.out.println(selectedBisYear+""+selectedBisMonth+ "" +selectedBisDay );
			System.out.println("refresh action!");
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

	private void initDatePicker()
	{
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		model1 = new UtilDateModel();
		model2 = new UtilDateModel();
		model3 = new UtilDateModel();
		datePanel1 = new JDatePanelImpl(model1, p);
		datePanel2 = new JDatePanelImpl(model2, p);
		datePanel3 = new JDatePanelImpl(model3, p);
		datePickerVon = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
		datePickerBis = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
		datePickerCurrent = new JDatePickerImpl(datePanel3, new DateLabelFormatter());
		vonPanel.add(datePickerVon);
		bisPanel.add(datePickerBis);
		currentPanel.add(datePickerCurrent);

		Calendar cal = Calendar.getInstance();
		int bisDay = cal.get(Calendar.DAY_OF_MONTH);
		int bisMonth = cal.get(Calendar.MONTH);
		int bisYear = cal.get(Calendar.YEAR);

		cal.add(Calendar.DATE, -30);
		int vonDay = cal.get(Calendar.DAY_OF_MONTH);
		int vonMonth = cal.get(Calendar.MONTH);
		int vonYear = cal.get(Calendar.YEAR);

		model1.setDate(vonYear, vonMonth, vonDay);
		model2.setDate(bisYear, bisMonth, bisDay);
		model3.setDate(bisYear, bisMonth, bisDay);
		model2.setSelected(true);
		model1.setSelected(true);
		model3.setSelected(true);
	}

	private void initTimeField()
	{
		Calendar cal = Calendar.getInstance();
		textFieldUhrzeit.setValue(cal.getTime());
	}


}
