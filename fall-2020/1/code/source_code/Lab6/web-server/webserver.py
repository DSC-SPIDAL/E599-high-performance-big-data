import json
import random
import time
from datetime import datetime

from flask import Flask, Response, request, render_template

application = Flask(__name__)


race_time = 0
anomaly_score = 0
speed_data = [0]

@application.route('/')
def index():
    return render_template('index.html')


@application.route('/chart-data')
def chart_data():
    global race_time, anomaly_score, speed_data
    def generate_data():
        while True:
            json_data = json.dumps(
                {'time': str(race_time), 'speed_data': speed_data, 'anomaly_score': anomaly_score})
            yield f"data:{json_data}\n\n"
            time.sleep(0.35)

    return Response(generate_data(), mimetype='text/event-stream')



@application.route('/', methods=['POST'])
def get_data():
    global race_time, anomaly_score, speed_data
    raw_data = request.data
    print('Recieved from client: {}'.format(raw_data))
    parsed_data = json.loads(raw_data)
    race_time = parsed_data['race_time']
    anomaly_score = parsed_data['speed_data']
    speed_data = parsed_data['anomaly_score']
    
    return Response('We recieved something…')

if __name__ == '__main__':
    application.run(debug=True)
