package utilities;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class MyTools {
	
    public static TableModel resultSetToTableModel(ResultSet rs) {
	try {
	    ResultSetMetaData metaData = rs.getMetaData();
	    int numberOfColumns = metaData.getColumnCount();
	    Vector<String> columnNames = new Vector<String>();
	    columnNames.add("Wind Turbine ID");
	    columnNames.add("Blade");
	    columnNames.add("VG ID");
	    columnNames.add("Date and Time");
	    columnNames.add("Humidity");
	    columnNames.add("Temperature");
	    columnNames.add("Pressure");

	    // Get all rows.
	    Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

	    while (rs.next()) {
		Vector<Object> newRow = new Vector<Object>();

		for (int i = 1; i <= numberOfColumns; i++) {
		    newRow.addElement(rs.getObject(i));
//		    if (Integer.parseInt(rs.getString("Hum")) > 20 ) {
//			
//			}
		}

		rows.addElement(newRow);
	    }

	    return new DefaultTableModel(rows, columnNames);
	} catch (Exception e) {
	    e.printStackTrace();

	    return null;
	}
    }

}
