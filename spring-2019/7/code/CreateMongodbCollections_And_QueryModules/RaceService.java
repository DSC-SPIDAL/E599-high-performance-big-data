package mongo_submit;  //***********Change Package Name*********

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;

import org.bson.Document;


import com.mongodb.client.MongoCursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class RaceService {
	
	private MongoDatabase mongoDatabase;
    private MongoCollection<Document> RunInfo_$R;
    private MongoCollection<Document> FlagInfo_$F;
    private MongoCollection<Document> OverallResults_$O;
    private MongoCollection<Document> Telemetry_$P;
    private MongoCollection<Document> EntryInfo_$E;
    
    public RaceService(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
        this.RunInfo_$R = mongoDatabase.getCollection("RunInfo");
        this.FlagInfo_$F= mongoDatabase.getCollection("FlagInfo");
        this.OverallResults_$O= mongoDatabase.getCollection("OverallResults");
        this.Telemetry_$P=mongoDatabase.getCollection("telemetry");
        this.EntryInfo_$E=mongoDatabase.getCollection("EntryInfo");
    }
    
    
    public ArrayList<String> getAll() { //Race
		 
	     
	     Document doc1=new Document();
	     doc1.append("run_command","R");
	     Document doc2=new Document();
	     
	     doc2.append("event_name", "$event_name");
	     doc2.append("race_id", "$race_id");
	     
	     AggregateIterable<Document> output_2=this.RunInfo_$R.aggregate(Arrays.asList(
	        		new Document("$match",doc1),
	        		new Document("$group",new Document("_id",doc2))
	        		));
	    
	     	String c;
	        String[] d;
	        ArrayList<String> result = new ArrayList<String>();
	        
	        for (Document dbObject : output_2)
	        {
	        	c=dbObject.toJson();
	        	c=c.substring(12, c.length()-3);
	            d=c.split(",");
	            //System.out.println(Arrays.toString(d));
	            result.add(Arrays.toString(d));
	           
	        }
	  return result;
		 
	 }
    
    public ArrayList<String> getRace(int race_id) { //race
		 
	     Document doc1=new Document();
	     doc1.append("run_command","R");
	     doc1.append("race_id", String.valueOf(race_id));
	     Document doc2=new Document();
	     
	     doc2.append("event_name", "$event_name");
	     doc2.append("event_round", "$event_round");
	     doc2.append("start_time_date", "$start_time_date");
	     doc2.append("race_id", "$race_id");
	     
	     AggregateIterable<Document> output_2=this.RunInfo_$R.aggregate(Arrays.asList(
	        		new Document("$match",doc1),
	        		new Document("$group",new Document("_id",doc2))
	        		));
	    
	     String c;
	     String[] d;
	     ArrayList<String> result = new ArrayList<String>();
	        
	     
	     
	        for (Document dbObject : output_2)
	        {
	        	c=dbObject.toJson();
	        	c=c.substring(12, c.length()-3);
	            d=c.split(",");
	            //System.out.println(Arrays.toString(d));
	            result.add(Arrays.toString(d));
	        }
	        return result;
	 }
    
    
    public ArrayList<String> getFlags(int race_id) { //race
	        
	        Document doc1=new Document();
	        Document doc2=new Document();
	        Document doc3=new Document();
	        Document doc4=new Document();
	        
	        doc1.append("$ne","U");
	        doc2.append("$exists", "true");
	        
	        doc3.append("track_status",doc1);
	        doc3.append("race_id",String.valueOf(race_id));
	        doc3.append("timeOfDay",doc2);
	        
	        doc4.append("track_status","$track_status");
	        doc4.append("lap_number","$lap_number");
	        doc4.append("timeOfDay","$timeOfDay");
	        doc4.append("counter_$F","$counter_$F");
	        
	        
	        
	        AggregateIterable<Document> output_2=this.FlagInfo_$F.aggregate(Arrays.asList(
	        		new Document("$match",doc3),
	        		new Document("$group",new Document("_id",doc4)),
	        		new Document("$sort",new Document("_id.counter_$F",1))
	        		));
	    
	     String c;
	     String[] d;
	     ArrayList<String> result = new ArrayList<String>();
	     
	        for (Document dbObject : output_2)
	        {
	        	c=dbObject.toJson();
	        	c=c.substring(12, c.length()-3);
	            d=c.split(",");
	            //System.out.println(Arrays.toString(d));
	           result.add(Arrays.toString(d));
	        }
	        //System.out.println("finished");
	        
	        return result;
	 }
	 
    
    public ArrayList<String> getRanks(int race_id) { //race
		 
	     Document doc1=new Document();
	     
	     doc1.append("race_id", String.valueOf(race_id));
	     doc1.append("flag_status","Finish");
	     Document doc2=new Document();
	     
	     doc2.append("overall_rank","$overall_rank");
	     doc2.append("no","$no");
	     doc2.append("first_name","$first_name");
	     doc2.append("last_name","$last_name");
	     
	     AggregateIterable<Document> output_2=this.OverallResults_$O.aggregate(Arrays.asList(
	        		new Document("$match",doc1),
	        		new Document("$group",new Document("_id",doc2)),
	        		new Document("$sort",new Document("_id.overall_rank",1))
	        		));
	    
	     String c;
	     String[] d;
	     ArrayList<String> result = new ArrayList<String>();
	     
	        for (Document dbObject : output_2)
	        {
	        	c=dbObject.toJson();
	        	c=c.substring(12, c.length()-3);
	            d=c.split(",");
	            //System.out.println(Arrays.toString(d));
	           result.add(Arrays.toString(d));
	        }
	        //System.out.println("finished");
	        
	        return result;
	 }
    
	 public ArrayList<String> get_nearest_car(String car_num, double time_in_seconds, int number_of_car_records,int race_id){

	        Document doc1=new Document();
	        Document doc2=new Document();
	        Document doc3=new Document();
	        Document doc4=new Document();
	        Document doc5=new Document();
	        Document doc6=new Document();
	        
	        
	        List<Double> array_doc = new ArrayList<>();
	        array_doc.add(time_in_seconds/500);
	        array_doc.add(0.0);
	        doc3.append("$near", array_doc);
	        doc2.append("ratio",doc3);
	        doc2.append("car_num", car_num);
	        doc2.append("race_id",String.valueOf(race_id));
	        
	        
	        FindIterable<Document> output_2=this.Telemetry_$P.find(doc2).limit(number_of_car_records);
	        
	         String c;
	   	     String[] d;
	   	     ArrayList<String> result = new ArrayList<String>();
	   	     
	   	        for (Document dbObject : output_2)
	   	        {
	   	        	c=dbObject.toJson();
	   	        	c=c.substring(12, c.length()-3);
	   	            d=c.split(",");
	   	            //System.out.println(Arrays.toString(d));
	   	           result.add(Arrays.toString(d));
	   	        }
	        	//System.out.println(result.getClass());
	        	return result;
	        }
	        
    
    
    public ArrayList<String> getsnapshot_final(int hours,int minutes, double seconds,int race_id,int threshold_in_seconds){
        
        int i;
        double time_in_seconds;
        time_in_seconds=(double) (hours*60);
        time_in_seconds=time_in_seconds+minutes;
        time_in_seconds=time_in_seconds*60;
        time_in_seconds=time_in_seconds+seconds;
        
        Document doc1=new Document();
        Document doc2=new Document();
        Document doc3=new Document();
        Document doc4=new Document();
        Document doc5=new Document();
        
        
        doc1.append("$ne", "");
        doc2.append("car_number", doc1);
        doc2.append("race_id",String.valueOf(race_id));
        
        MongoCursor<String> files=this.EntryInfo_$E.distinct("car_number", String.class).filter(doc2).iterator();
        
        
        ArrayList<String> car_numbers_list = new ArrayList<String>();
        
        while(files.hasNext()) {
        	car_numbers_list.add(files.next());
          }
        
        String a;
        String b[];
        Double k;
        ArrayList<String> car_details = new ArrayList<String>();
        //ArrayList<String> car_details=new ArrayList<String>(); 
        for(String data : car_numbers_list) {
   		    //System.out.println(get_nearest_car(data,time_in_seconds,1,race_id).getClass());
        
        	a=get_nearest_car(data,time_in_seconds,1,race_id).get(0);
        	b=a.split(",");
        	k=Double.parseDouble(b[8].substring(22));
        	
        	if(Math.abs(k-time_in_seconds)>threshold_in_seconds) {
        		continue;
        	}
        	
        	car_details.add(a);
        	//System.out.println("1"+car_details);
   	 }
        
       // car_details.add(car_numbers_list);
        //System.out.println("2"+car_details);
        
        return car_details;
 }
 
 
 
    
    
}
