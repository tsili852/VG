package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;


public class Uploader {

	private File selectedFile;
	private String fileName;
	
	public Uploader(File sFile){
		selectedFile = sFile;
		fileName = sFile.getName().replaceFirst("[.][^.]+$", "");
	}
	
	public int uploadToSQLLite() throws SQLException{
		int mesCounter = 0;
		
//		SimpleDateFormat sdft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Measurement newMeas = new Measurement();
		
		@SuppressWarnings("resource")
		SqlConnector propConnector = new SqlConnector("VG_db");
		propConnector.connectToDatabase();
		
		try {
			BufferedReader br =  new BufferedReader(new FileReader(selectedFile));
			String line = null;
			
			while ((line = br.readLine()) != null) {
				newMeas.clearAllValues();
				
				String[] values = line.split(";");
				
				newMeas.setBlade(Integer.parseInt(fileName));
				newMeas.setVGID(Integer.parseInt(values[0]));
				newMeas.setMesDate(values[1]);
				newMeas.setMesTemperature(Double.parseDouble(values[2]));
				newMeas.setMesHumidity(Double.parseDouble(values[3]));
				newMeas.setMesForce(Double.parseDouble(values[4]));
				
				String sqlStatement = "Insert Into Measurements (Blade, VGID, DateTime, Hum, Temp, Force) Values "
						+ "('" + newMeas.getBlade() + "', '" + newMeas.getVGID()+ "', '" + newMeas.getMesDate() + "', " + newMeas.getMesHumidity() + ", " + newMeas.getMesTemperature()
						+ ", " + newMeas.getMesForce() + ")";
				propConnector.executeUpdateQuery(sqlStatement);
				mesCounter++;
				
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
