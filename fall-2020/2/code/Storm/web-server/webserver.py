import json
import random
import time
from datetime import datetime

from flask import Flask, Response, request, render_template

application = Flask(__name__)


car_number = 0
lap = 0
rank = 0
prediction = 0


@application.route('/')
def index():
    return render_template('index.html')


@application.route('/chart-data')
def chart_data():
    global car_number, lap, rank, prediction
    def generate_data():
        while True:
            time.sleep(0.1)
            json_data = json.dumps({'car_number':car_number, 'lap': lap, 'actual_rank': rank, 'prediction' : prediction})
            yield f"data:{json_data}\n\n"

    return Response(generate_data(), mimetype='text/event-stream')



@application.route('/', methods=['POST'])
def get_data():
    global car_number, lap, rank, prediction
    raw_data = request.data
    print('Recieved from client: {}'.format(raw_data))
    parsed_data = json.loads(raw_data)
    car_number = parsed_data['car_number']
    lap = parsed_data['lap']
    rank = parsed_data['actual_rank']
    prediction = parsed_data['prediction']
    
    
    return Response('We recieved something…')

if __name__ == '__main__':
    application.run(debug=True)
