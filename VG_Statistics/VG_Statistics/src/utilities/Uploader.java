package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Uploader {

	private File selectedFile;
	
	public Uploader(File sFile){
		selectedFile = sFile;
	}
	
	public int uploadToSQLLite(){
		int mesCounter = 0;
		
//		SimpleDateFormat sdft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Measurement newMeas = new Measurement();
		
		SqlConnector propConnector = new SqlConnector("VG_db");
		propConnector.connectToDatabase();
		
		try {
			BufferedReader br =  new BufferedReader(new FileReader(selectedFile));
			String line = null;
			
			while ((line = br.readLine()) != null) {
				newMeas.clearAllValues();
				
				String[] values = line.split(";");
				
				newMeas.setWindTurbineID(values[0]);
				newMeas.setMesDate(values[1]);
				newMeas.setMesHumidity(Double.parseDouble(values[2]));
				newMeas.setMesTemperature(Double.parseDouble(values[3]));
				newMeas.setMesPressure(Double.parseDouble(values[4]));
				
				String sqlStatement = "Insert Into Measurements (WTID, DateTime, Hum, Temp, Pres) Values "
						+ "('" + newMeas.getWindTurbineID() + "', '" + newMeas.getMesDate() + "', " + newMeas.getMesHumidity() + ", " + newMeas.getMesTemperature()
						+ ", " + newMeas.getMesPressure() + ")";
				propConnector.executeUpdateQuery(sqlStatement);
				mesCounter++;
								
//				String TurbineID = values[0];
//				Date mDate = sdft.parse(values[1]);
//				double mHumidity = Double.parseDouble(values[2]);
//				double mTemperature = Double.parseDouble(values[3]);
//				double mPressure = Double.parseDouble(values[4]);
				
//				System.out.printf("ID: %s, Date: %s, Temperature: %f, Humidity: %f, Pressure: %f", TurbineID, mDate, mHumidity, mTemperature, mPressure);
				
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	
	
		if(propConnector.isConnectionOpen()) {
			propConnector.closeConnection();
		}
		
		return mesCounter;
	}
	
}
