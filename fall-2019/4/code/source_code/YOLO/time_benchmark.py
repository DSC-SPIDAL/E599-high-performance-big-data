
import glob

import cv2
import numpy as np
import core.utils as utils
import tensorflow as tf
from PIL import Image
import time

return_elements = ["input/input_data:0", "pred_sbbox/concat_2:0", "pred_mbbox/concat_2:0", "pred_lbbox/concat_2:0"]
pb_file         = "./yolov3_coco.pb"
image_path      = "./docs/images/road.jpeg"
num_classes     = 20
input_size      = 416
graph           = tf.Graph()



image_paths = glob.glob( "VOC/train/VOCdevkit/VOC2007/JPEGImages/*.jpg")



return_tensors = utils.read_pb_return_tensors(graph, pb_file, return_elements)

image_count = 0

sess = tf.Session(graph=graph)

print('Number of images:', len(image_paths))

load_time = 0
pre_proc_time = 0
inf_time = 0
post_proc_time = 0

for image_path in image_paths:

    start_time = time.time()
    original_image = cv2.imread(image_path)
    load_time += (time.time() - start_time)

    start_time = time.time()
    original_image = cv2.cvtColor(original_image, cv2.COLOR_BGR2RGB)
    original_image_size = original_image.shape[:2]
    image_data = utils.image_preporcess(np.copy(original_image), [input_size, input_size])
    image_data = image_data[np.newaxis, ...]
    pre_proc_time += (time.time() - start_time) 

    start_time = time.time()
    pred_sbbox, pred_mbbox, pred_lbbox = sess.run(
        [return_tensors[1], return_tensors[2], return_tensors[3]],
                feed_dict={ return_tensors[0]: image_data})
    inf_time += (time.time() - start_time)

    start_time = time.time()
    pred_bbox = np.concatenate([np.reshape(pred_sbbox, (-1, 5 + num_classes)),
                            np.reshape(pred_mbbox, (-1, 5 + num_classes)),
                            np.reshape(pred_lbbox, (-1, 5 + num_classes))], axis=0)

    bboxes = utils.postprocess_boxes(pred_bbox, original_image_size, input_size, 0.3)
    bboxes = utils.nms(bboxes, 0.45, method='nms')
    image = utils.draw_bbox(original_image, bboxes)
    image = Image.fromarray(image)
    post_proc_time += (time.time() - start_time)
    #image.show()
    image_count += 1

    if image_count % 10 == 0:
    	print(image_count, 'images inferred')
    
print("Total Load time = ",load_time)
print("Total Pre-Processing time = ",pre_proc_time)
print("Total Inference time = ",inf_time)
print("Total Post-Processing time = ",post_proc_time)
