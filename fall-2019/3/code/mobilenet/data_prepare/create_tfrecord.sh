cd models/research
#./bin/protoc object_detection/protos/*.proto --python_out=.
export PYTHONPATH=$PYTHONPATH:`pwd`:`pwd`/slim
python object_detection/builders/model_builder_test.py

cd ../../

python ../models/research/object_detection/dataset_tools/create_pascal_tf_record.py --data_dir=train_dataset --annotations_dir=Annotations --output_path=train.tfrecord --label_map_path=pascal_label_map.pbtxt

echo 'train.tfrecord is ready!!!!'

python ../models/research/object_detection/dataset_tools/create_pascal_tf_record.py --data_dir=val_dataset --annotations_dir=Annotations --output_path=val.tfrecord --label_map_path=pascal_label_map.pbtxt

echo 'val.tfrecord is ready!!!!'


python ../models/research/object_detection/dataset_tools/create_pascal_tf_record.py --data_dir=test_dataset --annotations_dir=Annotations --output_path=test.tfrecord --label_map_path=pascal_label_map.pbtxt

echo 'test.tfrecord is ready!!!!'
