package client.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import base.Buchung;
import base.BuchungType;
import base.IconStore;
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
	protected Buchung selectedBuchung = null;

	public JPanel buchungPanel = null;
	public JPanel buchungInnerPanel = null;
	public JPanel vonPanel = null;
	public JPanel bisPanel = null;
	public JPanel currentPanel = null;

	public JLabel statusbar = null;
	public JComboBox<String> comboboxBuchungtypen;
	public JComboBox<String> comboboxKonto;

	public JButton   buttonBuchungAnlegen;
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
		textfieldBetrag.setValue(0);
		
		Format timeFormat = new SimpleDateFormat("HH:mm");
		textFieldUhrzeit = new JFormattedTextField(timeFormat);

		scrollPaneBuchungTable = new JScrollPane();
		scrollPaneBuchungTable.setViewportView(getTableBuchungen());
		buchungPanelFrame = (JFrame)cookSwing.render("src/client/panel/ext/buchungPanel.xml");
		buchungPanelFrame.setVisible(true);
		buchungPanelFrame.setFocusable(true);
		textfieldBetrag.getDocument().addDocumentListener(textFieldChangeListener);
		textFieldUhrzeit.getDocument().addDocumentListener(textFieldChangeListener);
		initComboboxBuchungstypen();
		initComboboxKonto();
		initDatePicker();
		initTimeField();
		buttonBuchungSpeichern.setEnabled(false);
		comboboxBuchungtypen.setEnabled(false);
		comboboxKonto.setEnabled(false);


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
	
	public DocumentListener textFieldChangeListener = new DocumentListener() {
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
			buttonBuchungSpeichern.setEnabled(true);
		}
	};

	public ActionListener AbrechenAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			if(selectedBuchung == null)
			{
				textfieldBetrag.setText("");
				Calendar cal = Calendar.getInstance();
				int bisDay = cal.get(Calendar.DAY_OF_MONTH);
				int bisMonth = cal.get(Calendar.MONTH);
				int bisYear = cal.get(Calendar.YEAR);
				model3.setDate(bisYear, bisMonth, bisDay);
				model3.setSelected(true);
				initTimeField();
				textfieldBetrag.setEnabled(false);
				textFieldUhrzeit.setEnabled(false);
				comboboxBuchungtypen.setEnabled(false);
				comboboxKonto.setEnabled(false);
			}
			else
			{
				ReloadBuchung();
			}
			buttonBuchungSpeichern.setEnabled(false);
		}
	};

	public ActionListener AnlegenAction = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			selectedBuchung = null;
			textfieldBetrag.setValue(0);
			Calendar cal = Calendar.getInstance();
			int bisDay = cal.get(Calendar.DAY_OF_MONTH);
			int bisMonth = cal.get(Calendar.MONTH);
			int bisYear = cal.get(Calendar.YEAR);
			model3.setDate(bisYear, bisMonth, bisDay);
			model3.setSelected(true);
			initTimeField();
			textfieldBetrag.setEnabled(true);
			textFieldUhrzeit.setEnabled(true);
			comboboxBuchungtypen.setEnabled(true);
			comboboxKonto.setEnabled(true);

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
			int buchungsNumber = -1;
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

			if (selectedBuchung == null)
			{
				buchungsNumber = -1;

				Buchung tempBuchung = new Buchung(buchungsNumber, getUserObject().getUserid(), konto, buchungType, betragString, datumString, uhrzeitString);

				try {
					returncode = ServerSystemManager.getDatenbankManager().insertBuchung(tempBuchung);
				} catch (RemoteException e1) {
					e1.printStackTrace();
					returncode = -1;
				}
			}
			else
			{
				buchungsNumber = selectedBuchung.getBuchungnumber();
				Buchung tempBuchung = new Buchung(buchungsNumber, getUserObject().getUserid(), konto, buchungType, betragString, datumString, uhrzeitString);
				try {
					returncode = ServerSystemManager.getDatenbankManager().updateBuchung(tempBuchung);
				} catch (RemoteException e1) {
					e1.printStackTrace();
					returncode = -1;
				}
			}
			
			Date Datumvon = model1.getValue();
			Date Datumbis = model2.getValue();
			getTableContent(Datumvon, Datumbis);

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
			Date Datumvon = model1.getValue();
			Date Datumbis = model2.getValue();
			getTableContent(Datumvon, Datumbis);

		}
	};

	private final ListSelectionListener BuchungSelected = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting())
			{
				ReloadBuchung();
				buttonBuchungSpeichern.setEnabled(false);
				textfieldBetrag.setEnabled(true);
				textFieldUhrzeit.setEnabled(true);
				comboboxBuchungtypen.setEnabled(true);
				comboboxKonto.setEnabled(true);
			}
		}
	};
	
	public ItemListener ComboboxChanged = new ItemListener() {
		
		public void itemStateChanged(ItemEvent e) {
			buttonBuchungSpeichern.setEnabled(true);
		}
	};

	private void ReloadBuchung()
	{
		try
		{
			selectedBuchung = ((BuchungTableModel)getTableBuchungen().getModel()).getDataAt(getTableBuchungen().convertRowIndexToModel(getTableBuchungen().getSelectedRow()));
			textfieldBetrag.setValue((Integer.parseInt(selectedBuchung.getBetrag())/100));

			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date tempDate;
				tempDate = formatter.parse(selectedBuchung.getDatum()+" "+selectedBuchung.getUhrzeit());
				textFieldUhrzeit.setValue(tempDate);
				model3.setValue(tempDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			comboboxBuchungtypen.setSelectedItem(BuchungtypenIntToString.get(selectedBuchung.getBuchungsType()));
			comboboxKonto.setSelectedItem(KontonameIntToString.get(selectedBuchung.getKonto()));
		}
		catch (Exception e) {
		}
	}

	private void getTableContent(Date von, Date bis)
	{

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		userid = getUserObject().getUserid();

		try {
			buchungTableData = getDatenbankManager().getBuchungen(userid, formatter.format(von), formatter.format(bis));
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		((BuchungTableModel)(tableBuchungen.getModel())).setData(buchungTableData);
	}



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
		model1.setSelected(true);
		model2.setSelected(true);
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
			table.setData(new Vector<Buchung>());
			tableBuchungen.setModel(table);
			tableBuchungen.getSelectionModel().addListSelectionListener(BuchungSelected);

			//Table Initialisieren 
			Calendar cal = Calendar.getInstance();
			Date bis = cal.getTime();
			cal.add(Calendar.DATE, -30);
			Date von = cal.getTime();
			getTableContent(von, bis);
		}

		return tableBuchungen;
	}


}
