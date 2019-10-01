package com.example.droneChallenge;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class DroneOrderTests extends DroneChallengeApplicationTests{
	
	@Test
	public void testWMOrder() {
		String data="WM0001 N11W5 05:11:50\nWM0002 S3E2 05:11:55\nWM0003 N7E50 05:31:50";
		String[] wmOrder= new String[1];
		String[] resultWmOrder= new String[1];
		
		DroneProcessor dp= new DroneProcessor();
		dp.getOrder(data);
		//expected order
		wmOrder[0]="WM0001";
		resultWmOrder[0]=dp.wmOrder.get(0).toString();
		
		assertEquals(resultWmOrder[0],wmOrder[0]);
		}
	
	@Test
	public void testOrderCoordinates() {
		String data="WM0001 N11W5 05:11:50\nWM0002 S3E2 05:11:55\nWM0003 N7E50 05:31:50";
		String[] coords= new String[1];
		String[] resultCoords= new String[1];
		
		DroneProcessor dp= new DroneProcessor();
		dp.getOrder(data);
		
		coords[0]="N11W5";
		resultCoords[0]=dp.coords.get(0).toString();
		
		assertEquals(coords[0],resultCoords[0]);
		}
	
     @Test
     public void testProcessOrder(){
    	 int droneCurrentHour=6;
    	 int x=26; 
    	 int y=26;
    	 int listIndex=0;
    	 Date orderDeliveryDate = new Date();
  	   	 orderDeliveryDate.setHours(06);
  	     DroneProcessor dp= new DroneProcessor();
  	     dp.processOrder(droneCurrentHour, x, y, listIndex, orderDeliveryDate);
  	     assertNotNull(orderDeliveryDate);
  	    assertEquals(droneCurrentHour,6);
  	   }
   

    @Test
    public void testGridSize() {
        double n=9;
        DroneOrder o= new DroneOrder();
        o.wareHouseCoords(n);
        assertEquals(o.x,o.y);
      }
}