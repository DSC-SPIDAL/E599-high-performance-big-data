from flask import Flask, render_template, Response
from Test import VideoCamera

app = Flask(__name__)

@app.route('/')
def emotion_detection():
    # rendering webpage
    return render_template('anomaly_detection.html')

def gen(Test):
    while True:
        frame = Test.read_frame()
        yield (b'--frame\r\n' b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')
                    

@app.route('/video_feed')
def video_feed():
    return Response(gen(VideoCamera()),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

if __name__ == '__main__':
    app.run(host='0.0.0.0',port='5000', debug=True)          