package com.example.droneChallenge;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DroneChallengeApplication {
	static String filePath[];
	static String input[];
    static double n;
    
    
	public static void main(String[] args) {
		
	ConfigurableApplicationContext context=SpringApplication.run(DroneChallengeApplication.class, args);
	
	//File path of the input file
	
	filePath=args[1].split("=");
	input=args[2].split("=");
	
	//size of the matrix where n=m
	n=Double.parseDouble(input[1]);
    
	DroneOrder order=context.getBean(DroneOrder.class);
	order.readOrderFile(filePath[1]);
	order.wareHouseCoords(n);
	order.scheduleDrone();
	order.getPromotersDetractors();
	order.calculateNPS();
	order.writeToFile();
	}
}
