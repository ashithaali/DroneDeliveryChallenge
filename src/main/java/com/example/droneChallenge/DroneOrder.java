package com.example.droneChallenge;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DroneOrder{
	int x;
	int y;
	int droneCurrentHour=6;
	int[] indexHour;
	int newHour;
	int promoters=0;
	int detractors=0;
	int neutral=0;
	int nps;
	String filePath;
	@Autowired
	DroneProcessor dp;
	
	/**
	 * 
	 * @param filePath
	 */
	
	public void readOrderFile(String filePath) {
	String data;	
	this.filePath=filePath;
	File file = new File(filePath);
	String path=file.getAbsolutePath()+"/input.txt";
	try (Stream<String> stream = Files.lines(Paths.get(path))){
		 data = stream.collect(Collectors.joining("\n"));
		 dp.getOrder(data);
		 
	  }catch(Exception e) {
		System.out.println(e);
	}
   }
	
	/**
	 * Calculate location of the drone launch center
	 * @param n
	 */
	
	public void wareHouseCoords(double n){
		double tempCoords;
		tempCoords=(n/2)+0.5;
		x=(int)tempCoords;
		y=x;
		//System.out.println("Warehouse coords("+x+","+y+")");
	}
	
  /**
   * Schedule drone from 6:00 AM to 10:00 PM
   */
	
   public void scheduleDrone(){
	   int listIndex=0;
	   Date orderDeliveryDate = new Date();
	   orderDeliveryDate.setHours(06);
	   
	   while(droneCurrentHour<=22){
			indexHour=dp.processOrder(droneCurrentHour,x, y,listIndex, orderDeliveryDate);
			newHour=indexHour[0];
			listIndex=indexHour[1];
			droneCurrentHour=newHour+1;
			
			if(listIndex==-1) {
				break;
			}
			}
		 
     }
   
   /**
    * Calculate the number of promoters, neutral and detractors for the orders
    */
   
   public void getPromotersDetractors() {
   String[] oHr;
   int deliveredHour;
   int orderHour;
   int len=dp.orderDeliveryTime.size();
   int deliveryTime;
  
   for(int i=0;i<len;i++) {
	  oHr=dp.orderDeliveryTime.get(i).split(":");
	  deliveredHour=Integer.parseInt(oHr[0]);
	  oHr=dp.orderTime.get(i).split(":");
	  orderHour=Integer.parseInt(oHr[0]);
	  deliveryTime=deliveredHour-orderHour;
	  if(deliveryTime<=1) {
		  promoters++;
	  }
	  else if(deliveryTime>1 && deliveryTime<4) {
		  neutral++;
	  }
	  else {
		  detractors++;
	  }
	 }
   }
   
   /**
    * Calculate NPS %Promoters-%Detractors
    */
   
   public void calculateNPS() {
	   int totalOrder=dp.orderDeliveryTime.size();;
	   float perPromoters=0;
	   float perDetractors=0;
	  
	   if(promoters>0) {
		  perPromoters=(float)((promoters*100)/totalOrder);
	   }
	   if(detractors>0) {
		  perDetractors=(float)((detractors*100)/totalOrder);
	   }
	   nps=(int)(perPromoters-perDetractors);
	   /*System.out.println("NPS:"+nps);
	   
	   System.out.println("Promoters:"+promoters);
	   System.out.println("Neutral:"+neutral);
	   System.out.println("Detractors:"+detractors);
	   System.out.println("TotalOrder:"+totalOrder);
	   System.out.println("perPr:"+perPromoters);
	   System.out.println("Final Order"+dp.orderDeliveryTime);*/
	   
   }
   
   public void writeToFile(){
	   dp.writeToOrderFile(nps, filePath);
   }
}

