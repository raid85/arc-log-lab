package ca.etsmtl.log430.lab2;

/**
 * This class defines the Delivery object for the system.
 * 
 * @author A.J. Lattanze, CMU
 * @version 1.5, 2012-May-31
 */

/*
 * Modification Log **********************************************************
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
	 * Start time of the course
	 */
	private String estimatedDeliveryDuration;

	/**
	 * List of teachers assigned to the course
	 */
	private DriverList driversAssigned = new DriverList();
	private boolean assigned;

	public Delivery() {
		this(null);
	}

	public Delivery(String deliveryID) {
		this(deliveryID, null);
	}

	public Delivery(String deliveryID, String estimatedDuration) {
		this.setAssigned(false);
		this.setDeliveryID(deliveryID);
		this.setDesiredDeliveryTime(null);
		this.setEstimatedDeliveryDuration(estimatedDuration);
	}

	/**
	 * Assign a teacher to a class.
	 * 
	 * @param driver
	 */
	public void assignDriver(Driver driver) {
		if (!this.assigned) {

			driver.getDeliveriesAssigned().goToFrontOfList();
			Delivery driverDelivery;
			boolean done;
			boolean conflict = false;
			done = false;
			while (!done) {
				driverDelivery = driver.getDeliveriesAssigned()
						.getNextDelivery();
				if (driverDelivery == null) {
					if (!conflict) {
						if ((driver.getTotalDeliveryTime()+this.getEstimatedDeliveryDuration("minute")<=720 && driver.getType().equals("SNR")) ||  ((driver.getTotalDeliveryTime()+this.getEstimatedDeliveryDuration("minute")<=480 && driver.getType().equals("JNR")))) {
							
							driversAssigned.addDriver(driver);
							
						}
						else{
							System.out.println("\n\n *** Maximum DeliveryTime has been reached ***");
						}
						
					}
					else{
						System.out.println("\n\n *** Schedule Conflict ***");
					}
					done = true;
				} else {
					// if to validate the schedule conflict
					if (driverDelivery.getDesiredDeliveryTime("minute")
							+ driverDelivery
									.getEstimatedDeliveryDuration("minute") <= this
							.getDesiredDeliveryTime("minute")
							|| driverDelivery.getDesiredDeliveryTime("minute") >= this
									.getDesiredDeliveryTime("minute")
									+ this.getEstimatedDeliveryDuration("minute")) {

					} else {
						conflict = true;
					}
				}
			} // if

		} else {
			System.out
					.println("\n\n *** There is already a driver assigned to this delivery ***");
		}
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
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

	// Override getDesiredDeliveryTime to get the time in minutes
	public int getDesiredDeliveryTime(String minute) {
		// System.out.println(desiredDeliveryTime.substring(2));
		int desiredDeliveryTimeInMinutes = Integer.parseInt(desiredDeliveryTime
				.substring(0, 2))
				* 60
				+ Integer.parseInt(desiredDeliveryTime.substring(2));
		return desiredDeliveryTimeInMinutes;
	}

	public void setEstimatedDeliveryDuration(String duration) {
		this.estimatedDeliveryDuration = duration;
	}

	public String getEstimatedDeliveryDuration() {
		return estimatedDeliveryDuration;
	}

	// Override getEstimatedDeliveryDuration to get the time in minutes
	public int getEstimatedDeliveryDuration(String minute) {
		int estimatedDeliveryDurationInMinutes = Integer
				.parseInt(estimatedDeliveryDuration.substring(0, 2))
				* 60
				+ Integer.parseInt(estimatedDeliveryDuration.substring(2));
		return estimatedDeliveryDurationInMinutes;
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

	public boolean getAssigned() {
		return assigned;
	}

} // Delivery class