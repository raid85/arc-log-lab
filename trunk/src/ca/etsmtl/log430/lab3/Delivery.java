package ca.etsmtl.log430.lab3;

import java.sql.Time ;

/** This class defines the Delivery object for the system.
 * 
 * @author A.J. Lattanze, CMU
 * @version 1.6, 2012-Jun-19
 */

/*
 * Modification Log **********************************************************
 *
 * v1.6, R. Champagne, 2012-Jun-19 - Various refactorings for new lab.
 * 
 * v1.5, R. Champagne, 2012-May-31 - Various refactorings for new lab.
 * 
 * v1.4, R. Champagne, 2012-Feb-02 - Various refactorings for new lab.
 * 
 * v1.3, R. Champagne, 2011-Feb-02 - Various refactorings, conversion of
 * comments to javadoc format.
 * 
 * v1.2, R. Champagne, 2002-May-21 - Adapted for use at ETS.
 * 
 * v1.1, G.A.Lewis, 01/25/2001 - Bug in second constructor. Removed null
 * assignment to deliveryID after being assigned a value.
 * 
 * v1.0, A.J. Lattanze, 12/29/99 - Original version.
 * ***************************************************************************
 */

public class Delivery {

	/**
	 * Delivery ID
	 */
	private String deliveryID;

	/**
	 * Delivery address
	 */
	private String address;

	/**
	 * Time at which the delivery should be planned
	 */
	private String desiredDeliveryTime;

	/**
	 * Estimated duration of delivery
	 */
	private String estimatedDeliveryDuration;

	/**
	 * List of drivers assigned to the delivery
	 */
	private DriverList driversAssigned = new DriverList();

	private boolean notAssigned;

	public Delivery() {
		this(null);
	}

	public Delivery(String deliveryID) {
		this(deliveryID, null);
	}

	public Delivery(String deliveryID, String estimatedDuration) {
		notAssigned = true;
		this.setDeliveryID(deliveryID);
		this.setDesiredDeliveryTime(null);
		this.setEstimatedDeliveryDuration(estimatedDuration);
	}

	/**
	 * Assign a driver to a delivery.
	 * 
	 * @param driver
	 */
	public void assignDriver(Driver driver) {
		driversAssigned.addDriver(driver);
		notAssigned = false;
	}

	public void setDeliveryID(String deliveryID) {
		this.deliveryID = deliveryID;
	}

	public String getDeliveryID() {
		return deliveryID;
	}

	public void setDesiredDeliveryTime(String time) {
		this.desiredDeliveryTime = time;
	}

	public String getDesiredDeliveryTime() {
		return desiredDeliveryTime;
	}

	//Accesseur pour avoir l'heure de d�part du livreur en base 100	
	public float getEstimatedDepartureTime100() {return timeHelper(desiredDeliveryTime,estimatedDeliveryDuration);}
	//Accesseur pour avoir l'heure d'arriv�e du livreur en base 100	
	public float getDesiredDeliveryTime100() {return timeHelper(desiredDeliveryTime,null); }
	//M�thode qui converti le temps de base 60 en base 100, et fait les soustractions n�c�ssaires pour obtenir l'heure de d�part en base 100
	public float timeHelper (String time1,String time2 ){

		float hourddt ;
		float minutesddt ;
		float houredd ;
		float minutesedd ;
		float deptime = -1 ;	

		hourddt = Integer.valueOf(String.valueOf(time1.charAt(0))+String.valueOf(time1.charAt(1)));
		minutesddt = Integer.valueOf(String.valueOf(time1.charAt(2))+String.valueOf(time1.charAt(3)));		
		minutesddt = (minutesddt*100)/60 ;
		if(time2 == null){deptime = hourddt + (minutesddt/100) ;}
		else {
			houredd = Integer.valueOf(String.valueOf(time2.charAt(0))+String.valueOf(time2.charAt(1)));
			minutesedd = Integer.valueOf(String.valueOf(time2.charAt(2))+String.valueOf(time2.charAt(3)));
			minutesedd = (minutesedd*100)/60 ;

			float deptimeH=hourddt-houredd ;
			float deptimeM =((minutesddt-minutesedd))/100 ;

			deptime = deptimeH+deptimeM ;}

		return deptime ; }

	public void setEstimatedDeliveryDuration(String duration) {
		this.estimatedDeliveryDuration = duration;
	}

	public String getEstimatedDeliveryDuration() {
		return estimatedDeliveryDuration;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public DriverList getDriversAssigned() {
		return driversAssigned;
	}

	public void setDriversAssigned(DriverList driversAssigned) {
		this.driversAssigned = driversAssigned;
	}

	public boolean getNotAssigned(){
		return notAssigned;
	}

} // Delivery class