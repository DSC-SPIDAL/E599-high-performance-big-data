

# Object Detection On Indianapolis Racing Car Images: 

## Using Retinanet

Using Retinane Object Detection is used almost everywhere these days. The use cases are endless, be it tracking objects, video surveillance, pedestrian detection, anomaly detection, people counting, self-driving cars, or face detection. The list of possible applications goes on. Thegoal of my project is to explore the performance of object detection models specifically RetinaNet in detecting cars and smoke in the Indianapolis racing car image dataset. The model is evaluated on mean average precision (MAP) score. Two variants of the model were explored and mAP was computed for each of the RetinaNet model. In the first model, Resnet50 was used as a backbone network for feature extraction and, in the second model Resnet152  was used as a backbone network for feature extraction. Difference between the models is the number of layers of the network.

Resnet50 has 50 layers while, resnet 152 has 152 layers. Both the models scored very similar in the mAP score.

Retinanet model with resnet50 backbone network scored <b>0.0872 mAP</b> on detection of car and <b>0.048 mAP on detection of smoke</b> on the Indianapolis racing car images while, the model with resnet152 backbone network scored <b>0.0847 mAP on detection of car and 0.0565 mAP on detection of smoke</b> on the images. RetinaNet model with resnet50 did well on dectection of cars while resnet152 model  performed well in detection of smoke on the test images. Since detection of smoke is more a fine grained problem, addition of more layers with resnet152 provided better detection while model overfitten on detection of cars.

Results on car indicated that the model overfitted on the training data. 



 ![](https://raw.githubusercontent.com/mchivuku/E599-high-performance-big-data/master/fall-2019/1/results.png)

 



```

```