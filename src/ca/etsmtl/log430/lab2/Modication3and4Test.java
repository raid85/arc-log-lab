package ca.etsmtl.log430.lab2;

import static org.junit.Assert.*;
import org.junit.Test;

public class Modication3and4Test {
	private DeliveryReader myCourseList = new DeliveryReader("deliveries.txt");
	private DriverReader myTeacherList = new DriverReader("drivers.txt");
	private Driver driver = myTeacherList.getListOfDrivers().findTeacherByID("DRV001");
	private Driver driver2 = myTeacherList.getListOfDrivers().findTeacherByID("DRV002");
	private Delivery delivery  = myCourseList.getListOfDeliveries().findDelivery("D001");
	private Delivery delivery1 = myCourseList.getListOfDeliveries().findDelivery("D002");
	private Delivery delivery2 = myCourseList.getListOfDeliveries().findDelivery("D122");
	
	//Verification si 1 livraison ne peut pas etre livrer a plus qu'un livreur
	@Test
	public void valide() {
		delivery1.assignDriver(driver);
		driver.assignDelivery(delivery1);
		delivery1.assignDriver(driver2);
		driver2.assignDelivery(delivery1);
		assertFalse(driver.getDeliveriesAssigned() == driver2.getDeliveriesAssigned());		
	}
	//Vefirication si 2 livraison au meme moment peut etre assigne a 2 livreurs different
	@Test
	public void valideNotSame() {
		delivery1.assignDriver(driver);
		driver.assignDelivery(delivery1);
		delivery.assignDriver(driver2);
		driver2.assignDelivery(delivery);
		assertTrue(driver2.getDeliveriesAssigned() != null);		
	}
	//Verification du conflit d'horaire
	@Test
	public void valideConflit() {
		delivery1.assignDriver(driver);
		driver.assignDelivery(delivery1);
		delivery.assignDriver(driver);
		driver.assignDelivery(delivery);
		assertFalse(delivery.getAssigned());		
	}
	//Verification du test avec le Junior pour voir si il ne peut pas avoir plus de 8 heures de livraision
	@Test
	public void JNRTest() {
		delivery2.assignDriver(driver2);
		driver2.assignDelivery(delivery2);
		assertFalse(delivery2.getAssigned());
	}
	//Verification du test avec le Senior pour voir si il peut avoir plus de 8 heures de livraision
	@Test
	public void SNRTest() {
		delivery2.assignDriver(driver);
		driver.assignDelivery(delivery2);
		assertTrue(delivery2.getAssigned());
	}
	
	

}
