# Crash Detection using YOLO and Siamese Network
### [Manjeet Pandey](https://github.com/ManjeetKP), [Sanket Patole](https://github.com/sanket-11), [Shivam Thakur](https://github.com/shivam529)
This is a code repository for our project based on Crash Detection in the Indy500 racing event.
## Indy500
Indy500 is one of the premier car racing event in the nation involving numerous participants and the nature of the track with unexpected twists and turns, there is always a possibility of crash. Nearly 60 competitors have perished at Indy, with plenty more spectators and crew also falling at the track.
## Crash Detection for Indy500
We propose an end-to-end deep learning crash detection pipeline using traditional concepts of object detection and latent space embeddings to accurately detect a crash in a live feed. Our project is based on a two layer model, where we first detect the crash in a given frame using YOLO and to further improve the results, we pass the predicted output from YOLO to a Siamese Network which uses a one-shot technique to classify between a normal car and crashed car.
## Dataset
For the dataset, we were provided with around 2000 labelled images of IndyCar races. To add to this, we scraped Youtube videos of IndyCar crashes and labelled around 1000 crash images in YOLO format using the [labelImg](https://github.com/tzutalin/labelImg) tool.
## Architecture
#### YOLO
![Yolo](https://miro.medium.com/max/640/0*WUpMWzNu_ymDyHPp.png)
 - In Yolo a single convolutional network simultaneously predicts multiple bounding boxes and class probabilities for those boxes.
 - It trains on full images and directly optimizes detection performance.
 - Yolo divides the input image into a number of grids cells. Each cell predicts bounding boxes and confidence scores for those boxes.
 - The confidence scores reflect how confident the model is that the box contains an object and also how accurate it thinks the box is that it predicts.
 ##### YOLO Stage 1 Output :
![yolostage1](https://raw.githubusercontent.com/ManjeetKP/E599-high-performance-big-data/master/fall-2019/4/code/output_data/stage11.png)
 ##### YOLO Stage 2 Output :
![yolostage2](https://i.imgur.com/3iebLRz.png)
##### YOLO Stage 3 Output :
![yolostage3](https://raw.githubusercontent.com/ManjeetKP/E599-high-performance-big-data/master/fall-2019/4/code/output_data/stage22.png)
#### Siamese Network
 - A Siamese networks consists of two identical neural networks, each taking one of the two input images.
 - The output of this network is sent to a contrastive loss function which calculates a similarity score between the two images.
 - We use this to our advantage where we cluster the embeddings of crashed cars and normal cars.
 - The K-Means clustering performs well on the embeddings and clearly forms separate clusters for the two classes, which is further used for classification.
 - Individual images can be used to classify using the YOLO and Siamese in conjugation, however we are still working to integrate siamese with YOLO to do classification on live streams
 
![Siamese Netowork](https://sites.google.com/site/quartetnetlearning/_/rsrc/1467097642046/home/siamese.jpg?height=255)

##### Siamese Network Output(Single Margin):
![singlemargin](https://i.imgur.com/xftfvrI.png)
##### Siamese Network Output(Double Margin):
![doublemargin](https://i.imgur.com/44tOpvp.png)
## Conclusion:
 -	YOLO performs well in detecting cars in general, irrespective of its condition.
 -	When trained to differentiate and predict Crash and normal car, it gets confused and sometimes detects a crashed car as a normal car.
 -	Also, since the idea behind YOLO to detect a crash is to detect smoke, off-road or a car with broken parts, sometimes a normal car in the vicinity of smoke or broken parts is detected as crash.
 -	Adding a Siamese network in the pipe-line along with YOLO boosts the accuracy a lot.
 -	Performing clustering using K-Means on the embeddings of the two classes (crash and car) clearly visualizes two separate clusters, which supports our boost in the accuracy with Siamese network.
 -	However, apart from the above results, there is still room for improvement.
 -	Amongst our experiments, we realized that the ideal architecture to predict crash should involve recurrence, such as a RNN.
 -	With recurrence we can use data from multiple frames of the live stream and then make meticulous predictions, since a recurrent network will use data along a time-line to increase the accuracy of the prediction. Such as multiple frames of a crash one-after the other.

## References

[1]	Miller, A. (2015, March 12). The 8 Deadliest Race Tracks In The World.  https://www.thrillist.com/cars/the-8-deadliest-tracks-on-earth-most-dangerous-races.

[2]	Schroff, Florian, et al. “FaceNet: A Unified Embedding for Face Recognition and Clustering.” 2015 IEEE Conference on Computer Vision and Pattern Recognition (CVPR), 2015, doi:10.1109/cvpr.2015.7298682.

[3] 	Redmon, Joseph, et al. “You Only Look Once: Unified, Real-Time Object Detection.” 2016 IEEE Conference on Computer Vision and Pattern Recognition (CVPR), 2016, doi:10.1109/cvpr.2016.91.

[4]	Hadsell, R., Chopra, S., & Lecun, Y. (n.d.). Dimensionality Reduction by Learning an Invariant Mapping. 2006 IEEE Computer Society Conference on Computer Vision and Pattern Recognition - Volume 2 (CVPR06). 

[5]	Hao, J., Dong, J., Wang, W., & Tan, T. (2018). DeepFirearm: Learning Discriminative Feature Representation for Fine-grained Firearm Retrieval. 2018 24th International Conference on Pattern Recognition (ICPR). 
