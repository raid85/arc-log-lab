package ca.etsmtl.log430.lab3;

import java.util.Observable;

/**
 * Assigns drivers to deliveries.
 *  
 * @author A.J. Lattanze, CMU
 * @version 1.4, 2012-Jun-19
 */

/*
 * Modification Log **********************************************************
 * v1.4, R. Champagne, 2012-Jun-19 - Various refactorings for new lab.
 * 
 * v1.3, R. Champagne, 2012-Feb-14 - Various refactorings for new lab.
 * 
 * v1.2, R. Champagne, 2011-Feb-24 - Various refactorings, conversion of
 * comments to javadoc format.
 * 
 * v1.1, R. Champagne, 2002-Jun-19 - Adapted for use at ETS.
 * 
 * v1.0, A.J. Lattanze, 12/29/99 - Original version.
 * ***************************************************************************
 */
public class AssignDriverToDelivery extends Communication
{
	Driver myDriver = new Driver();
	Delivery myDelivery = new Delivery();
	static final float MAX_HOURS_SENIORS = 8 ;
	static final float MAX_HOURS_JUNIORS = 10 ;


	public AssignDriverToDelivery(Integer registrationNumber, String componentName) {
		super(registrationNumber, componentName);
	}

	/**
	 * The update() method is an abstract method that is called whenever the
	 * notifyObservers() method is called by the Observable class. First we
	 * check to see if the NotificationNumber is equal to this thread's
	 * RegistrationNumber. If it is, then we execute.
	 * 
	 * @see ca.etsmtl.log430.lab3.Communication#update(java.util.Observable,
	 *      java.lang.Object)
	 */
	public void update(Observable thing, Object notificationNumber) {
		Menus menu = new Menus();


		if (registrationNumber.compareTo((Integer)notificationNumber) == 0) {
			addToReceiverList("ListDriversComponent");
			addToReceiverList("ListDeliveriesComponent");

			// Display the drivers and prompt the user to pick a driver

			signalReceivers("ListDriversComponent");

			myDriver = menu.pickDriver(CommonData.theListOfDrivers.getListOfDrivers());

			if (myDriver != null) {
				/*
				 * Display the deliveries that are available and ask the user to
				 * pick a delivery to register for
				 */
				signalReceivers("ListDeliveriesComponent");

				myDelivery = menu.pickDelivery(CommonData.theListOfDeliveries.getListOfDeliveries());

				if (myDelivery != null)	{	
					/*
					 * If the selected delivery and driver exist, then complete
					 * the assignment process. */

					if(myDelivery.getNotAssigned()){

						if(!checkConflicts()){
							if(!checkMaxHours()){
								myDelivery.assignDriver(myDriver);
								myDriver.assignDelivery(myDelivery);
							}else{System.out.println("-----------------------------------------------------------------" +
									"\n"+"******** Erreur : le chauffeur  "+myDriver.getFirstName()+" "+myDriver.getLastName()+"********\n"+
							"******** � atteint son nombre d'heures maximum permis *****");}

						}else{
							System.out.println("-----------------------------------------------------------------" +
									"\n"+"******** Erreur : Conflit d'horraire ********\n"+
							"-----------------------------------------------------------------");}
					}else{						
						System.out.println("-----------------------------------------------------------------" +
								"\n"+"******** Erreur : Cette livraison est d�j� assign�e ********\n"+
						"-----------------------------------------------------------------");}
				} else {
					System.out.println("\n\n *** Delivery not found ***");
				} 
			} else {
				System.out.println("\n\n *** Driver not found ***");
			}
		}
	}

	private boolean checkConflicts() {

		boolean conflict = false;
		float chosenDeliveryDepartureTime = myDelivery.getEstimatedDepartureTime100();
		float chosenDeliveryArrivalTime = myDelivery.getDesiredDeliveryTime100();
		float padDeliveryDepartureTime =0 ;
		float padDeliveryArrivalTime = 0;

		myDriver.getDeliveriesAssigned().goToFrontOfList();
		if(myDriver.getDeliveriesAssigned().getListSize()!=0){
			for (int i=0; i<myDriver.getDeliveriesAssigned().getListSize();i++){

				padDeliveryDepartureTime = myDriver.getDeliveriesAssigned().getDelivery().getEstimatedDepartureTime100() ;
				padDeliveryArrivalTime = myDriver.getDeliveriesAssigned().getDelivery().getDesiredDeliveryTime100() ;
				if(( padDeliveryDepartureTime < chosenDeliveryDepartureTime  && chosenDeliveryDepartureTime  < padDeliveryArrivalTime) || 
						(padDeliveryDepartureTime < chosenDeliveryArrivalTime  && chosenDeliveryArrivalTime  < padDeliveryArrivalTime) || 
						(chosenDeliveryArrivalTime > padDeliveryArrivalTime && chosenDeliveryDepartureTime < padDeliveryDepartureTime )
				){conflict = true ;}
				myDriver.getDeliveriesAssigned().pointNext();

			}		
		}

		return conflict;
	}

	private boolean checkMaxHours() {

		boolean maxHoursReached = false ;
		float driverTotalHours = 0 ;
		String driverType = myDriver.getType() ;
		
		myDriver.getDeliveriesAssigned().goToFrontOfList();
		if(myDriver.getDeliveriesAssigned().getListSize()!=0){
			for(int i=0 ; i<myDriver.getDeliveriesAssigned().getListSize();i++){
				
				driverTotalHours = driverTotalHours + (myDriver.getDeliveriesAssigned().getDelivery().getDeliveryDurationTime100());
						
				myDriver.getDeliveriesAssigned().pointNext();
			}
		}
		
	
		if((driverType.compareTo("SNR")==0) && (driverTotalHours + myDelivery.getDeliveryDurationTime100()) > MAX_HOURS_SENIORS){maxHoursReached = true ;}
		else if ((driverType.compareTo("JNR")==0) && (driverTotalHours + myDelivery.getDeliveryDurationTime100()) > MAX_HOURS_JUNIORS){maxHoursReached = true;}			
	

		return maxHoursReached; 
	}






}
