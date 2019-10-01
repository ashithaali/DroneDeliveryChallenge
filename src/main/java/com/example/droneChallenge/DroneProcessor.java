package com.example.droneChallenge;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class DroneProcessor {

List<String> wmOrder= new LinkedList<>();
List<String> coords= new LinkedList<>();
List<String> orderTime= new LinkedList<>();
List<String> orderDeliveryList= new LinkedList<>();
List<String> orderDeliveryTime= new LinkedList<>();
int hour;
Date deliveryTime;
int distanceIndex=0;
SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

/**
 * Read from input file and populate order list, coordinates list and order time list
 * @param data
 */
public void getOrder(String data){
String orderList[];	
String tempOrder[];

orderList=data.split("\n");
for(int i=0;i<orderList.length;i++){
	tempOrder=orderList[i].split(" ");
	wmOrder.add(tempOrder[0]);
	coords.add(tempOrder[1]);
	orderTime.add(tempOrder[2]);
  }

}

/**
 * Process orders for the orders whose order time is less that current drone time
 * @param droneCurrentHour
 * @param x
 * @param y
 * @param listIndex
 * @param orderDeliveryDate
 * @return
 */

public int[] processOrder(int droneCurrentHour, int x, int y, int listIndex, Date orderDeliveryDate){
distanceIndex=listIndex;
int index=0;
int orderHour;
String tempHour[];
String currentCoordList[]= new String[coords.size()];
int[] indexHour=new int[2];

if(listIndex<=(coords.size()-1)) {
orderDeliveryDate.setHours(droneCurrentHour);
orderDeliveryDate.setMinutes(00);
orderDeliveryDate.setSeconds(00);

String tempDate=format.format(orderDeliveryDate);
try {
	deliveryTime=format.parse(tempDate);
}catch(Exception e) {
	System.out.println("Invalid Order Time");
}
tempHour=orderTime.get(listIndex).split(":");
orderHour=Integer.parseInt(tempHour[0]);

while((orderHour<=droneCurrentHour)&&(listIndex<=(coords.size()-1))){
	currentCoordList[index]=coords.get(listIndex);
	index=index+1;
	listIndex=listIndex+1;
	if(listIndex<=(coords.size()-1)){
		tempHour=orderTime.get(listIndex).split(":");
		orderHour=Integer.parseInt(tempHour[0]);
	}
	}
getNearestCoords(currentCoordList,x,y,index);
indexHour[0]=hour;
indexHour[1]=listIndex;
return indexHour;
}else {
	indexHour[0]=-1;
	indexHour[1]=-1;	
  return indexHour;
}
}

/**
 * Determine the distance of the customer location from drone launch center
 * @param currentCoordList
 * @param x
 * @param y
 * @param coordListLen
 */
public void getNearestCoords(String[] currentCoordList, int x, int y, int coordListLen) {
	String temp;
	String[] tempCoord;
	int x1;
	int y1;
	int xDistance;
	int yDistance;
	int distance;
	
	Map<Integer,Integer> distanceMap= new HashMap<>();

	try {
		for(int i=0;i<coordListLen;i++){
			temp=currentCoordList[i];	
			temp=temp.replace('N', ' ');
			temp=temp.replace('S', ' ');
			temp=temp.trim();
			temp=temp.replace('E', '-');
			temp=temp.replace('W', '-');
			tempCoord=temp.split("-");
	 
		 x1=Integer.parseInt(tempCoord[0]);
		 y1=Integer.parseInt(tempCoord[1]);
		 
		 if(x>=x1) {
			 xDistance=x-x1;
		 }
		 else {
			 xDistance=x1-x;
		 }
		 if(y>=y1) {
			 yDistance=y-y1;
		 }else {
			 yDistance=y1-y;
		 }
		 distance=xDistance+yDistance;
		 distanceMap.put(distanceIndex, distance);
		 distanceIndex=distanceIndex+1;
		 }	
	}catch(Exception e) {
		System.out.println("Invalid Coordinates");
	}
	sortDistanceMap(distanceMap);
}

/**
 * Sorts the customer location in the order of smallest distance 
 * @param distanceMap
 */

public void sortDistanceMap(Map<Integer,Integer> distanceMap){
  List<Map.Entry<Integer,Integer>> list =
            new LinkedList<Map.Entry<Integer, Integer> >(distanceMap.entrySet());
	
	
	  Collections.sort(list, new Comparator<Map.Entry<Integer,Integer> >() {
          public int compare(Map.Entry<Integer, Integer> o1,
                             Map.Entry<Integer, Integer> o2) {
              return (o1.getValue()).compareTo(o2.getValue());
          }
      }); 
	  
  Map<Integer,Integer> sortedDistanceMap= new LinkedHashMap<>();
	 int droneDeliveryTime=5;
	 for (Map.Entry<Integer, Integer> entry : list) {
    	 sortedDistanceMap.put(entry.getKey(), entry.getValue());
    	 orderDelivery(entry.getKey(), entry.getValue(), droneDeliveryTime);
    	 droneDeliveryTime=droneDeliveryTime+5;
      }
	}

/**
 * Deliver to customer location and determine the delivery time
 * @param index
 * @param distance
 * @param droneDeliveryTime
 */

public void orderDelivery(int index, int distance, int droneDeliveryTime) {
  int min=droneDeliveryTime;
  int sec=distance;
  String oDeliveryTime;
  String[] tempDate;
  
  Calendar calendar = Calendar.getInstance();
  calendar.setTime(deliveryTime);
  calendar.add(Calendar.MINUTE, min);
  calendar.add(Calendar.SECOND, sec);
  deliveryTime=calendar.getTime();
 
  oDeliveryTime= format.format(deliveryTime);
  tempDate=oDeliveryTime.split(" ");
  oDeliveryTime=tempDate[1];
  int oHr=deliveryTime.getHours();
  orderDeliveryList.add(wmOrder.get(index));
  orderDeliveryTime.add(oDeliveryTime);
  hour=oHr;
  }
}


