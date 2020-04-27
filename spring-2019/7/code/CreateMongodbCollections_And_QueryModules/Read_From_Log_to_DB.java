
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient; 
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Aggregates.*;
import com.mongodb.Block;

//UID FOR DRIVER NAMES

public class mongo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// run this code to insert documents into the collections
		File a=new File("IPBroadcaster_Input_2018-05-27_0.log");


		try {
			writeToDB(a,1);
		}
		catch(IOException e) {
			System.out.println("error in reading"+e);
		
		
		
		// THESE ARE THE FUNCTIONS AS MENTIONED IN THE DOC FILE
		
		//----Driver----
		//System.out.println(getAll());
		//System.out.println(getDriversByRace(1)); //arguments- race_id (int)
		//System.out.println(search("st"));
		//System.out.println(getlapdata(1,"23")); //as well as section data, arguments- race_id (int), car number (string)
		//System.out.println(getdriverbyID("752"));
		
		//-----------Track------
		//System.out.println(getAll_Track());
		//System.out.println(getInfo_Track("Indianapolis Motor Speedway"));   //arguments- track_name(string)
		
		
		//--------Race--------
		//System.out.println(getAll_Race());
		//System.out.println(getrace(1)); //arguments- race_id (int)
		//System.out.println(getflags_race(1)); //arguments- race_id (int)
		//System.out.println(getranks(1));
		//System.out.println(getsnapshot(16,24,0.5,1,1)); // arguments- hours(int),minutes(int),seconds(double),race_id (int), previous time in seconds for which the data is to be queried (double)(maximum can be 60 seconds) (example-previous 1 seconds data in this example). 
		//System.out.println(getsnapshot_for_car(16,24,0.5,1,1,"3")); //arguments- same as above followed by car_num
        
		System.out.println("3"
				+ ""+getsnapshot_final(16,24,0.5,1,1));
		
		
		//System.out.println(get_nearest_car("59",59458.08,1,1));
		
	}
	
	
	 public static void writeToDB(File file, int race_id) throws IOException {
	        System.out.println("Parsing file " + file.getAbsolutePath());
	        
	        MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        System.out.println("Created successDocument");
	        
	        MongoCollection<Document> Telemetry_$P= database.getCollection("telemetry"); 
	        MongoCollection<Document> CompletedLaps_$C= database.getCollection("CompletedLaps");  
	        MongoCollection<Document> CompletedSection_$S= database.getCollection("CompletedSection");
	        MongoCollection<Document> TrackInfo_$T= database.getCollection("TrackInfo");
	        MongoCollection<Document> OverallResults_$O= database.getCollection("OverallResults");
	        MongoCollection<Document> RunInfo_$R= database.getCollection("RunInfo");
	        MongoCollection<Document> FlagInfo_$F= database.getCollection("FlagInfo");
	        MongoCollection<Document> EntryInfo_$E= database.getCollection("EntryInfo");
	        
	        
	        
	        
	        FileReader fis = new FileReader(file);
	        
	        System.out.println("1");
	        String date = file.getName().split("_")[2];
	        System.out.println("1");
	        BufferedReader br = new BufferedReader(fis);
	        System.out.println("1");
	        String line = br.readLine();

	        List<Document> docs_Telemetry = new ArrayList<>();
	        List<Document> docs_CompletedLaps = new ArrayList<>();
	        List<Document> docs_CompletedSection = new ArrayList<>();
	        List<Document> docs_Trackinfo = new ArrayList<>();
	        List<Document> docs_OverallResults = new ArrayList<>();
	        List<Document> docs_RunInfo = new ArrayList<>();
	        List<Document> docs_FlagInfo = new ArrayList<>();
	        List<Document> docs_EntryInfo = new ArrayList<>();
	        
	        int counter_$S=0;
	        int counter_$O=0;
	        //-----changed
	        int counter_$F=0;
	        Double time_in_seconds;
	        //change ended
	        String flag_from_$h="not yet initialized";
	        int hours;
	        int minutes;
	        double seconds;
	         //for $C gets activated when $R event round is not equal to "Sweeps"
	        String event_name="not yet initialized";
	        String run_command="not yet initialized";
	        //-----changed
	        String timeOfDay="not yet initialized";
	      //change ended
	        //IndexOptions indexOptions = new IndexOptions().unique(true);
	        //EntryInfo_$E.createIndex(Indexes.compoundIndex(Indexes.text("command"),Indexes.text("driver_name"),Indexes.text("race_id")),indexOptions);
	        //"unique_identifier"),Indexes.text("driver_name"),Indexes.text("start_position"),Indexes.text("field_count"),Indexes.text("Class"),Indexes.text("driver_id"),Indexes.text("transponder_control"),Indexes.text("equipment"),Indexes.text("license"),Indexes.text("team"),Indexes.text("team_id"),Indexes.text("engine"),Indexes.text("entrant_id"),Indexes.text("points_eligible"),Indexes.text("hometown"),Indexes.text("competitor_id")));
	        
	        while (line != null) {
	            if (line.startsWith("$P")) {
	                String[] splits = line.split("¦");
	                String command=splits[0];
	                String carNumber = splits[1];
	                //-----changed
	                timeOfDay = splits[2];
	              //change ended
	                String[] time_array=timeOfDay.split(":");
	                
	                //System.out.println(Arrays.toString(splits));
	                
	                
	                
	                
	                String lapDistance = splits[3];
	                String vehicleSpeed = splits[4];
	                String engineSpeed = splits[5];
	                String throttle = splits[6];
	                String RaceID= String.valueOf(race_id);
	                
	                //https://drive.google.com/drive/u/0/folders/1ZZn0r7k77wQMBn6jqM0yay07p25DX9q_
	                //https://docs.google.com/document/d/1zrIDjnpWWYyfBBCehExnywTHkAHmh6NOdbCBg7JugBc/edit#
	                //https://drive.google.com/file/d/1ELCmImp9F5kBCWQegAMQmzgzGGARzu23/view
	                Document document = new Document(); //why create docs, when u are clearing it???
	                
	                document.append("command", command);
	                document.append("car_num", carNumber);
	                document.append("lap_distance", lapDistance);
	                document.append("time_of_day", timeOfDay);
	                
	                
	           
	                
	                if(run_command.equals("R") && (!flag_from_$h.contentEquals("U"))&&(!flag_from_$h.contentEquals("C"))&&(!flag_from_$h.contentEquals("not yet initialized"))&&timeOfDay.length()>=11) {
	                	//System.out.println(Arrays.toString(splits));
	                	hours=Integer.parseInt(time_array[0]);
		                minutes=Integer.parseInt(time_array[1]);
		                seconds=Double.parseDouble(time_array[2]);
		                time_in_seconds=(double) (hours*60);
		                time_in_seconds=time_in_seconds+minutes;
		                time_in_seconds=time_in_seconds*60;
		                time_in_seconds=time_in_seconds+seconds;
		                document.append("hours", hours);
		                document.append("minutes", minutes);
		                document.append("seconds", seconds);
		                document.append("time_in_seconds", time_in_seconds);
		                List<Double> geoloc = new ArrayList<>();
		                geoloc.add(time_in_seconds/500);
		                geoloc.add(0.0);
		                document.append("ratio", geoloc);
		                document.append("time_for_comparison",1);
		                
		                //System.out.println("hello");
	                }
	                else if(timeOfDay.length()>=11){
	                	hours=Integer.parseInt(time_array[0]);
		                minutes=Integer.parseInt(time_array[1]);
		                seconds=Double.parseDouble(time_array[2]);
		                time_in_seconds=(double) (hours*60);
		                time_in_seconds=time_in_seconds+minutes;
		                time_in_seconds=time_in_seconds*60;
		                time_in_seconds=time_in_seconds+seconds;
		                document.append("hours", hours);
		                document.append("minutes", minutes);
		                document.append("seconds", seconds);
		                document.append("time_in_seconds", time_in_seconds);
		                List<Double> geoloc = new ArrayList<>();
		                geoloc.add(time_in_seconds/500);
		                geoloc.add(0.0);
		                document.append("ratio", geoloc);
		                
	                	document.append("time_for_comparison",0);
	                }
	                else {
	                	document.append("time_for_comparison",0);
	                }
	                
	                document.append("vehicle_speed", vehicleSpeed);
	                document.append("engine_rpm", engineSpeed);
	                document.append("throttle", throttle);
	                document.append("date", date);
	                document.append("race_id", RaceID);
	                document.append("flag_from_$h", flag_from_$h);
	                document.append("run_command", run_command);
	                //document.append("counter_overall_results", counter_$O);
	                
	                

	                if (docs_Telemetry.size() == 100) {
	                	Telemetry_$P.insertMany(docs_Telemetry);
	                    docs_Telemetry.clear();
	                } else {
	                	docs_Telemetry.add(document);
	                }
	            }
	           
	            else if(line.startsWith("$C")) {
	            	
	            	//completed laps, start position, overall position will be integers
	            	
	            	//Time behind prec 15, Laps behind prec 16, Overall best lap time 18 not included along with 1,2,3,4 positioned items in record
	            	String[] splits = line.split("¦");
	            	String command=splits[0];
	            	//String rank=splits[4];
	                String carNumber = splits[5];
	                String completed_laps = splits[7];
	                String elapsed_time = splits[8];
	                String last_laptime = splits[9];
	                String lap_status = splits[10];
	                String best_lap_time = splits[11];
	                String best_lap = splits[12];
	                String times_behind_leader=splits[13];
	                String laps_behind_leader=splits[14];
	                String overall_rank=splits[17];
	                String current_status=splits[19];
	                String track_status=splits[20];
	                String pit_stop_counts=splits[21];
	                String last_pitted_lap=splits[22];
	                String start_position=splits[23];
	                String laps_led=splits[24];
	                String RaceID= String.valueOf(race_id);
	                
	                //  
	                
	                Document document = new Document();
	                document.append("command", command);
	                //document.append("rank",  Integer.parseInt(rank,16));
	                document.append("car_num", carNumber);
	                document.append("completed_laps", Integer.parseInt(completed_laps,16));
	                document.append("elapsed_time", elapsed_time);
	                document.append("last_laptime", last_laptime);
	                document.append("lap_status", lap_status);
	                document.append("best_lap_time", best_lap_time);
	                document.append("best_lap", best_lap);
	                document.append("times_behind_leader", times_behind_leader);
	                document.append("laps_behind_leader", laps_behind_leader);
	                document.append("overall_rank", Integer.parseInt(overall_rank,16));
	                document.append("current_status",current_status);
	                document.append("track_status", track_status);
	                document.append("pit_stop_counts",pit_stop_counts );
	                document.append("last_pitted_lap",Integer.parseInt(last_pitted_lap,16));
	                document.append("start_position",Integer.parseInt(start_position,16));
	                document.append("laps_led",laps_led);
	                document.append("event_name",event_name);
	                document.append("run_command", run_command);
	                document.append("race_id", RaceID);
	                
	                

	                if (docs_CompletedLaps.size() == 100) {
	                	CompletedLaps_$C.insertMany(docs_CompletedLaps);
	                    docs_CompletedLaps.clear();
	                } else {
	                	docs_CompletedLaps.add(document);
	                }
	            }
	            
	            else if(line.startsWith("$S")) {
	            	
	            	//last lap is integer.
	            	
	            	String[] splits = line.split("¦");
	            	String command=splits[0];
	                String carNumber = splits[4];
	                String section_identifier = splits[6];
	                String elapsed_time = splits[7];
	                String last_section_time = splits[8];
	                String last_lap = splits[9];
	                
	                String RaceID= String.valueOf(race_id);
	                
	                
	                Document document = new Document();
	                document.append("command", command);
	                document.append("car_num", carNumber);
	                document.append("section_identifier",section_identifier );
	                document.append("elapsed_time",elapsed_time );
	                document.append("last_section_time",last_section_time);
	                document.append("last_lap", Integer.parseInt(last_lap,16));
	                document.append("event_name",event_name);
	                document.append("run_command", run_command);
	                document.append("race_id", RaceID);
	                document.append("unrelated_counter", counter_$S);
	                counter_$S=counter_$S+1;
	                
	                

	                if (docs_CompletedSection.size() == 100) {
	                	CompletedSection_$S.insertMany(docs_CompletedSection);
	                    docs_CompletedSection.clear();
	                } else {
	                	docs_CompletedSection.add(document);
	                }
	            }
	            
	            else if(line.startsWith("$T")) { //Track records facing issue, because I am inserting multiple track records for the same race.
	            	String[] splits = line.split("¦");
	            	String command=splits[0];
	                String track_name = splits[4];
	                String venue = splits[5];
	                String track_length = splits[6];
	                String number_of_sections = splits[7];
	                String RaceID= String.valueOf(race_id);
	                
	                List<Document> embed_document = new ArrayList<Document>();
	 
	                //---
	                Document document = new Document();
	                document.append("command", command);
	                document.append("track_name", track_name );
	                document.append("venue",venue );
	                document.append("track_length",track_length );
	                document.append("number_of_sections",number_of_sections);
	                 
	                document.append("race_id", RaceID);
	                //--
	                
	                for(int m=8;m<splits.length;m=m+4) {
	                	Document sub_doc= new Document();
	                	sub_doc.append("section_name", splits[m]);
	                	sub_doc.append("section_length", splits[m+1]);
	                	sub_doc.append("section_start", splits[m+2]);
	                	sub_doc.append("section_end", splits[m+3]);
	                	embed_document.add(sub_doc);	                	
	                }
	                document.append("section_information", embed_document);
	                
	                
	                

	                if (docs_Trackinfo.size() == 100) {
	                	TrackInfo_$T.insertMany(docs_Trackinfo);
	                    docs_Trackinfo.clear();
	                } else {
	                	docs_Trackinfo.add(document);
	                } 
	            }	
	            else if(line.startsWith("$O")) {
	            	
	            	String[] splits = line.split("¦");
	            	String command=splits[0];  //1,2,3,4 are type,sequence,preamble,resultID are missing
	                String Deleted = splits[5];
	                String Marker = splits[6];
	                String Rank = splits[7];
	                String overall_rank = splits[8];
	                String start_position=splits[9];
	                String best_lap_time=splits[10];
	                String best_lap=splits[11];
	                String last_lap_time=splits[12];
	                String laps=splits[13];
	                String total_time=splits[14];
	                String last_warm_up_qual_time=splits[15];
	                String lap1_QualTime=splits[16];
	                String lap2_QualTime=splits[17];
	                String lap3_QualTime=splits[18];
	                String lap4_QualTime=splits[19];
	                String total_QualTime=splits[20];
	                String status=splits[21];
	                String diff=splits[22];
	                String gap=splits[23];
	                String on_track=splits[24];
	                String pit_stops=splits[25];
	                String last_pit_lap=splits[26];
	                String since_pit_lap=splits[27];
	                String flag_status=splits[28];
	                String no=splits[29];
	                String first_name=splits[30];
	                String last_name=splits[31];
	                String Class=splits[32];
	                String equipment=splits[33];
	                String license=splits[34];
	                String team=splits[35];
	                String total_entrant_points=splits[36];
	                String total_driver_points=splits[37];
	                String comment=splits[38];
	                String total_chasis_points=splits[39];
	                String total_engine_points=splits[40];
	                String off_track=splits[41];
	                String non_tow_rank=splits[42];
	                String non_tow_lap=splits[43];
	                String non_tow_time=splits[44];
	                String on_track_passes=splits[45];
	                String on_track_times_passed=splits[46];
	                String overtake_remaining=splits[47];
	                String tire_type=splits[48];
	                String driver_id=splits[49];
	                String qualifying_speed=splits[50];	                
	                String RaceID= String.valueOf(race_id);
	                
	                
	 
	                //---
	                Document document = new Document();
	                document.append("command", command);
	                document.append("Deleted",Deleted);
	                document.append("Marker",Marker);
	                document.append("Rank",Integer.parseInt(Rank,16));
	                document.append("overall_rank",Integer.parseInt(overall_rank,16));
	                document.append("start_position",start_position);
	                document.append("best_lap_time",best_lap_time);
	                document.append("best_lap",best_lap);
	                document.append("last_lap_time",last_lap_time);
	                document.append("laps",laps);
	                document.append("total_time",total_time);
	                document.append("last_warm_up_qual_time",last_warm_up_qual_time);
	                document.append("lap1_QualTime",lap1_QualTime);
	                document.append("lap2_QualTime",lap2_QualTime);
	                document.append("lap3_QualTime",lap3_QualTime);
	                document.append("lap4_QualTime",lap4_QualTime);
	                document.append("total_QualTime",total_QualTime);
	                document.append("status",status);
	                document.append("diff",diff);
	                document.append("gap",gap);
	                document.append("on_track",on_track);
	                document.append("pit_stops",pit_stops);
	                document.append("last_pit_lap",last_pit_lap);
	                document.append("since_pit_lap",since_pit_lap);
	                document.append("flag_status",flag_status);
	                document.append("no",no);
	                document.append("first_name",first_name);
	                document.append("last_name",last_name);
	                document.append("Class",Class);
	                document.append("equipment",equipment);
	                document.append("license",license);
	                document.append("team",team);
	                document.append("total_entrant_points",total_entrant_points);
	                document.append("total_driver_points",total_driver_points);
	                document.append("comment",comment);
	                document.append("total_chasis_points",total_chasis_points);
	                document.append("total_engine_points",total_engine_points);
	                document.append("off_track",off_track);
	                document.append("non_tow_rank",non_tow_rank);
	                document.append("non_tow_lap",non_tow_lap);
	                document.append("non_tow_time",non_tow_time);
	                document.append("on_track_passes",on_track_passes);
	                document.append("on_track_times_passed",on_track_times_passed);
	                document.append("overtake_remaining",overtake_remaining);
	                document.append("tire_type",tire_type);
	                document.append("driver_id",driver_id);
	                document.append("qualifying_speed",qualifying_speed);
	                document.append("race_id", RaceID);
	                //counter_$O=counter_$O+1;
	                //document.append("counter_overall_results", counter_$O);
	                
	                //--
	                

	                if (docs_OverallResults.size() == 100) {
	                	OverallResults_$O.insertMany(docs_OverallResults);
	                	docs_OverallResults.clear();
	                } else {
	                	docs_OverallResults.add(document);
	                } 
	            }
	            
	            else if(line.startsWith("$R")) {
	            	
	            	
	            	
	            	
	            	String[] splits = line.split("¦");
	            	String command=splits[0];
	                event_name = splits[4];
	                String event_round = splits[5];
	                String run_name = splits[6];
	                run_command = splits[7];
	                String start_time_date = splits[8];
	                String RaceID= String.valueOf(race_id);
	                
	                
	                Document document = new Document();
	                document.append("command", command);
	                document.append("event_name", event_name);
	                document.append("event_round", event_round);
	                document.append("run_name", run_name);
	                document.append("run_command", run_command);
	                document.append("start_time_date", start_time_date);
	                document.append("race_id", RaceID);
	                
	                

	                if (docs_RunInfo.size() == 100) {
	                	RunInfo_$R.insertMany(docs_RunInfo);
	                	docs_RunInfo.clear();
	                } else {
	                	docs_RunInfo.add(document);
	                }
	            }
	            else if(line.startsWith("$F")) {
	            	String[] splits = line.split("¦");
	            	String command=splits[0];
	                String track_status = splits[4];
	                String lap_number = splits[5];
	                String green_time = splits[6];
	                String green_laps = splits[7];
	                String yellow_time = splits[8];
	                String yellow_laps = splits[9];
	                String red_time = splits[10];
	                String number_of_yellows = splits[11];
	                String current_leader = splits[12];
	                String number_of_lead_changes = splits[13];
	                String average_race_speed = splits[14];
	                String RaceID= String.valueOf(race_id);
	                
	                
	                Document document = new Document();
	                document.append("command", command);
	                document.append("track_status", track_status);
	                document.append("lap_number", lap_number);
	                document.append("green_time", green_time);
	                document.append("green_laps", green_laps);
	                document.append("yellow_time", yellow_time);
	                document.append("yellow_laps",yellow_laps);
	                document.append("red_time",red_time);
	                document.append("number_of_yellows",number_of_yellows);
	                document.append("current_leader",current_leader);
	                document.append("number_of_lead_changes",number_of_lead_changes);
	                document.append("average_race_speed",average_race_speed);
	                document.append("race_id", RaceID);
	                //----changed
	                document.append("counter_$F", counter_$F);
	                counter_$F=counter_$F+1;
	                
	                if(run_command.equals("R") && (!flag_from_$h.contentEquals("U"))&&(!flag_from_$h.contentEquals("C"))&&(!flag_from_$h.contentEquals("not yet initialized"))) {
	                	document.append("timeOfDay",timeOfDay.substring(0,timeOfDay.length()-3));
	                }
	                // change ended

	                if (docs_FlagInfo.size() == 100) {
	                	FlagInfo_$F.insertMany(docs_FlagInfo);
	                	docs_FlagInfo.clear();
	                } else {
	                	docs_FlagInfo.add(document);
	                }
	            }
	            
	            else if(line.startsWith("$E")) {
	            	String[] splits = line.split("¦");
	            	String command=splits[0];
	                String car_number = splits[4];
	                String unique_identifier = splits[5];
	                String driver_name = splits[6];
	                String start_position = splits[7];
	                String field_count = splits[8];
	                String Class = splits[9];
	                String driver_id = splits[10];
	                String transponder_control = splits[11];
	                String equipment = splits[12];
	                String license = splits[13];
	                String team = splits[14];
	                String team_id = splits[15];
	                String engine = splits[16];
	                String entrant_id = splits[17];
	                String points_eligible = splits[19]; //documentation given is wrong, confirm.
	                String hometown = splits[18];
	                String competitor_id = splits[20];
	                
	                String RaceID= String.valueOf(race_id);
	                
	                
	                Document document = new Document();
	                document.append("command", command);
	                document.append("car_number", car_number);
	                document.append("unique_identifier", unique_identifier);
	                document.append("driver_name", driver_name);
	                document.append("start_position", start_position);
	                document.append("field_count", field_count);
	                document.append("Class",Class);
	                document.append("driver_id",driver_id);
	                document.append("transponder_control",transponder_control);
	                document.append("equipment",equipment);
	                document.append("license",license);
	                document.append("team",team);
	                document.append("team_id",team_id);
	                document.append("engine",engine);
	                document.append("entrant_id",entrant_id);
	                document.append("points_eligible",points_eligible);
	                document.append("hometown",hometown);
	                document.append("competitor_id",competitor_id);
	                
	                document.append("race_id", RaceID);
	                
	                
	                
	                
	                if (docs_EntryInfo.size() == 100) {
	                	try{EntryInfo_$E.insertMany(docs_EntryInfo);}
	                	catch(Exception e) {System.out.println(e);}
	                	docs_EntryInfo.clear();
	                } else {
	                	docs_EntryInfo.add(document);
	                }
	            }
	            
	            else if(line.startsWith("$H")) {
	            	String[] splits = line.split("¦");
	            	flag_from_$h=splits[4];
	            }
	            	
	            line = br.readLine();
	        }
	        
	        Telemetry_$P.insertMany(docs_Telemetry);
	        CompletedLaps_$C.insertMany(docs_CompletedLaps);
	        CompletedSection_$S.insertMany(docs_CompletedSection);
	        TrackInfo_$T.insertMany(docs_Trackinfo);
	        OverallResults_$O.insertMany(docs_OverallResults);
	        RunInfo_$R.insertMany(docs_RunInfo);
	        FlagInfo_$F.insertMany(docs_FlagInfo);
	        EntryInfo_$E.insertMany(docs_EntryInfo);
	        
	        br.close();
	        System.out.println("Finished Entering Records");
	}
	
	 public static ArrayList<String> getAll() {   //driver
		 
		 	MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        //System.out.println("Created successDocument");
	        
	        
	        MongoCollection<Document> EntryInfo_$E= database.getCollection("EntryInfo");
	        
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
	        
	        
	        AggregateIterable<Document> output=EntryInfo_$E.aggregate(Arrays.asList(
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
	        
	        
	        
	        /*
	        MongoCursor<String> files=EntryInfo_$E.distinct("driver_name", String.class).filter(doc5).iterator();
	        ArrayList<String> result = new ArrayList<String>();
	        
	        while(files.hasNext()) {
	            result.add(files.next());
	          }
	        */
		 return result;
	 }
	 
	 public static ArrayList<String> getDriversByRace(int race_id) { //driver
		 
		 	MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        //System.out.println("Created successDocument");
	        
	        
	        MongoCollection<Document> EntryInfo_$E= database.getCollection("EntryInfo");
	        
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
	        //Document doc7=new Document();
	        //Document doc8=new Document();
	        
	        doc7.append("driver_name","$driver_name");
	        doc7.append("driver_id","$driver_id");
	        
	        
	        AggregateIterable<Document> output=EntryInfo_$E.aggregate(Arrays.asList(
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
	        
	        /*
	        MongoCursor<String> files=EntryInfo_$E.distinct("driver_name", String.class).filter(doc5).iterator();
	        ArrayList<String> result = new ArrayList<String>();
	        while(files.hasNext()) {
	            result.add(files.next());
	          }*/
	        return result;
	        
	 }
	 
	 
public ArrayList<String> getdriverbyID(String driver_id){
		 
		 MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        //System.out.println("Created successDocument");
	        
	        
	        MongoCollection<Document> EntryInfo_$E= database.getCollection("EntryInfo");
	        
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
	        
	        AggregateIterable<Document> output=EntryInfo_$E.aggregate(Arrays.asList(
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
	 
	 public static ArrayList<String> search(String check_str) { //driver
		 
		 	MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        //System.out.println("Created successDocument");
	        
	        
	        MongoCollection<Document> EntryInfo_$E= database.getCollection("EntryInfo");
	        
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
	        
	        MongoCursor<String> files=EntryInfo_$E.distinct("driver_name", String.class).filter(doc5).iterator();
	        
	        ArrayList<String> result = new ArrayList<String>();
	        
	        while(files.hasNext()) {
	            result.add(files.next());
	          }
	        return result;
	 }
	 
	 
	 public static ArrayList<ArrayList<String>> getlapdata(int race_id,String car) { //driver
		 
		 	MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        //System.out.println("Created successDocument");
	        
	        MongoCollection<Document> CompletedLaps_$C= database.getCollection("CompletedLaps"); 
	        
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
	        
	        
	        
	        AggregateIterable<Document> output=CompletedLaps_$C.aggregate(Arrays.asList(
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
	        
	        MongoCollection<Document> CompletedSection_$S= database.getCollection("CompletedSection");
	        
	        AggregateIterable<Document> output_2=CompletedSection_$S.aggregate(Arrays.asList(
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
	 
	 
	 public static ArrayList<String> getAll_Track() {  //track
		 	MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        
	        MongoCollection<Document> TrackInfo_$T= database.getCollection("TrackInfo");
	        
	        MongoCursor<String> files=TrackInfo_$T.distinct("track_name", String.class).iterator();
	        
	        
	        ArrayList<String> result = new ArrayList<String>();
	        
	        while(files.hasNext()) {
	             result.add(files.next());
	          }
	        
	        return result;
	        
	 }
	 
	 public static ArrayList<String> getInfo_Track(String Track_name) {  //track
		 
		 
		    MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        
	        MongoCollection<Document> TrackInfo_$T= database.getCollection("TrackInfo");
	        
	        
	        
	        Document doc1=new Document();
	        Document doc2=new Document();
	        
	        doc2.append("track_name", Track_name);
	        
	        doc1.append("track_name", "$track_name");
	        doc1.append("venue", "$venue");
	        doc1.append("number_of_sections", "$number_of_sections");
	        doc1.append("race_id", "$race_id");
	        doc1.append("section_information","$section_information");
	        
	        
	        AggregateIterable<Document> output= TrackInfo_$T.aggregate(Arrays.asList(
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
	 
	 public static ArrayList<String> getAll_Race() { //Race
		 
		 MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	     System.out.println("Connected to the database successfully");

	     MongoDatabase database = mongo.getDatabase("Sahaj");
	        
	     MongoCollection<Document> RunInfo_$R= database.getCollection("RunInfo");
	     
	     Document doc1=new Document();
	     doc1.append("run_command","R");
	     Document doc2=new Document();
	     
	     doc2.append("event_name", "$event_name");
	     doc2.append("race_id", "$race_id");
	     
	     AggregateIterable<Document> output_2=RunInfo_$R.aggregate(Arrays.asList(
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
	 
	 
	 public static ArrayList<String> getrace(int race_id) { //race
		 MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	     System.out.println("Connected to the database successfully");

	     MongoDatabase database = mongo.getDatabase("Sahaj");
	        
	     MongoCollection<Document> RunInfo_$R= database.getCollection("RunInfo");
	     
	     Document doc1=new Document();
	     doc1.append("run_command","R");
	     doc1.append("race_id", String.valueOf(race_id));
	     Document doc2=new Document();
	     
	     doc2.append("event_name", "$event_name");
	     doc2.append("event_round", "$event_round");
	     doc2.append("start_time_date", "$start_time_date");
	     doc2.append("race_id", "$race_id");
	     
	     AggregateIterable<Document> output_2=RunInfo_$R.aggregate(Arrays.asList(
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
	 
	 public static ArrayList<String> getflags_race(int race_id) { //race
		 
		 MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        //System.out.println("Created successDocument");
	        
	        
	        MongoCollection<Document> FlagInfo_$F= database.getCollection("FlagInfo");
	        
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
	        
	        
	        
	        AggregateIterable<Document> output_2=FlagInfo_$F.aggregate(Arrays.asList(
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
	 
	 public static ArrayList<String> getranks(int race_id) { //race
		 MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	     System.out.println("Connected to the database successfully");

	     MongoDatabase database = mongo.getDatabase("Sahaj");
	        
	     MongoCollection<Document> OverallResults_$O= database.getCollection("OverallResults");
	     
	     Document doc1=new Document();
	     
	     doc1.append("race_id", String.valueOf(race_id));
	     doc1.append("flag_status","Finish");
	     Document doc2=new Document();
	     
	     doc2.append("overall_rank","$overall_rank");
	     doc2.append("no","$no");
	     doc2.append("first_name","$first_name");
	     doc2.append("last_name","$last_name");
	     
	     AggregateIterable<Document> output_2=OverallResults_$O.aggregate(Arrays.asList(
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
	 


	 public static ArrayList<String> getsnapshot_for_car_trial(int hours,int minutes, double seconds,int race_id,double timer,String car_num) { //race
		 	MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        //System.out.println("Created successDocument");
	        MongoCollection<Document> Telemetry_$P= database.getCollection("telemetry");
	        
	        int prev_hours;
	        int prev_minutes;
	        double prev_seconds;
	        int flagger=0;
	        
	        
	        
	        if(seconds-timer<0) {
	        	flagger=1;
	        	prev_seconds=60+(seconds-timer);
	        	if(minutes-1<0) {
	        		prev_minutes=60+(minutes-1);
	        		if(hours-1<0) {
	        			prev_hours=24+(hours-1);
	        		}
	        		else {
	        			prev_hours=hours-1;
	        		}
	        	}
	        	else {
	        		prev_minutes=minutes-1;
	        		prev_hours=hours;
	        	}
	        	
	        }
	        else {
	        	prev_seconds=seconds-timer;
	        	prev_minutes=minutes;
	        	prev_hours=hours;
	        }
	        
	        ArrayList<String> result = new ArrayList<String>();
	        
	        if(flagger==0) {
	        	
	        	Document main_doc=new Document();
	        	Document doc1= new Document();
	        	Document doc2= new Document();
	        	Document doc3= new Document();
	        	Document doc4= new Document();
	        	Document doc5= new Document();
	        	Document doc6= new Document();
	        	Document doc7= new Document();
	        	Document doc8= new Document();
	        	Document doc9= new Document();
	        	Document doc10= new Document();
	        	Document doc11= new Document();
	        	Document doc12= new Document();
	        	
	        	doc1.append("$gte", hours);
	        	doc2.append("hours", doc1);
	        	
	        	doc3.append("$lte", hours);
	        	doc4.append("hours", doc3);
	        	
	        	doc5.append("$gte", minutes);
	        	doc6.append("minutes", doc5);
	        	
	        	doc7.append("$lte", minutes);
	        	doc8.append("minutes", doc7);
	        	
	        	doc9.append("$gte", prev_seconds);
	        	doc10.append("seconds", doc9);

	        	doc11.append("$lte", seconds);
	        	doc12.append("seconds", doc11);
	        	
	        	main_doc.append("race_id", String.valueOf(race_id));
	        	main_doc.append("car_num", car_num);
	        	main_doc.append("time_for_comparison",1);
	        	main_doc.append("$and", Arrays.asList(doc2,doc4,doc6,doc8,doc10,doc12));
	        	
	        	FindIterable<Document> output_2=Telemetry_$P.find(main_doc);
	        	        
	         String c;
	   	     String[] d;
	   	     
	   	     
	   	        for (Document dbObject : output_2)
	   	        {
	   	        	c=dbObject.toJson();
	   	        	c=c.substring(12, c.length()-3);
	   	            d=c.split(",");
	   	            //System.out.println(Arrays.toString(d));
	   	           result.add(Arrays.toString(d));
	   	        }
	        	
	        	
	        }
	        
	        
	        else if(flagger==1) {
	        	System.out.println("Inside 1");
	        	
	        	Document main_doc=new Document();
	        	Document doc1= new Document();
	        	Document doc2= new Document();
	        	Document doc3= new Document();
	        	Document doc4= new Document();
	        	Document doc5= new Document();
	        	Document doc6= new Document();
	        	Document doc7= new Document();
	        	Document doc8= new Document();
	        	Document doc9= new Document();
	        	Document doc10= new Document();
	        	Document doc11= new Document();
	        	Document doc12= new Document();
	        	
	        	doc1.append("$gte", hours);
	        	doc2.append("hours", doc1);
	        	
	        	doc3.append("$lte", hours);
	        	doc4.append("hours", doc3);
	        	
	        	doc5.append("$gte", minutes);
	        	doc6.append("minutes", doc5);
	        	
	        	doc7.append("$lte", minutes);
	        	doc8.append("minutes", doc7);
	        	
	        	doc9.append("$gte", 0);
	        	doc10.append("seconds", doc9);

	        	doc11.append("$lte", seconds);
	        	doc12.append("seconds", doc11);
	        	
	        	main_doc.append("race_id", String.valueOf(race_id));
	        	main_doc.append("car_num", car_num);
	        	main_doc.append("time_for_comparison",1);
	        	
	        	main_doc.append("$and", Arrays.asList(doc2,doc4,doc6,doc8,doc10,doc12));
	        	
	        	FindIterable<Document> output_2=Telemetry_$P.find(main_doc);
	        	        

	   	     
	   	     
	   	     //------------part for previous--------------
	   	     
	   	     Document main_doc_prev=new Document();
	   	     Document doc1_prev= new Document();
	   	     Document doc2_prev= new Document();
   		 Document doc3_prev= new Document();
   		 Document doc4_prev= new Document();
   		 Document doc5_prev= new Document();
   		 Document doc6_prev= new Document();
   		 Document doc7_prev= new Document();
   		 Document doc8_prev= new Document();
   		 Document doc9_prev= new Document();
   		 Document doc10_prev= new Document();
   		 Document doc11_prev= new Document();
   		 Document doc12_prev= new Document();
   	
	      	doc1_prev.append("$gte", prev_hours);
	      	doc2_prev.append("hours", doc1_prev);
	      	
	      	doc3_prev.append("$lte", prev_hours);
	      	doc4_prev.append("hours", doc3_prev);
	      	
	      	doc5_prev.append("$gte", prev_minutes);
	      	doc6_prev.append("minutes", doc5_prev);
	      	
	      	doc7_prev.append("$lte", prev_minutes);
	      	doc8_prev.append("minutes", doc7_prev);
	      	
	      	doc9_prev.append("$gte", prev_seconds);
	      	doc10_prev.append("seconds", doc9_prev);
	
	      	doc11_prev.append("$lte", 60);
	      	doc12_prev.append("seconds", doc11_prev);
	      	
	      	main_doc_prev.append("race_id", String.valueOf(race_id));
	      	main_doc_prev.append("car_num", car_num);
	      	main_doc_prev.append("time_for_comparison",1);
	      	
	      	main_doc_prev.append("$and", Arrays.asList(doc2_prev,doc4_prev,doc6_prev,doc8_prev,doc10_prev,doc12_prev));
	      	
	      	FindIterable<Document> output_1=Telemetry_$P.find(main_doc_prev);
   	        
	        
	      	 String e;
		 	 String[] f;
		   	     
		   	     
		   	     
		   	        for (Document dbObject : output_1)
		   	        {
		   	        	e=dbObject.toJson();
		   	        	e=e.substring(12, e.length()-3);
		   	            f=e.split(",");
		   	            //System.out.println(Arrays.toString(f));
		   	           result.add(Arrays.toString(f));
		   	        }
	      	
	      	
	      	String c;
	 	    String[] d;
	   	     
	   	     
	   	     
	   	        for (Document dbObject : output_2)
	   	        {
	   	        	c=dbObject.toJson();
	   	        	c=c.substring(12, c.length()-3);
	   	            d=c.split(",");
	   	            //System.out.println(Arrays.toString(d));
	   	           result.add(Arrays.toString(d));
	   	        }
	        	
	        	
	        }
	        
	        //System.out.println("hello");
	        return result;

	        
	 }
	 
	 
	 
	 public static ArrayList<String> getsnapshot_trial(int hours,int minutes, double seconds,int race_id,double timer) { //race
		 	MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        //System.out.println("Created successDocument");
	        MongoCollection<Document> Telemetry_$P= database.getCollection("telemetry");
	        
	        int prev_hours;
	        int prev_minutes;
	        double prev_seconds;
	        int flagger=0;
	        
	        
	        
	        if(seconds-timer<0) {
	        	flagger=1;
	        	prev_seconds=60+(seconds-timer);
	        	if(minutes-1<0) {
	        		prev_minutes=60+(minutes-1);
	        		if(hours-1<0) {
	        			prev_hours=24+(hours-1);
	        		}
	        		else {
	        			prev_hours=hours-1;
	        		}
	        	}
	        	else {
	        		prev_minutes=minutes-1;
	        		prev_hours=hours;
	        	}
	        	
	        }
	        else {
	        	prev_seconds=seconds-timer;
	        	prev_minutes=minutes;
	        	prev_hours=hours;
	        }
	        
	        ArrayList<String> result = new ArrayList<String>();
	        
	        if(flagger==0) {
	        	
	        	Document main_doc=new Document();
	        	Document doc1= new Document();
	        	Document doc2= new Document();
	        	Document doc3= new Document();
	        	Document doc4= new Document();
	        	Document doc5= new Document();
	        	Document doc6= new Document();
	        	Document doc7= new Document();
	        	Document doc8= new Document();
	        	Document doc9= new Document();
	        	Document doc10= new Document();
	        	Document doc11= new Document();
	        	Document doc12= new Document();
	        	
	        	doc1.append("$gte", hours);
	        	doc2.append("hours", doc1);
	        	
	        	doc3.append("$lte", hours);
	        	doc4.append("hours", doc3);
	        	
	        	doc5.append("$gte", minutes);
	        	doc6.append("minutes", doc5);
	        	
	        	doc7.append("$lte", minutes);
	        	doc8.append("minutes", doc7);
	        	
	        	doc9.append("$gte", prev_seconds);
	        	doc10.append("seconds", doc9);

	        	doc11.append("$lte", seconds);
	        	doc12.append("seconds", doc11);
	        	
	        	main_doc.append("race_id", String.valueOf(race_id));
	        	
	        	main_doc.append("time_for_comparison",1);
	        	main_doc.append("$and", Arrays.asList(doc2,doc4,doc6,doc8,doc10,doc12));
	        	
	        	FindIterable<Document> output_2=Telemetry_$P.find(main_doc);
	        	        
	         String c;
	   	     String[] d;
	   	     
	   	     
	   	        for (Document dbObject : output_2)
	   	        {
	   	        	c=dbObject.toJson();
	   	        	c=c.substring(12, c.length()-3);
	   	            d=c.split(",");
	   	            //System.out.println(Arrays.toString(d));
	   	           result.add(Arrays.toString(d));
	   	        }
	        	
	        	
	        }
	        
	        
	        else if(flagger==1) {
	        	
	        	Document main_doc=new Document();
	        	Document doc1= new Document();
	        	Document doc2= new Document();
	        	Document doc3= new Document();
	        	Document doc4= new Document();
	        	Document doc5= new Document();
	        	Document doc6= new Document();
	        	Document doc7= new Document();
	        	Document doc8= new Document();
	        	Document doc9= new Document();
	        	Document doc10= new Document();
	        	Document doc11= new Document();
	        	Document doc12= new Document();
	        	
	        	doc1.append("$gte", hours);
	        	doc2.append("hours", doc1);
	        	
	        	doc3.append("$lte", hours);
	        	doc4.append("hours", doc3);
	        	
	        	doc5.append("$gte", minutes);
	        	doc6.append("minutes", doc5);
	        	
	        	doc7.append("$lte", minutes);
	        	doc8.append("minutes", doc7);
	        	
	        	doc9.append("$gte", 0);
	        	doc10.append("seconds", doc9);

	        	doc11.append("$lte", seconds);
	        	doc12.append("seconds", doc11);
	        	
	        	main_doc.append("race_id", String.valueOf(race_id));
	        	
	        	main_doc.append("time_for_comparison",1);
	        	
	        	main_doc.append("$and", Arrays.asList(doc2,doc4,doc6,doc8,doc10,doc12));
	        	
	        	FindIterable<Document> output_2=Telemetry_$P.find(main_doc);
	        	        

	   	     
	   	     
	   	     //------------part for previous--------------
	   	     
	   	     Document main_doc_prev=new Document();
	   	     Document doc1_prev= new Document();
	   	     Document doc2_prev= new Document();
		 Document doc3_prev= new Document();
		 Document doc4_prev= new Document();
		 Document doc5_prev= new Document();
		 Document doc6_prev= new Document();
		 Document doc7_prev= new Document();
		 Document doc8_prev= new Document();
		 Document doc9_prev= new Document();
		 Document doc10_prev= new Document();
		 Document doc11_prev= new Document();
		 Document doc12_prev= new Document();
	
	      	doc1_prev.append("$gte", prev_hours);
	      	doc2_prev.append("hours", doc1_prev);
	      	
	      	doc3_prev.append("$lte", prev_hours);
	      	doc4_prev.append("hours", doc3_prev);
	      	
	      	doc5_prev.append("$gte", prev_minutes);
	      	doc6_prev.append("minutes", doc5_prev);
	      	
	      	doc7_prev.append("$lte", prev_minutes);
	      	doc8_prev.append("minutes", doc7_prev);
	      	
	      	doc9_prev.append("$gte", prev_seconds);
	      	doc10_prev.append("seconds", doc9_prev);
	
	      	doc11_prev.append("$lte", 60);
	      	doc12_prev.append("seconds", doc11_prev);
	      	
	      	main_doc_prev.append("race_id", String.valueOf(race_id));
	      	
	      	main_doc_prev.append("time_for_comparison",1);
	      	
	      	main_doc_prev.append("$and", Arrays.asList(doc2_prev,doc4_prev,doc6_prev,doc8_prev,doc10_prev,doc12_prev));
	      	
	      	FindIterable<Document> output_1=Telemetry_$P.find(main_doc_prev);
	        
	        
	      	 String e;
		 	 String[] f;
		   	     
		   	     
		   	     
		   	        for (Document dbObject : output_1)
		   	        {
		   	        	e=dbObject.toJson();
		   	        	e=e.substring(12, e.length()-3);
		   	            f=e.split(",");
		   	            //System.out.println(Arrays.toString(f));
		   	           result.add(Arrays.toString(f));
		   	        }
	      	
	      	
	      	String c;
	 	    String[] d;
	   	     
	   	     
	   	     
	   	        for (Document dbObject : output_2)
	   	        {
	   	        	c=dbObject.toJson();
	   	        	c=c.substring(12, c.length()-3);
	   	            d=c.split(",");
	   	            //System.out.println(Arrays.toString(d));
	   	           result.add(Arrays.toString(d));
	   	        }
	        	
	        	
	        }
	        
	        //System.out.println("hello");
	        return result;

	        
	 }
	 
	 
	 public static ArrayList<String> get_nearest_car(String car_num, double time_in_seconds, int number_of_car_records,int race_id){
		 	MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        //System.out.println("Created successDocument");
	        MongoCollection<Document> Telemetry_$P= database.getCollection("telemetry");
	        
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
	        
	        
	        FindIterable<Document> output_2=Telemetry_$P.find(doc2).limit(number_of_car_records);
	        
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
	        
	 
	 
	 
	 public static ArrayList<String> getsnapshot_final(int hours,int minutes, double seconds,int race_id,int threshold_in_seconds){
		 	MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	        System.out.println("Connected to the database successfully");

	        MongoDatabase database = mongo.getDatabase("Sahaj");
	        //System.out.println("Created successDocument");
	        MongoCollection<Document> Telemetry_$P= database.getCollection("telemetry");
	        MongoCollection<Document> EntryInfo_$E= database.getCollection("EntryInfo");
	        
	        
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
	        
	        MongoCursor<String> files=EntryInfo_$E.distinct("car_number", String.class).filter(doc2).iterator();
	        
	        
	        ArrayList<String> car_numbers_list = new ArrayList<String>();
	        
	        while(files.hasNext()) {
	        	car_numbers_list.add(files.next());
	          }
	        
	        String a;
	        ArrayList<String> car_details = new ArrayList<String>();
	        //ArrayList<String> car_details=new ArrayList<String>(); 
	        for(String data : car_numbers_list) {
	   		    //System.out.println(get_nearest_car(data,time_in_seconds,1,race_id).getClass());
	        
	        	a=get_nearest_car(data,time_in_seconds,1,race_id).get(0);
	        	car_details.add(a);
	        	//System.out.println("1"+car_details);
	   	 }
	        
	       // car_details.add(car_numbers_list);
	        //System.out.println("2"+car_details);
	        
	        return car_details;
	 }
	 
	 
	 
	 
}