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
        car_number = tup.values[0]
        lap = tup.values[1]
        rank = tup.values[2]
        prediction = tup.values[3]
        requests.post("http://127.0.0.1:5000", data=json.dumps({'car_number':car_number, 'lap': lap, 'actual_rank': rank, 'prediction' : prediction}))
        storm.logInfo("SendWebsServer Bolt data: %s" % (str({'car_number':car_number, 'lap': lap, 'actual_rank': rank, 'prediction' : prediction})))


# Start the bolt when it's invoked
SendWebServerBolt().run()


