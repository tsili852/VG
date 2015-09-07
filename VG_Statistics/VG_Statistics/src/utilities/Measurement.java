package utilities;

import java.util.Date;

public class Measurement {
	private String windTurbineID;
	private String mesDate;
	private double mesHumidity;
	private double mesTemperature;
	private double mesPressure;
	
	public String getWindTurbineID() {
		return windTurbineID;
	}
	public void setWindTurbineID(String windTurbineID) {
		this.windTurbineID = windTurbineID;
	}
	
	public String getMesDate() {
		return mesDate;
	}
	public void setMesDate(String mesDate) {
		this.mesDate = mesDate;
	}
	
	public double getMesHumidity() {
		return mesHumidity;
	}
	public void setMesHumidity(double mesHumidity) {
		this.mesHumidity = mesHumidity;
	}
	
	public double getMesTemperature() {
		return mesTemperature;
	}
	public void setMesTemperature(double mesTemperature) {
		this.mesTemperature = mesTemperature;
	}
	
	public double getMesPressure() {
		return mesPressure;
	}
	public void setMesPressure(double mesPressure) {
		this.mesPressure = mesPressure;
	}
	
	
	public void clearAllValues() {
		setWindTurbineID("");
		setMesDate(null);
		setMesHumidity(0.0);
		setMesTemperature(0.0);
		setMesPressure(0.0);
	}

}
