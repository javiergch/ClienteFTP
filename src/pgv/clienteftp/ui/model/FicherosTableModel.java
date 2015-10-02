package pgv.clienteftp.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.net.ftp.FTPFile;


@SuppressWarnings("serial")
public class FicherosTableModel extends AbstractTableModel {
	
	private List<FTPFile> ficheros = new ArrayList<FTPFile>();		//lista que contendrá los ficheros
	
	private static final String[] COLUMN_NAMES = { "Nombre", "Tamaño", "Tipo" };
	private static final Class<?>[] COLUMN_CLASSES = { String.class, Long.class, Integer.class };
	
	
	public void setFicheros(FTPFile[] ficherosArray) { //cargar datos en la tabla
		ficheros.clear();
		
		FTPFile otro = new FTPFile();
		otro.setName(".");
		otro.setSize(0);
		otro.setType(1);
		ficheros.add(otro);
		
		FTPFile atras = new FTPFile();
		atras.setName("..");
		atras.setSize(0);
		atras.setType(1);
		ficheros.add(atras);
		
		
		for (int i = 0; i < ficherosArray.length; i++) {
			ficheros.add(ficherosArray[i]);
		}
		
		fireTableDataChanged();
	}
	
	public FTPFile getFichero(int rowIndex) {
		FTPFile fichero = ficheros.get(rowIndex);
		return fichero;
	}

	@Override
	public int getColumnCount() {
		//devuelve cantidad de columnas de la tabla
		return COLUMN_NAMES.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		//devuelve los nombres de las columnas
		return COLUMN_NAMES[columnIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {	
		return COLUMN_CLASSES[columnIndex];
	}
	
	@Override
	public int getRowCount() {
		return ficheros.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;	
		FTPFile fichero = ficheros.get(rowIndex);
		
		switch (columnIndex) {
		case 0:	value=fichero.getName(); break;
		case 1:	value=fichero.getSize(); break;
		case 2:	if(fichero.getType() == 0)
					value = "ARCHIVO";
				else
					value = "DIRECTORIO";
				break;
		}
		
		return value;
	}

}
