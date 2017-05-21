package views;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1928365160419055488L;
	
	
	MyTableModel(Vector<Object> data, Vector<String> columnNames){
		super(data, columnNames);
	}  
	 
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 2) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
    	return getValueAt(0, columnIndex).getClass();
    }   
              
}  
