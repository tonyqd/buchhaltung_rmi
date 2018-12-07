package client.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import base.Buchung;

public class BuchungTableModel extends AbstractTableModel{

	private String[] columnNames = new String[] { "Date", "Time", "tmp" };

	private static final long serialVersionUID = 1L;

	private List<Buchung> data = new ArrayList<Buchung>();

	public BuchungTableModel() {
		super();
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
//
//	public Object getValueAt(int rowIndex, int columnIndex) {
//		switch (columnIndex) {
//			case 0:
//				return data.get(rowIndex).getSortIndex();
//			case 1:
//				if (data.get(rowIndex).getAbku().equals(getFreeAbkuText(data.get(rowIndex).getTextNummer()))) {
//					return "FREI";
//				}
//				return data.get(rowIndex).getAbku();
//			case 2:
//				return data.get(rowIndex).getTextNummer();
//		}
//		return null;
//	}
//
	public void setData(List<Buchung> data) {
		this.data = data;
		fireTableDataChanged();
	}
//
//	public TextListBaustein getDataAt(int rowIndex) {
//		return rowIndex > -1 ? data.get(rowIndex) : null;
//	}
//

//	
//	
//	private String getFreeAbkuText(int textNr){
//		String text = "" + textNr;
//		while (text.length() < 6) {
//			text = "0" + text;
//		}
//		return "##" + text;
//	}
//
	public List<Buchung> getData() {
		return data;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
