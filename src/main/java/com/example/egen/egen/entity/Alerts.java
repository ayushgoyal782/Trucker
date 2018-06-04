package com.example.egen.egen.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "Alerts.fetchHighAlerts", query = "SELECT a FROM Alerts a WHERE a.high = '1' AND a.timestamp > :input"),
	@NamedQuery(name = "Alerts.fetchAlertsByVehicle", query = "SELECT a FROM Alerts a WHERE a.vin = :input")
})
public class Alerts {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int id;
	private String vin;
	private boolean high;
	private boolean low;
	private boolean medium;
	private Date timestamp;
	
	
	public Alerts()
	{
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isHigh() {
		return high;
	}
	public void setHigh(boolean high) {
		this.high = high;
	}
	public boolean isLow() {
		return low;
	}
	public void setLow(boolean low) {
		this.low = low;
	}
	public boolean isMedium() {
		return medium;
	}
	public void setMedium(boolean medium) {
		this.medium = medium;
	}
	@Override
	public String toString() {
		return "Alerts [id=" + id + ", vin=" + vin + ", high=" + high + ", low=" + low + ", medium=" + medium
				+ ", timestamp=" + timestamp + "]";
	}
	
	
	
	
}