1. To run SSD-Mobilenet models (SSD-Mobilenetv1 and SSD-Mobilenetv2) please refer to the readme file in the folder mobilenet
2. To run Tiny-yoloV3 model  follow these steps: 

## 1. Clone the repository
## 2. Install requirement 
Inside the folder TensorFlow-yolov3-tiny run: 

```bash
TensorFlow-yolov3-tiny
```

## 3. Prepare the data 
The data needs to have the following structure :

```bash
VOC           # path:  /N/u/<username>/TensorFlow-yolov3-tiny/VOC/

── VOC
│   ├── test
│   │   └── VOCdevkit
│   │       └── VOC2007
│   │           ├── Annotations # annotation files in VOC format
│   │           ├── ImageSets 
│   │           │   └── Main
│   │           │       └── test.txt  # each line contains image names; for example: 102nd_1750 (without extension
│   │           └── JPEGImages # jpeg files
│   └── train
│       └── VOCdevkit
│           └── VOC2007
│               ├── Annotations # annotation files in VOC format
│               ├── ImageSets
│               │   └── Main
│               │       ├── train.txt
│               │       └── trainval.txt # each line contains image names; for example: 102nd_1750 (without extension)
│               └── JPEGImages # jpeg files
```
Update num2 like below in ``scripts/voc_annotation.py``
```python 
num1 = convert_voc_annotation(os.path.join(flags.data_path,
'train/VOCdevkit/VOC2007'), 'trainval', flags.train_annotation, False)
num2 = 0 #convert_voc_annotation(os.path.join(flags.data_path,
'train/VOCdevkit/VOC2012'), 'trainval', flags.train_annotation, False)
num3 = convert_voc_annotation(os.path.join(flags.data_path,
'test/VOCdevkit/VOC2007'), 'test', flags.test_annotation, False)
print('=> The number of image for train is: %d\tThe number of image for test
is:%d' %(num1 + num2, num3))
```
Then run the script below:
```bash
python scripts/voc_annotation.py --data_path ~/tensorflow-yolov3/VOC
```
 
## 4. Configurations: 
Edit ``core/config.py`` and ``core/config_tiny.py`` and change the lines like below:

```bash
  __C.YOLO.CLASSES = "./data/classes/voc.names"
__C.TRAIN.ANNOT_PATH = "./data/dataset/voc_train.txt"
__C.TEST.ANNOT_PATH = "./data/dataset/voc_test.txt"
  ```
 ## 5. Training 
 1. Run the script "run_training.sh
 ```python 
python train_tiny.py                      
```
Training will take long. Think of doing it within a tmux session

## 6. Export model
 1. Once the training is done, run the script "run_training.sh
 ```python 
python freeze_graph.py                  
```
## 7. Inference
 update ``core/config.py``and  ``core/config_tiny.py``
```bash
__C.TEST.WEIGHT_FILE = "./checkpoint/yolov3_test_loss=9.0973.ckpt-26" choose the chackpoint with the smallest loss
```
### 7.1. On images
run the followings:
```bash 
python evaluate_tiny1.py
cd mAP
python main.py -na
```
if you see an error update this line
```python 
bbox_data_gt = np.array([list(map(int,map(float, box.split(',')))) for box in
annotation[1:]])
```

The resulting images will be found in ``data/detection``
If you want to run the time benchmark like we did for the mobilenet networks: 
```bash 
python evaluate_tiny.py

```
### 7.2. On video
Using the script ``video_demo.py``, it is possible to recreate the same results as the time_benchmark script but instead of the input being images, it can be a video that frames will be extracted from.
```bash 
python video_demo.py

```
Make sure to update the video file location 
### 7.3. Real-time object detection using red5 and flask 
1. Run these commands in your home directory:
``` bash
sudo apt-get install openjdk-8-jdk
sudo apt-get install maven
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
```
Run ``sudo vim ~/.bashrc`` and paste the line below inside the file:
```bash
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

```
Then run this to update the system variables:
``` bash
source ~/.bashrc

```
Check the JAVA_HOME:
```bash 
echo $JAVA_HOME
/usr/lib/jvm/java-8-openjdk-amd64
```


Install red5 server
```bash
wget https://github.com/Red5/red5-server/releases/download/v1.0.9-RELEASE/red5-
server-1.0.9-RELEASE.tar.gz
tar xzvf red5-server-1.0.9-RELEASE.tar.gz
cd red5-server
# start the server. & starts it in the background
./red5.sh &
```
To shut dow the server ``./red5-shutdown.sh``

Port forwarding  (this step is only necessary if you are working on a virtual server
```bash
ssh -L 61521:localhost:61521 -L 5080:localhost:5080 vm2.india.futuregrid.org
```
Now go to page: ``localhost:5080`` on your browser and you will see the red5 page

To download the rtmp simulator : [use this link] (https://github.com/DSC-SPIDAL/IndyCar/tree/master/utils/rtmp-simulator)
Go to the rtmp-simulator directory and run :
```bash
mvn packag

```
then, 
```bash 
java -cp target/rtmp-simulator-1.0-SNAPSHOT-jar-with-dependencies.jar
edu.iu.dsc.indycar.rtmp.RTMPStreamer PATH_TO_VIDEO_FILE
```
Go to the flask  folder and run: 
```bash
python run_flask_server.py

```
Then, Go to your browser  and run ``localhost:61521`` (or the port that you assigned) 
