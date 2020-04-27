#!/usr/bin/env python3

import os
import xml.etree.ElementTree as ET
data_path="data/IndyCar_ImageData"

def build_image_file():
    
    train_img_inds_file = os.path.join(data_path, 'train.txt')
    with open(train_img_inds_file,'w') as f: 
        for file in os.listdir(os.path.join(data_path,'train_images')):
            f.write(file + "\n")

    test_img_inds_file = os.path.join(data_path, 'test.txt')
    
    with open(test_img_inds_file,'w') as f: 
        for file in os.listdir(os.path.join(data_path,'test_images')):
            f.write(file + "\n")


## Build Annotations
def convert_indycar_annotation(data_path, data_type, anno_path, use_difficult_bbox=True):

    classes = ['aeroplane', 'bicycle', 'bird', 'boat', 'bottle', 'bus',
               'car', 'cat', 'chair', 'cow', 'diningtable', 'dog', 'horse',
               'motorbike', 'person', 'pottedplant', 'sheep', 'sofa',
               'train', 'tvmonitor']
               
               
    img_inds_file = os.path.join(data_path,  data_type + '.txt')
    
    
    with open(img_inds_file, 'r') as f:
        txt = f.readlines()
        image_inds = [line.strip() for line in txt]

    with open(anno_path, 'a') as f:
        for image_ind in image_inds:

            imagename, image_extension = os.path.splitext(image_ind)
            
            inner_folder = "train_images" if data_type=="train" else "test_images"
            image_path = os.path.join(data_path,inner_folder, image_ind)
            

            annot_folder = ""
            if data_type=="train":
              annot_folder = "train_annotations"
            else:
              annot_folder = "test_annotations"

            label_path = os.path.join(data_path, annot_folder, imagename + '.xml')

            try:
              root = ET.parse(label_path).getroot()
              objects = root.findall('object')
              for obj in objects:
                  annotation = image_path
                  difficult = obj.find('difficult').text.strip()
                  if (not use_difficult_bbox) and(int(difficult) == 1):
                      continue
                  bbox = obj.find('bndbox')
                  class_ind = classes.index(obj.find('name').text.lower().strip())
                  class_name = obj.find('name').text.lower().strip()
                  xmin = bbox.find('xmin').text.strip()
                  xmax = bbox.find('xmax').text.strip()
                  ymin = bbox.find('ymin').text.strip()
                  ymax = bbox.find('ymax').text.strip()
                  annotation += ',' + ','.join([xmin, ymin, xmax, ymax, class_name])
                  print(annotation)
                  f.write(annotation + "\n")
            except:
              print("An exception occurred", annotation)
    return len(image_inds)

if __name__=="__main__":
    ## build indicator files
    build_image_file()
    convert_indycar_annotation(data_path,"train", 'data/indycar_train.csv',False)
    convert_indycar_annotation(data_path,"test", 'data/indycar_test.csv',False)