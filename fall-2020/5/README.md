**ENGR - E599 High Performance Big Data Systems**

**Project:** **INDYCAR VIDEO CRASH DETECTION USING SPATIO-TEMPORAL AUTOENCODER**

**Team Members:**

Sai Prasad Parsa

Humshavarthini Karunanidhi

**Summary:**
There is an increasing need not only for recognition of objects and their behavior but in particular for detecting the rare, interesting occurrences of unusual objects or suspicious behavior in the large body of ordinary data. Finding such abnormalities in Indycar race videos is crucial to provide increased safety and to develop better winning strategies for the drivers in the Series. Abnormal events that are of interest to us in the IndyCar race events are car crashes or damages. But these events often have an extremely low probability of occurring in long video footage. As such, it is a very meticulous job to manually detect such events, or anomalies, that often requires more manpower than is generally available. This has prompted the need for developing an automated detection of anomalies or crashes in the IndyCar race events and motivated us to use a deep learning approach.

 We have developed an end-to-end model that consists of a spatial feature extractor and a temporal encoder-decoder which together learns the temporal patterns of the input volume of frames is trained. The model is trained with video volumes consisting of only normal scenes. The objective here is to minimize the reconstruction error between the input video volume and the output video volume reconstructed by the learned model. The trained model is expected to give a low reconstruction error for normal video volume, whereas video volume consisting of abnormal scenes is expected to have a high reconstruction error. 

The model is trained on car race videos scraped from YouTube mainly consisting of IndyCar race events. The videos were manually edited especially to obtain sequences with a reasonable duration of abnormal events within the long footage. Training data consists of 50 video clips where the duration of each clip varies between less than a minute and two minutes long and test data consists of 10 video clips. Each video clip contains about 600 frames making up data of about 6GB. All videos are taken from a fixed camera angle for both training and testing sets, where the car moves parallel to the camera plane i.e, visor cam view. 

**Performance and Analysis:**    
**Input1: Crash Video**
![img](https://lh4.googleusercontent.com/BeqyJ8b88Qs9mgWmmiGQl-DzIcY112KmlVquTfoONj2NRHqSf7YfO69JCvc9gOzAVEf0eg_iF2VhE8T84PPm-eK6FhPB4tQRLSQmw_p0-RTLY4_WZOqHwqbNB0FV_i97iO6BhZIW)   **Abnormality score per bunch** 

When the performance of the model for video input with multiple anomalies like crash and people on the race track was observed, it was evident that the anomaly score stays below the threshold for the normal case (no crash) and spikes up over the threshold value before the collision. The model predicts events like off-road frames or cars abnormally closer to each other which represents an anomaly and a possible collision. In the above figure , it can be seen that the anomaly score is the highest when a person comes onto the track as it is a rarer event compared to a car crash to occur in a race. It is also observed that the model tags event as no anomaly when the car spins back on to the road amidst the anomalies.

**Input2: No Crash Video**![img](https://lh6.googleusercontent.com/-dMg-EkRE_bKRZ5tkAvu1Z4cy181qxt6ri0-P96N4q5mbt1ZfGoVrteq7LXiiVzvsYSU_2wKuo4GidhDO39ABn5y4eKFTAN48wBL-UtZVuMXaRwnx8xjuWJtMMj_a58gwBTL6rUH)    **Abnormality score per bunch**

If a no crash video as in the above figure is given as the input, the anomaly score stays well below the threshold but the score varies slightly based on the lighting conditions in the video frames. The model is trained predominantly on videos with better lighting conditions that might give higher anomaly scores owing to the bad lighting conditions. 

