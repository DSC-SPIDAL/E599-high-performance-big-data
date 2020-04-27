package mongo_submit; //***********Change Package Name*********


import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class mongoclienttest {
    public static void main(String[] args) {
        
    	IndycarDBClient indycarDBClient = new IndycarDBClient("localhost");

        
        //System.out.println(indycarDBClient.drivers().getAll());
        //System.out.println(indycarDBClient.drivers().getDriversByRace(1));
        //System.out.println(indycarDBClient.drivers().getdriver("747"));
        //System.out.println(indycarDBClient.drivers().search("st"));
    	//System.out.println(indycarDBClient.drivers().getLapRecords(1,"23"));
    	
    	
    	
    	//System.out.println(indycarDBClient.getRaceService().getAll());
    	//System.out.println(indycarDBClient.getRaceService().getRace(1));
    	//System.out.println(indycarDBClient.getRaceService().getFlags(1));
    	//System.out.println(indycarDBClient.getRaceService().getRanks(1));
    	//System.out.println(indycarDBClient.getRaceService().getsnapshot_final(16,24,0.5,1,1)); //sometimes may not print due to slower mongo java connection, use the below method.
    	
    	/*
    	for(String j: indycarDBClient.getRaceService().getsnapshot_final(16,24,0.5,1,1)) {
    		System.out.println(j);
    	}*/
    	
    	//System.out.println(indycarDBClient.getTrackService().getAll());
    	//System.out.println(indycarDBClient.getTrackService().getTrack("Indianapolis Motor Speedway"));
    	
    }
}