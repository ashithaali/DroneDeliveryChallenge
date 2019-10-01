package com.example.droneChallenge;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DroneChallengeApplication  implements ApplicationRunner{

List<String> argsList=new ArrayList<>();
static int n;
static String filePath;
    
    public static void main(String[] args){
    	ConfigurableApplicationContext context=SpringApplication.run(DroneChallengeApplication.class, args);
    	DroneOrder order=context.getBean(DroneOrder.class);
    	
    	order.readOrderFile(filePath);
    	order.wareHouseCoords(n);
    	order.scheduleDrone();
    	order.getPromotersDetractors();
    	order.calculateNPS();
    	order.writeToOrderFile();
    	
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception{
    	String gridSize="";
    	argsList= args.getOptionValues("input");
    		if(argsList!=null) {
    			filePath=argsList.get(0).toString();
    		}
    		argsList=args.getOptionValues("n");
    		if(argsList!=null) {
    			gridSize=argsList.get(0).toString();
    			n=Integer.parseInt(gridSize);
    		}	
      }
    } 	
    