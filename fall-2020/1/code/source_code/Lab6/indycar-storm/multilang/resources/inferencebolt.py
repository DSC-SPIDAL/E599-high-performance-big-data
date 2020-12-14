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
        self.speed_data = []
        self.next_step_data = []
        self.time_step = 80
        self.scaler = MinMaxScaler(feature_range=(0, 1)).fit(np.expand_dims([0, 100, 200, 235.31], 1))
        
        #############################################################################################
        ## change 'sakkas' with your username
        self.model = tf.keras.models.load_model('/N/u/sakkas/Lab6/indycar-anomaly-model.h5')
        #############################################################################################

    def process(self, tup):
        race_time = tup.values[1]
        row_data =  [tup.values[i+2] for i in range(10)]
        storm.logInfo("Inference Bolt data: race_time: %s, data: %s" % (str(race_time),str(row_data)))
        
        # restart the event
        if race_time == 0:
            self.speed_data = []
            self.next_step_data = row_data
        
        # we need to have 80 seconds data at least to start detection
        elif race_time <= self.time_step:
            self.speed_data.append(self.next_step_data)
            self.next_step_data = row_data
        
        #normal case
        else:
            self.speed_data.append(self.next_step_data)
            self.speed_data.pop()
            self.next_step_data = row_data
            input_data = np.expand_dims(self.scaler.transform(np.array(self.speed_data).T), 2)
            prediction = self.scaler.inverse_transform(self.model.predict(input_data))
            anomaly_score = np.abs(prediction[:,0] - np.array(self.next_step_data)).tolist()
            
            #storm.logInfo("race_time:%s speed:%s anomaly_score %s" % (str(race_time), str(self.next_step_data), str(anomaly_score)))
            emit_data = ["word"] 
            emit_data.append(race_time)
            emit_data = emit_data + self.next_step_data + anomaly_score
            
            storm.logInfo("Inference Bolt emiting: %s" % str(emit_data))
            storm.emit(emit_data)
        

# Start the bolt when it's invoked
InferenceBolt().run()
