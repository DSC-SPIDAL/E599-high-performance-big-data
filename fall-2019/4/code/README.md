# Instructions to run the code

### Install the dependencies

```sh
$ pip install opencv-python
$ pip install easydict
$ sudo apt-get install openjdk-8-jdk
$ sudo apt-get install maven
```

### Flask server
```sh
wget https://github.com/Red5/red5-server/releases/download/v1.0.9-RELEASE/red5-server-1.0.9-RELEASE.tar.gz
tar xzvf red5-server-1.0.9-RELEASE.tar.gz
cd red5-server
# start the server. & starts it in the background
./red5.sh &
```

### To run pre trained model on a live feed or a video

```sh
$ python run_flask_server.py
```

### Obtain car embeddings 

```sh
Once you run the model and obtain the bounding boxes, pass the output images from YOLO to the siamese network to obtain embeddings using CarEmbedding.ipynb
```
