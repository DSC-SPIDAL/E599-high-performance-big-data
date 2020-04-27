package mongo_submit;  //***********Change Package Name*********


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;


import com.mongodb.client.MongoCursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class DriverService {

    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> EntryInfo_$E;
    private MongoCollection<Document> CompletedLaps_$C;
    private MongoCollection<Document> CompletedSection_$S;

    public DriverService(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
        this.EntryInfo_$E = mongoDatabase.getCollection("EntryInfo");
        this.CompletedLaps_$C=mongoDatabase.getCollection("CompletedLaps");
        this.CompletedSection_$S=mongoDatabase.getCollection("CompletedSection");
    }

    
    
    public ArrayList<String> getAll() {
    	 Document doc1=new Document();
	        Document doc2=new Document();
	        Document doc3=new Document();
	        Document doc4=new Document();
	        Document doc5=new Document();
	        
	        
	        doc1.append("$ne", "ONE TEST");
	        doc2.append("$ne", "");
	        doc3.append("driver_name", doc1);
	        doc4.append("driver_name", doc2);
	        List<Document> main_doc = new ArrayList<>();
	        main_doc.add(doc3);
	        main_doc.add(doc4);
	        doc5.append("$and", main_doc);
	        
	        Document doc6=new Document();
	        //Document doc7=new Document();
	        //Document doc8=new Document();
	        
	        doc6.append("driver_name","$driver_name");
	        doc6.append("driver_id","$driver_id");
	        
	        AggregateIterable<Document> output=this.EntryInfo_$E.aggregate(Arrays.asList(
	        		new Document("$match",doc5),
	        		new Document("$group",new Document("_id",doc6))
	        		));
	        String a;
	        String[] b;
	        
	        ArrayList<String> result = new ArrayList<String>();
	        
	        for (Document dbObject : output)
	        {
	        	a=dbObject.toJson();
	        	a=a.substring(12, a.length()-3);
	            b=a.split(",");
	            
	            result.add(Arrays.toString(b));
	           
	        }
	        
	        return result;

    }
    
    
    public ArrayList<String> getDriversByRace(int race_id) {
		
        Document doc1=new Document();
        Document doc2=new Document();
        Document doc3=new Document();
        Document doc4=new Document();
        Document doc5=new Document();
        Document doc6=new Document();
        
        
        doc1.append("$ne", "ONE TEST");
        doc2.append("$ne", "");
        doc3.append("driver_name", doc1);
        doc4.append("driver_name", doc2);
        doc6.append("race_id",String.valueOf(race_id));
        List<Document> main_doc = new ArrayList<>();
        main_doc.add(doc3);
        main_doc.add(doc4);
        main_doc.add(doc6);
        doc5.append("$and", main_doc);
        
        
        Document doc7=new Document();
        
        doc7.append("driver_name","$driver_name");
        doc7.append("driver_id","$driver_id");
        
        
        AggregateIterable<Document> output=this.EntryInfo_$E.aggregate(Arrays.asList(
        		new Document("$match",doc5),
        		new Document("$group",new Document("_id",doc7))
        		));
        String a;
        String[] b;
        
        ArrayList<String> result = new ArrayList<String>();
        
        for (Document dbObject : output)
        {
        	a=dbObject.toJson();
        	a=a.substring(12, a.length()-3);
            b=a.split(",");
            
            result.add(Arrays.toString(b));
           
        }
        return result;
        
    }
    
    
    public ArrayList<String> getdriver(String driver_id){
		    
	        Document doc1=new Document();
	        Document doc2=new Document();
	        
	        
	        
	        doc1.append("driver_id",driver_id);
	        doc2.append("driver_name","$driver_name");
	        doc2.append("driver_id","$driver_id");
	        doc2.append("car_number","$car_number");
	        doc2.append("engine","$engine");
	        doc2.append("team","$team");
	        doc2.append("team_id","$team_id");
	        doc2.append("hometown","$hometown");
	        doc2.append("unique_identifier","$unique_identifier");
	        
	        AggregateIterable<Document> output=this.EntryInfo_$E.aggregate(Arrays.asList(
	        		new Document("$match",doc1),
	        		new Document("$group",new Document("_id",doc2))
	        		));
	        String a;
	        String[] b;
	        
	        ArrayList<String> result = new ArrayList<String>();
	        
	        for (Document dbObject : output)
	        {
	        	a=dbObject.toJson();
	        	a=a.substring(12, a.length()-3);
	            b=a.split(",");
	            
	            result.add(Arrays.toString(b));
	           
	        }
	        
	        return result;
		 
	 }
    
    public ArrayList<String> search(String check_str) { 
		
        Document doc1=new Document();
        Document doc2=new Document();
        Document doc3=new Document();
        Document doc4=new Document();
        Document doc5=new Document();
        Document doc6=new Document();
        Document doc7=new Document();
        
        String pass_string="\\b"+check_str;
        
        
        doc1.append("$ne", "ONE TEST");
        doc2.append("$ne", "");
        doc7.append("$regex", pass_string);
        doc7.append("$options", "i");
        doc3.append("driver_name", doc1);
        doc4.append("driver_name", doc2);
        doc6.append("driver_name",doc7);
        List<Document> main_doc = new ArrayList<>();
        main_doc.add(doc3);
        main_doc.add(doc4);
        main_doc.add(doc6);
        doc5.append("$and", main_doc);
        
        // db.EntryInfo.distinct("driver_name",{$and:[{"driver_name":{$ne:"ONE TEST"}},{"driver_name":{$ne:""}},{"driver_name":{$regex:"\\bCa"}}]})
        
        MongoCursor<String> files=this.EntryInfo_$E.distinct("driver_name", String.class).filter(doc5).iterator();
        
        ArrayList<String> result = new ArrayList<String>();
        
        while(files.hasNext()) {
            result.add(files.next());
          }
        return result;
    }
    
    public ArrayList<ArrayList<String>> getLapRecords(int race_id,String car) {
		
        
        ArrayList<ArrayList<String>> combined_result_of_lap_and_section_data = new ArrayList<ArrayList<String>>();
        
        Document doc1=new Document();
        Document doc2=new Document();
        
        doc2.append("$ne", "U");
        
        doc1.append("car_num", car);
        doc1.append("track_status",doc2);
        doc1.append("run_command", "R");
        doc1.append("race_id", String.valueOf(race_id));
        
        Document doc3=new Document();
        
        doc3.append("car_num" , "$car_num");
        doc3.append("completed_laps" , "$completed_laps");
        doc3.append("elapsed_time" , "$elapsed_time");
        doc3.append("last_laptime" , "$last_laptime");
        doc3.append("lap_status" , "$lap_status");
        doc3.append("best_lap_time" , "$best_lap_time");
        doc3.append("best_lap" , "$best_lap");
        doc3.append("current_status" , "$current_status");
        doc3.append("pit_stop_counts" , "$pit_stop_counts");
        doc3.append("last_pitted_lap" , "$last_pitted_lap");
        doc3.append("start_position" , "$start_position");
        doc3.append("laps_led" , "$laps_led");
        doc3.append("run_command" , "$run_command");
        doc3.append("race_id" , "$race_id");
        
        
        
        AggregateIterable<Document> output=this.CompletedLaps_$C.aggregate(Arrays.asList(
        		new Document("$match",doc1),
        		new Document("$group",new Document("_id",doc3)),
        		new Document("$sort",new Document("_id",1))
        		));
        String a;
        String[] b;
        
        ArrayList<String> result1 = new ArrayList<String>();
        
        for (Document dbObject : output)
        {
        	a=dbObject.toJson();
        	a=a.substring(12, a.length()-3);
            b=a.split(",");
            
            result1.add(Arrays.toString(b));
           
        }
        
        
        Document doc4=new Document();
        Document doc5=new Document();
        
        doc4.append("car_num", car);
        doc4.append("run_command", "R");
        doc4.append("race_id", String.valueOf(race_id));
        
        doc5.append("car_num", "$car_num");
        doc5.append("section_identifier","$section_identifier");
        doc5.append("elapsed_time" , "$elapsed_time");
        doc5.append("last_section_time" , "$last_section_time");
        doc5.append("last_lap","$last_lap");
        doc5.append("run_command" , "$run_command");
        doc5.append("race_id" , "$race_id");
        doc5.append("unrelated_counter","$unrelated_counter");
        
        
        
        AggregateIterable<Document> output_2=this.CompletedSection_$S.aggregate(Arrays.asList(
        		new Document("$match",doc4),
        		new Document("$group",new Document("_id",doc5)),
        		new Document("$sort",new Document("_id.unrelated_counter",1))
        		));
        
        String c;
        String[] d;
        ArrayList<String> result2 = new ArrayList<String>();
        
        for (Document dbObject : output_2)
        {
        	c=dbObject.toJson();
        	c=c.substring(12, c.length()-3);
            d=c.split(",");
            result2.add(Arrays.toString(d));
           
        }
        	        
        
        combined_result_of_lap_and_section_data.add(result1);
        combined_result_of_lap_and_section_data.add(result2);
        
        return combined_result_of_lap_and_section_data;
        
 }
 

	 
   
}