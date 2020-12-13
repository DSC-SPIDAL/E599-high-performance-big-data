import storm
import pandas as pd
import numpy as np
import time

import sys


class IndyCarSpout(storm.Spout):
    # Not much to do here for such a basic spout
    def initialize(self, conf, context):
        self._conf = conf
        self._context = context
        self.car_number1 = 12
        self.car_number2 = 20
        self.lap = 0

        storm.logInfo("Spout instance starting...")
        
        #############################################################################################
        ## change 'sakkas' with your username
        self.data = pd.read_csv('~/Storm/cleaned_completed_laps.csv')
        #############################################################################################
        
        self.data1 = self.data[self.data.car_number == self.car_number1]
#         self.data2 = self.data[self.data.car_number == self.car_number2]
        

    # Process the next tuple
    def nextTuple(self):
        
        storm.logInfo("Lapppp %s" % (self.lap))
                
        row_data = list(self.data1.iloc[self.lap].values)
#         row_data2 = list(self.data2.iloc[self.lap].values)
        
#         last_lap_time = row_data[3]
#         track_status = row_data[4]
#         pit_stop_count = row_data[5]
        
        ## simulation speed up by 20 times
#         time.sleep(last_lap_time // 20)

#         emit_data = [[self.car_number1, self.lap, row_data1], [self.car_number2, self.lap, row_data2]]
        emit_data = [self.car_number1, self.lap, row_data]
        self.lap += 1
        
        # start from the beginning if race ends
        if self.lap == 201:
            self.lap = 0
        
        storm.logInfo("Emiting %s" % str(emit_data))
        storm.emit(emit_data)

        
        

# Start the spout when it's invoked
IndyCarSpout().run()
