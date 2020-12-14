import storm
import json
import requests


class SendWebServerBolt(storm.BasicBolt):
    # Initialize this instance
    def initialize(self, conf, context):
        self._conf = conf
        self._context = context
        # Create a new counter for this instance
        storm.logInfo("SendWebsServer bolt instance starting...")

    def process(self, tup):
        race_time = tup.values[1]
        speed_data =  [tup.values[i+2] for i in range(10)]
        anomaly_score = [tup.values[i+10] for i in range(10)]
        requests.post("http://127.0.0.1:5000", data=json.dumps({'race_time':race_time, 'speed_data': speed_data, 'anomaly_score': anomaly_score}))
        storm.logInfo("SendWebsServer Bolt data: race_time: %s, speed_data: %s, anomaly_data:%s" % (str(race_time),str(speed_data), str(anomaly_score)))
        storm.logInfo("SendWebsServer Bolt r: %s" % str(r))
        #storm.emit([race_time, " data has been succesfully pushed the web site."])

# Start the bolt when it's invoked
SendWebServerBolt().run()


