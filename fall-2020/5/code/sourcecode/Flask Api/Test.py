import numpy as np
import cv2
from keras.preprocessing.image import img_to_array
from keras.models import load_model
from skimage.transform import resize

class VideoCamera(object):
    def __init__(self):
       #capturing video
       self.video = cv2.VideoCapture("test/V3.mp4")
       if self.video.isOpened() == False:
           print("Video File Not Found")
       fps = self.video.get(cv2.CAP_PROP_FPS)
       print('The number of frames in this video = ',fps)
       self.count = -1

    def read_frame(self):
        def mean_squared_loss(x1,x2):
            diff=x1-x2
            a,b,c,d,e=diff.shape
            n_samples=a*b*c*d*e
            sq_diff=diff**2
            Sum=sq_diff.sum()
            dist=np.sqrt(Sum)
            mean_dist=dist/n_samples
            return mean_dist

        images = []
        imagestore  = []        

        while self.video.isOpened():            
            ret, img = self.video.read()
            self.count+=1
            if ret==True and self.count%3 == 0: # to control taking only 10 frames per second instead of taking 30 frames per second
                #the trained model will take only 10 frames per second since FFmpeg is used while preprocessing before training. 
                #Here, while creating the API, opencv is used to read  the video for which the default fps is 30. Thus, the above tweaking has to be done.
                #preprocessing the image as they are read from the video
                img=img_to_array(img)
                images.append(img)
                img=resize(img,(227,227,3))
                gray=0.2989*img[:,:,2]+0.5870*img[:,:,1]+0.1140*img[:,:,0]
                imagestore.append(gray)

                if len(images) ==10:
                    #preprocessing bunch of 10 image to feed into the model
                    imagestore=np.array(imagestore)
                    a,b,c=imagestore.shape
                    #Reshape to (227,227,batch_size)
                    imagestore.resize(b,c,a)
                    #Normalize
                    imagestore=(imagestore-imagestore.mean())/(imagestore.std())
                    #Clip negative Values
                    imagestore=np.asarray(np.clip(imagestore,0,1))
                    X_test=imagestore[:,:,:10]
                    X_test=X_test.reshape(-1,227,227,10)
                    X_test=np.expand_dims(X_test,axis=4)

                    model  = load_model('models/Model_1.h5')
                    reconstructed_bunch=model.predict(X_test)

                    loss=mean_squared_loss(X_test,reconstructed_bunch)
                    threshold = 0.0003116156187576395
                    if loss > threshold:
                        prediction = 'Anomaly Detected'
                    else:
                        prediction = 'Normal'

                    for i in range(len(images)):
                        cv2.putText(images[i],prediction, (800,100), cv2.FONT_HERSHEY_SIMPLEX, 3, (0, 0, 0), 2)
                        ret, jpeg = cv2.imencode('.jpg', images[i])
                        return jpeg.tobytes()
                    images=[]
                    imagestore=[] 