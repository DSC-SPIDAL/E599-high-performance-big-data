run2018
=================

This experiment runs the nab tool on indycar 2018 dataset.

```
#cd $ANY_LOCAL_DIR
run2018.sh IPBroadcaster_Input_2018-05-27_0.log
```

Refer to [log](run2018.log) for detailed message.


## 1. Run analysis with flaginfo step by step 

### init the enviroment

```
#make sure your radlib repo cloned under the home directory
source ~/radlib/bin/init_env.sh

```

### prepare the data


```

#
# extract telemetry for all the cars, besides with the flag file indy2018-flag.csv
#
python -m indycar.rplogex --extract --input IPBroadcaster_Input_2018-05-27_0.log --output indy2018


#
#extract data for car#1
#
makenabinput.sh indy2018-1.csv 3 indy2018-1-vspeed.csv

#
#convert flaginfo
#
python -m indycar.nabcsv --input indy2018-flag.csv --output flaginfo.csv --cmd flag
```

### prepare nab 

Install NAB according to the document.

```
#test nab first

mkdir runnab
cd runnab
cp -r $_radlibproject_/sub/NAB/data .
cp -r $_radlibproject_/sub/NAB/labels .
cp -r $_radlibproject_/sub/NAB/config .
python -m indycar.runnab

```

Prepare the dataset with the indycar data

```
makejson.sh ../indy2018-1-vspeed.csv
```

### Run nab on indycar data

```
python -m indycar.runnab --skipConfirmation --detector -d numenta --dataDir indycar --windowsFile indycar/indycar_labels.json
```

Output like these:
```
[pengb@j-030 nab]$ python -m indycar.runnab --skipConfirmation --detector -d numenta --dataDir indycar --windowsFile indycar/indycar_labels.json

Running detection step
0: Beginning detection with numenta for indycar/indy2018-1-vspeed.csv
. . . . . . . . . . . . . . . . . . . . . . . . 0: Completed processing #### records at ####
0: Results have been written to /scratch_hdd/hpda/radlib/runnab/nab/results/numenta/indycar/numenta_indy2018-1-vspeed.csv
```

### show the result with different threshold

```
makepdf.sh indy2018-1-vspeed 0.35
makepdf.sh indy2018-1-vspeed 0.55
makepdf.sh indy2018-1-vspeed 0.65
```

what makepdf does are like these:

```
#extract the anomaly by set the threshold(default 0.65)
#python -m indycar.nabcsv --input results/numenta/indycar/numenta_indy2018-1-vspeed.csv --output anomaly_1.csv --threshold 0.65

#draw the figure with flaginfo
#python -m indycar.plot --plot anomaly --input anomaly_1.csv --output indy2018-1-vspeed.pdf --flaginfo ../flaginfo.csv
```


Finally, download the figure file "indy2018-1-vspeed*.pdf" to your desktop.

## 2. Run anomaly detection by removing all data in the YELLOW flag status

```
cd ..
python -m indycar.nabcsv --cmd prune --input indy2018-1-vspeed.csv --flagfile flaginfo.csv --output indy2018-1-vspeed-prune.csv

cd runnab
#save old results
mv indycar indycar-all
mv result result-all

makejson.sh ../indy2018-1-vspeed-prune.csv
python -m indycar.runnab --skipConfirmation --detector -d numenta --dataDir indycar --windowsFile indycar/indycar_labels.json

makepdf.sh indy2018-1-vspeed-prune 0.35
makepdf.sh indy2018-1-vspeed-prune 0.55
makepdf.sh indy2018-1-vspeed-prune 0.65
```

Finally, download the figure file "indy2018-1-vspeed-prune*.pdf" to your desktop.




