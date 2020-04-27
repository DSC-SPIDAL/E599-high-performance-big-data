package mongo_submit;  //***********Change Package Name*********

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;


import com.mongodb.client.MongoCursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrackService {
	private MongoDatabase mongoDatabase;
    private MongoCollection<Document> TrackInfo_$T;
    
    public TrackService(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
        this.TrackInfo_$T= mongoDatabase.getCollection("TrackInfo");
    }
    
    public ArrayList<String> getAll() {  //track
	 	        
        
        MongoCursor<String> files=this.TrackInfo_$T.distinct("track_name", String.class).iterator();
        
        ArrayList<String> result = new ArrayList<String>();
        
        while(files.hasNext()) {
             result.add(files.next());
          }
        
        return result;
        
    }
    
    public ArrayList<String> getTrack(String Track_name) {  //track
		
        
        
        
        Document doc1=new Document();
        Document doc2=new Document();
        
        doc2.append("track_name", Track_name);
        
        doc1.append("track_name", "$track_name");
        doc1.append("venue", "$venue");
        doc1.append("number_of_sections", "$number_of_sections");
        doc1.append("race_id", "$race_id");
        doc1.append("section_information","$section_information");
        
        
        AggregateIterable<Document> output= this.TrackInfo_$T.aggregate(Arrays.asList(
        		new Document("$match",new Document(doc2)),
        		new Document("$group",new Document("_id",doc1))
        		));
        
        
        String a;
        String[] b;
        ArrayList<String> result = new ArrayList<String>();
        
        for (Document dbObject : output)
        {
        	a=dbObject.toJson();
        	//System.out.println(a.toString());
        	a=a.substring(12, a.length()-3);
            b=a.split(",(?=(((?!\\]).)*\\[)|[^\\[\\]]*$)");
            //System.out.println(Arrays.toString(b));
            result.add(Arrays.toString(b));
           
        }
        return result;
	 
 }
    
    
}
