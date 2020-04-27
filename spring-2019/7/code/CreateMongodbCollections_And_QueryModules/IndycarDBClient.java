package mongo_submit;//***********Change Package Name*********

import com.mongodb.client.MongoDatabase;

import com.mongodb.MongoClient; 

public class IndycarDBClient {

    private MongoDatabase database;

    /**
     * With this constructor, indycar db will be selected by default
     *
     * @param url
     */
    public IndycarDBClient(String url) {
        MongoClient mongoClient =new MongoClient( url);
        this.database = mongoClient.getDatabase("Sahaj");  //add database name instead of Sahaj
    }

    /**
     * With this constructor default db can be changed
     *
     * @param url
     * @param db
     */
    public IndycarDBClient(String url,String db) {
    	
        MongoClient mongoClient = new MongoClient( url);
        MongoDatabase database = mongoClient.getDatabase(db);
    
    }

    public DriverService drivers() {
        return new DriverService(this.database);
    }
    
    public RaceService getRaceService() {
        return new RaceService(this.database);
    }
    
    public TrackService getTrackService() {
    	return new TrackService(this.database);
    }
    
    


}
