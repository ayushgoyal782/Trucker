package com.example.egen.egen.service;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.egen.egen.entity.Alerts;
import com.example.egen.egen.entity.Readings;
import com.example.egen.egen.entity.Tires;
import com.example.egen.egen.entity.Vehicle;
import com.example.egen.egen.repository.AlertsDao;
import com.example.egen.egen.repository.VehicleDao;
import static org.springframework.data.convert.ConverterBuilder.reading;
import org.springframework.scheduling.annotation.Async;

@Service
public class AlertsService {
	
	@Autowired
	private AlertsDao alertsDao;
	Alerts alerts = new Alerts();
	
	@Autowired
	private VehicleDao vehicleDao;
	
	@Async
	public void insertAlerts(Readings readings) {
    System.out.println("checking");
        if (readings.isCheckEngineLightOn()) {
            System.out.println(" fwewfennfwno");
        }
        boolean check = false;
        boolean low = false;
        boolean high = false;
        boolean medium = false;
        Tires tire = readings.getTires();
        if (tire.getFrontLeft() < 32 || tire.getFrontLeft() > 36
                || tire.getFrontRight() < 32 || tire.getFrontRight() > 36
                || tire.getRearLeft() < 32 || tire.getRearLeft() > 36
                || tire.getRearRight() < 32 || tire.getRearRight() > 36
                || readings.isCheckEngineLightOn() || readings.isEngineCoolantLow()) {

            check = true;
            low = true;

        }

        Optional<Vehicle> vehicle = vehicleDao.findById(readings.getVin());
        if (vehicle.isPresent()) {
            if (readings.getEngineRpm() > vehicle.get().getRedlineRpm()) {
                check = true;
                high = true;
            }

            if (readings.getFuelVolume() < (.1 * vehicle.get().getMaxFuelVolume())) {
                check = true;
                medium = true;

            }
        }

        if (check) {
            Alerts alert = new Alerts();
            alert.setVin(readings.getVin());
            alert.setHigh(high);
            alert.setLow(low);
            alert.setMedium(medium);
            alert.setTimestamp(readings.getTimestamp());
            alertsDao.save(alert);

        }

    }
	
	public List<Alerts> fetchHighAlerts(){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.setTime(new Date());

		calendar.add(Calendar.HOUR, -2);//substract the number of days to look back
		Date dateToLookBackAfter = (Date) calendar.getTime();
		System.out.println(dateToLookBackAfter);
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sm.format(dateToLookBackAfter);
		System.out.println(time);
		Date ds = null;
		try {
			ds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		List<Alerts> alerts = new ArrayList<Alerts>();
		alerts = alertsDao.fetchHighAlerts(ds);
		return alerts;
	}
	
	public List<Alerts> fetchAlertsByVehicle(String vin){
		List<Alerts> alerts = new ArrayList<Alerts>();
		alerts = alertsDao.fetchAlertsByVehicle(vin);
		return alerts;
	}
}
