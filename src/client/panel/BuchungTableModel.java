package client.panel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import base.Buchung;

public class BuchungTableModel extends AbstractTableModel{

	private String[] columnNames = new String[] { "Datum", "Zeit", "Katalog", "Konto", "Betrag" };

	private static final long serialVersionUID = 1L;

	private List<Buchung> data = new ArrayList<Buchung>();
	
	private NumberFormat percentFormat = NumberFormat.getNumberInstance();
	

	public BuchungTableModel() {
		super();
		percentFormat.setMinimumFractionDigits(2);
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data != null ? data.size() : 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return data.get(rowIndex).getDatum();
			case 1:
				return data.get(rowIndex).getUhrzeit();
			case 2:
				return TemplatePanel.BuchungtypenIntToString.get(data.get(rowIndex).getBuchungsType());
			case 3:
				return TemplatePanel.KontonameIntToString.get(data.get(rowIndex).getKonto());
			case 4: 
				int temp = Integer.parseInt(data.get(rowIndex).getBetrag());
				temp = temp/100;
				return percentFormat.format(temp);
		}
		return null;
	}

	public void setData(List<Buchung> data) {
		this.data = data;
		fireTableDataChanged();
	}

	public Buchung getDataAt(int rowIndex) {
		return rowIndex > -1 ? data.get(rowIndex) : null;
	}


	public List<Buchung> getData() {
		return data;
	}

}
