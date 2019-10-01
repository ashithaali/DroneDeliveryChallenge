package com.example.droneChallenge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
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
	boolean error=false;
	
	@Autowired
	DroneProcessor dp;
	
	/**
	 * 
	 * @param filePath
	 */
	
	public void readOrderFile(String filePath) {
	String data;	
	this.filePath=filePath;
	try {
	File file = new File(filePath);
	String path=file.getAbsolutePath()+"/input.txt";
	try (Stream<String> stream = Files.lines(Paths.get(path))){
		 data = stream.collect(Collectors.joining("\n"));
		 dp.getOrder(data);
		 
	  }catch(IOException e) {
		error=true; 
		System.out.println("File Not Found");
		}
	}catch(Exception e) {
		error=true; 
		System.out.println("Invalid Input File Path");
	}
   }
	
	/**
	 * Calculate location of the drone launch center
	 * @param n
	 */
	
	public void wareHouseCoords(double n){
		if(n>0) {
		double tempCoords;
		tempCoords=(n/2)+0.5;
		x=(int)tempCoords;
		y=x;
		//System.out.println("Warehouse coords("+x+","+y+")");
		}else {
			error=true;
			System.out.println("Invalid Grid Size");
			}
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
   
   /**
    * Write order, delivered time and NPS to output file
    * @param nps
    */

   public void writeToOrderFile() {
   	try {
   		String data="";
   		String NPS="NPS:"+nps;
   		String path=filePath+"/output.txt";
   		File file = new File(path);
   		//path=file.getAbsolutePath();
   		if(error==false) {
   			System.out.println("Output FilePath:"+ path);
   		}
   		if (!file.exists()) {
               file.createNewFile();
           }
   		
   		FileWriter fw = new FileWriter(file.getAbsoluteFile());
           BufferedWriter bw = new BufferedWriter(fw);
           
           Iterator<String> itr1=dp.orderDeliveryList.iterator();
           Iterator<String> itr2=dp.orderDeliveryTime.iterator();
           while(itr1.hasNext()&&itr2.hasNext()) {
           	data=itr1.next()+" "+itr2.next();
           	bw.write(data);
           	bw.append('\n');
           }
           bw.write(NPS);
           bw.close();
   	}catch (IOException e) {
   		   error=true;
           System.out.println("Error occured while writing to output file, validate input file path");
       }
   }
}

