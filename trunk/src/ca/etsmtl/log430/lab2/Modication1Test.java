package ca.etsmtl.log430.lab2;

import static org.junit.Assert.*;
import org.junit.Test;

public class Modication1Test {
	//private DeliveryReader myCourseList = new DeliveryReader("deliveries.txt");
	private DriverReader myTeacherList = new DriverReader("drivers.txt");
	private Driver driver = myTeacherList.getListOfDrivers().findTeacherByID("DRV001");
	private Delivery delivery;
	
	
	@Test
	public void valide() {
		delivery = driver.getDeliveriesMadeList().getNextDelivery();
		assertTrue(delivery.getDeliveryID().equalsIgnoreCase("D001"));
		delivery = driver.getDeliveriesMadeList().getNextDelivery();
		assertTrue(delivery.getDeliveryID().equalsIgnoreCase("D023"));
		delivery = driver.getDeliveriesMadeList().getNextDelivery();
		assertTrue(delivery.getDeliveryID().equalsIgnoreCase("D067"));		
	}
	
	@Test
	public void nullTest() {
		for(int i = 0; i < 4; i++)	
			delivery = driver.getDeliveriesMadeList().getNextDelivery();	
		assertTrue(delivery == null);		
	}

}
