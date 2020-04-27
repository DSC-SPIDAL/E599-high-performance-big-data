


### trained model

# Running scripts..

## 1. Installation

Install requirements. make sure that you have Keras installed.
```
pip install -r requirements.txt
```

## 2. lets train region proposal network first, rather than training the whole network.
Training the entire faster-rcnn is quite difficult, but RPN itself can be more handy!

You can see if the loss converges.. etc

Other network options are: resnet50, mobilenetv1, vgg19.

```
python train_rpn.py --network resnet50 -o simple -p /path/to/your/dataset/

```

## 3. then train the whole Faster-RCNN network!

# sample command to train PASCAL_VOC dataset:
```
python train_frcnn.py -p ../VOCdevkit/ --lr 1e-4 --opt SGD --network resnet50 --elen 1000 --num_epoch 100 --hf 

```

## 4. test your models

```
python test_frcnn.py --path ~/VOC/test/VOCdevkit/VOC2007/JPEGImages/ --write
```

## 4. measure mAP

```
python measure_map.py --path ~/VOC/test/VOCdevkit/
```
code courtesy: https://github.com/kentaroy47




