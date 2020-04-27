Pytorch  implementation of RetinaNet object detection as described in [Focal Loss for Dense Object Detection](https://arxiv.org/abs/1708.02002) by Tsung-Yi Lin, Priya Goyal, Ross Girshick, Kaiming He and Piotr Dollár.

This implementation is primarily designed to be easy to read and simple to modify.

## Results


Retinanet model with resnet50 backbone network scored 0.0872mAP on detection of car and 0.048 mAP on detection of smoke on the Indianapolis racing car images while, the model with resnet152 backbone network scored 0.0847 mAP on detection of car and 0.0565 mAP on detection of smoke on the images

## Installation

1) Clone this repo

2) Install the required packages:

```
apt-get install tk-dev python-tk
```

3) Install the python packages:
	
```
pip install cffi

pip install pandas

pip install pycocotools

pip install cython

pip install opencv-python

pip install requests

```

4) Build the NMS extension.

```
cd pytorch-retinanet/lib
bash build.sh
cd ../
```

## Training

The network can be trained using the `train.py` script. Currently, two dataloaders are available: COCO and CSV. For training using csv, use

Model 1 

```bash
python3 train.py --dataset csv --csv_train input_data/indycar_train.csv --csv_val input_data/indycar_test.csv --csv_classes input_data/classes.csv --save_path models/model1  --depth 50
```

Model 2

```bash
python3 train.py --dataset csv --csv_train input_data/indycar_train.csv --csv_val input_data/indycar_test.csv --csv_classes input_data/classes.csv --save_path models/model2  --depth 152
```

## Testing

```bash
## Model 1
python3 visualize.py --dataset csv --csv_classes input_data/classes.csv --csv_val input_data/indycar_test.csv --model models/model1/csv_retinanet_99.pt --save_path output_path/model1/

## Model 2
python3 visualize.py --dataset csv --csv_classes input_data/classes.csv --csv_val input_data/indycar_test.csv --model models/model2/csv_retinanet_99.pt --save_path output_path/model2/
```