import storm
import pandas as pd
import numpy as np
import time



class IndyCarSpout(storm.Spout):
    # Not much to do here for such a basic spout
    def initialize(self, conf, context):
        self._conf = conf
        self._context = context

        storm.logInfo("Spout instance starting...")
        
        #############################################################################################
        ## change 'sakkas' with your username
        self.data = pd.read_csv('/N/u/sakkas/Lab6/indycar-speed-timeseries.csv')
        #############################################################################################
        
        # top 10 cars
        self.data = self.data[['car_12', 'car_20', 'car_9', 'car_27', 'car_28', 'car_22', 'car_29',
             'car_1', 'car_6', 'car_15']]
        self.myindex = 0

    # Process the next tuple
    def nextTuple(self):
        
        race_time = self.myindex # one record per second
        
        
        # I put sleep to simulate the event. It will be 3x faster than the normal race
        # We need at least 80 seconds data to start anomaly detection. I don't want to wait for first 80 seconds
        if race_time > 80:
            time.sleep(0.35)
        
        row_data = self.data.loc[self.myindex].values

        # We can only emit a list. List inside list is not allowed
        # Example emit_data: ['word', 9, 74.08, 75.02, 73.76, 77.67, 81.24, 74.63, 76.59, 74.61, 72.88, 71.91]
        # the text 'word' does not matter. If there are multiple multiple bolts to take the text, it will be equally splitted to bolts based on the text
        # for instance: 'apple' to bolt_instance1, 'banana' to bolt_instance2
        # I want them to emitted to a single bolt. Otherwise, I have to consider data order.
        emit_data = ["word"] 
        emit_data.append(race_time)
        emit_data = emit_data + row_data.tolist()

        self.myindex += 1
        
        # start from the beginning if race ends
        if self.myindex == len(self.data):
            self.myindex = 0
        
        storm.logInfo("Emiting %s" % str(emit_data))
        storm.emit(emit_data)

        
        

# Start the spout when it's invoked
IndyCarSpout().run()
