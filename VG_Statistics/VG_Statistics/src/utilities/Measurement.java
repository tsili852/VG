package utilities;

import java.util.Date;

public class Measurement {
	private int blade;
	private int vgID;
	private String mesDate;
	private double mesHumidity;
	private double mesTemperature;
	private double mesPressure;
	
	public int getBlade(){
		return blade;
	}
	public void setBlade(int blade){
		this.blade = blade;
	}
	
	public int getVGID(){
		return vgID;
	}
	public void setVGID(int vgID){
		this.vgID = vgID;
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
		setBlade(0);
		setVGID(0);
		setMesDate(null);
		setMesHumidity(0.0);
		setMesTemperature(0.0);
		setMesPressure(0.0);
	}

}
