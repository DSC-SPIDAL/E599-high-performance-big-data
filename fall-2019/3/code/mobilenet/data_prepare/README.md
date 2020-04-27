
1. Add images into images folder
2. Add annotations into annotations folder.
3. Adjust training, val, and test ratios in automated_converter.py
	3.1 It shuffles the data. If you don't want it, just remove shuffling.
4. Run the script, it will generate tfrecords files in main directory.
	4.1. I haven't tested if it overrrides the old file or appends.So, test it before using it.


Some notes:
1. /usr/bin/python3 parts in create_tfrecords.sh can be different for different machines.
2. I listed some dependencies in automated_converter.py. However, there could be extra dependencies.
