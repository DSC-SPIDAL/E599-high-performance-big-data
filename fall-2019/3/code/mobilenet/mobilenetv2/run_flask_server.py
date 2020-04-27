
# https://github.com/log0/video_streaming_with_flask_example

import numpy as np
import cv2
from flask import Flask, render_template, Response
import time
import cv2
import time
import core.utils as utils
import tensorflow as tf
from PIL import Image



app = Flask(__name__)


# use gpu-0 if there are multiple gpus
import os
os.environ["CUDA_VISIBLE_DEVICES"]="0"

return_elements = ["input/input_data:0", "pred_sbbox/concat_2:0", "pred_mbbox/concat_2:0", "pred_lbbox/concat_2:0"]
pb_file         = "exported_model/saved_model/saved_model.pb "
num_classes     = 20
input_size      = 416
graph           = tf.Graph()
return_tensors  = utils.read_pb_return_tensors(graph, pb_file, return_elements)

sess = tf.Session(graph=graph)


@app.route('/')
def index():
    return render_template('index.html')

def gen():
        
    myrtmp_addr = "rtmp://localhost/live/indycar live=1"
    cap = cv2.VideoCapture(myrtmp_addr)
    
    infer_flag=True
    while True:
        ret, frame = cap.read()
        if not ret:
            print('Input source error!')
            break
            
        frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        image = Image.fromarray(frame)

        if infer_flag:

            frame_size = frame.shape[:2]
            image_data = utils.image_preporcess(np.copy(frame), [input_size, input_size])
            image_data = image_data[np.newaxis, ...]
            pred_sbbox, pred_mbbox, pred_lbbox = sess.run([return_tensors[1], return_tensors[2], return_tensors[3]],
                feed_dict={ return_tensors[0]: image_data})
            pred_bbox = np.concatenate([np.reshape(pred_sbbox, (-1, 5 + num_classes)), 
                np.reshape(pred_mbbox, (-1, 5 + num_classes)),
                np.reshape(pred_lbbox, (-1, 5 + num_classes))], axis=0)

            bboxes = utils.postprocess_boxes(pred_bbox, frame_size, input_size, 0.3)
            bboxes = utils.nms(bboxes, 0.45, method='nms')
        infer_flag = not infer_flag

        image = utils.draw_bbox(frame, bboxes)

        result = np.asarray(image)
        frame = cv2.cvtColor(result, cv2.COLOR_RGB2BGR)
        
        ret, jpeg = cv2.imencode('.jpg', frame)
        frame = jpeg.tobytes()
        yield (b'--frame\r\n'
                b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')

@app.route('/video_feed')
def video_feed():
    return Response(gen(),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=61521)
