import storm
import numpy as np
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
import glob

class InferenceBolt(storm.BasicBolt):
    # There's nothing to initialize here,
    # since this is just a split and emit
    # Initialize this instance
    def initialize(self, conf, context):
        self._conf = conf
        self._context = context
        storm.logInfo("Inference bolt instance starting...")
        self.overall_rank = []
        self.last_laptime = []
        self.track_status = []
        self.pit_stop_count = []
        self.completed_laps = []
        self.elapsed_time = []
        self.best_laptime = []
        self.time_behind_leader = []
        self.time_behind_prec = []
        self.overall_best_laptime = []
        self.last_pitted_lap = []
        self.start_position = []
        self.laps_led = []
        self.best_lap = []
        self.laps_behind_leader = []
        self.laps_behind_prec = []
        self.time_step = 10
        
        #############################################################################################
        ## change 'sakkas' with your username
        self.model = tf.keras.models.load_model('/N/u/skaminen/Lab7/lap_time.prediction.h5')
        #############################################################################################
    def process(self, tup):
        
        car_number = tup.values[0][0]
        lap = tup.values[0][1]
        row_data = tup.values[0][2]
        
        # restart the event
        if lap == 0:
            self.overall_rank = []
            self.last_laptime = []
            self.track_status = []
            self.pit_stop_count = []
            self.completed_laps = []
            self.elapsed_time = []
            self.best_laptime = []
            self.time_behind_leader = []
            self.time_behind_prec = []
            self.overall_best_laptime = []
            self.last_pitted_lap = []
            self.start_position = []
            self.laps_led = []
            self.best_lap = []
            self.laps_behind_leader = []
            self.laps_behind_prec = []
        # we need to have at least 10 records
        elif len(self.overall_rank) < self.time_step:
            prediction = 0            
            self.overall_rank.append(row_data[11])
            self.last_laptime.append(row_data[4])
            self.track_status.append(row_data[13])
            self.pit_stop_count.append(row_data[14])
            self.completed_laps.append(row_data[2])
            self.elapsed_time.append(row_data[3])
            self.best_laptime.append(row_data[5])
            self.time_behind_leader.append(row_data[7])
            self.time_behind_prec.append(row_data[9])
            self.overall_best_laptime.append(row_data[12])
            self.last_pitted_lap.append(row_data[15])
            self.start_position.append(row_data[16])
            self.laps_led.append(row_data[17])
            self.best_lap.append(row_data[6])
            self.laps_behind_leader.append(row_data[8])
            self.laps_behind_prec.append(row_data[10])
        
        #normal case
        else:
            
            input_data = np.expand_dims(np.stack((self.overall_rank, self.last_laptime, self.track_status, self.pit_stop_count, self.completed_laps,
                                                  self.elapsed_time, self.best_laptime, self.time_behind_leader, self.time_behind_prec, self.overall_best_laptime,
                                                  self.last_pitted_lap, self.start_position, self.laps_led, self.best_lap, self.laps_behind_leader,
                                                  self.laps_behind_prec), axis=1), 0)
            prediction = self.model.predict(input_data)
            prediction = prediction[0,0]
            self.overall_rank.pop()
            self.last_laptime.pop()
            self.track_status.pop()
            self.pit_stop_count.pop()
            self.completed_laps.pop()
            self.elapsed_time.pop()
            self.best_laptime.pop()
            self.time_behind_leader.pop()
            self.time_behind_prec.pop()
            self.overall_best_laptime.pop()
            self.last_pitted_lap.pop()
            self.start_position.pop()
            self.laps_led.pop()
            self.best_lap.pop()
            self.laps_behind_leader.pop()
            self.laps_behind_prec.pop()
            self.overall_rank.append(row_data[11])
            self.last_laptime.append(row_data[4])
            self.track_status.append(row_data[13])
            self.pit_stop_count.append(row_data[14])
            self.completed_laps.append(row_data[2])
            self.elapsed_time.append(row_data[3])
            self.best_laptime.append(row_data[5])
            self.time_behind_leader.append(row_data[7])
            self.time_behind_prec.append(row_data[9])
            self.overall_best_laptime.append(row_data[12])
            self.last_pitted_lap.append(row_data[15])
            self.start_position.append(row_data[16])
            self.laps_led.append(row_data[17])
            self.best_lap.append(row_data[6])
            self.laps_behind_leader.append(row_data[8])
            self.laps_behind_prec.append(row_data[10])
            
            
        storm.emit([car_number, lap, str(row_data[11]), str(round(prediction))])
        

# Start the bolt when it's invoked
InferenceBolt().run()
