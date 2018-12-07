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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import base.Buchung;
import base.BuchungType;
import base.Konto;
import client.elements.YDjxtable;
import client.manager.ServerSystemManager;
import cookxml.cookswing.CookSwing;

public class BuchungPanel extends TemplatePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  JFrame buchungPanelFrame;
	private  JFrame parentFrame;
	private ResourceBundle globalBundle;
	protected ResourceBundle localBundle;
	private CookSwing cookSwing;
	protected List<Buchung> buchungTableData = null;

	public JPanel buchungPanel = null;
	public JPanel buchungInnerPanel = null;
	public JPanel vonPanel = null;
	public JPanel bisPanel = null;
	public JPanel currentPanel = null;

	public JLabel statusbar = null;
	public JComboBox<String> comboboxBuchungtypen;
	public JComboBox<String> comboboxKonto;
	public JButton   buttonBuchungSpeichern;
	public JButton   buttonBuchungAbrechen;
	public JButton   buttonZeitraumUpdate;
	public JFormattedTextField textFieldUhrzeit = null;
	public JFormattedTextField textfieldBetrag = null;
	public JScrollPane scrollPaneBuchungTable = null;

	public UtilDateModel model1 = null;
	public UtilDateModel model2 = null;
	public UtilDateModel model3 = null;
	public JDatePanelImpl datePanel1 = null;
	public JDatePanelImpl datePanel2 = null;
	public JDatePanelImpl datePanel3 = null;
	public JDatePickerImpl datePickerVon = null;
	public JDatePickerImpl datePickerBis = null;
	public JDatePickerImpl datePickerCurrent = null;
	
	private YDjxtable tableBuchungen = null;



	public BuchungPanel(ResourceBundle bundle, JFrame parent)
	{
		this.parentFrame = parent;
		this.globalBundle = bundle;
		Locale de_DE = new Locale("de", "DE");
		localBundle = ResourceBundle.getBundle("client.bundles.buchungPanel", de_DE); 
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
		
		scrollPaneBuchungTable = new JScrollPane();
		scrollPaneBuchungTable.setViewportView(getTableBuchungen());
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
			final boolean leave = notifyForLeaving(buchungPanelFrame, globalBundle,globalBundle.getString("LeavingMessage"));
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
	        int returncode = -1;
			
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
			
			Buchung tempBuchung = new Buchung(getUserObject().getUserid(), konto, buchungType, betragString, datumString, uhrzeitString);
			
			try {
				returncode = ServerSystemManager.getDatenbankManager().insertBuchung(tempBuchung);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				returncode = -1;
			}
			
			if (returncode == 1)
			{
				statusbar.setText("Speichern successful! ");
			}
			else
			{
				statusbar.setText("Speichern failed!!!");
			}
			
		}
	};

	public ActionListener RefreshAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			String von, bis;
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
			
			Date Datumvon = model1.getValue();
			Date Datumbis = model2.getValue();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			von = formatter.format(Datumvon);
			bis = formatter.format(Datumbis);
			userid = getUserObject().getUserid();
			
			try {
				buchungTableData = getDatenbankManager().getBuchungen(userid, von, bis);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			
			System.out.println(buchungTableData.toString());
			System.out.println("von: " + von + "bis:" + bis );
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
	
	public YDjxtable getTableBuchungen()
	{
		if (tableBuchungen == null)
		{
			tableBuchungen = new YDjxtable();
			tableBuchungen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			final BuchungTableModel table = new BuchungTableModel();
			table.setData(new ArrayList<Buchung>());
			tableBuchungen.setModel(table);
		}
		
		return tableBuchungen;
	}


}
