runnab
=================

This experiment runs the nab tool on indycar dataset.

### init the enviroment

```
#make sure your radlib repo cloned under the home directory
source ~/radlib/bin/init_env.sh

```

### prepare the data

Select Car#9 as example.

First extract the telemetry for car#9
```
#
#extract all telemetry data from log file
#make sure the log file is there, eRPGenerator_TGMLP_20170528_Indianapolis500_Race.log
#
python -m indycar.rplog --extract --cmdType \$P --input eRPGenerator_TGMLP_20170528_Indianapolis500_Race.log --output telemetry-all.csv

#
#extract data for car#9, valid records are in 9-rpm-valid.csv
#
telebycar.sh 9 telemetry-all.csv 9
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
mkdir -p indycar/indycar
cp ../9-rpm-valid.csv indycar/indycar
#make an empty labels file
python -c 'print("{\n\t\"indycar/%s\": []\n}"%("9-rpm-valid.csv"))' > indycar/indycar_labels.json

```

### Run nab on indycar data

```
python -m indycar.runnab --skipConfirmation --detector -d numenta --dataDir indycar --windowsFile indycar/indycar_labels.json
```

Output like these:
```
[pengb@j-030 nab]$ python -m indycar.runnab --skipConfirmation --detector -d numenta --dataDir indycar --windowsFile indycar/indycar_labels.json
{'dataDir': 'indycar',
    'detect': True,
    'detectors': ['numenta'],
    'normalize': False,
    'numCPUs': None,
    'optimize': False,
    'profilesFile': 'config/profiles.json',
    'resultsDir': 'results',
    'score': False,
    'skipConfirmation': False,
    'thresholdsFile': 'config/thresholds.json',
    'windowsFile': 'indycar/indycar_labels.json'}
Proceed? (y/n): y

Running detection step
0: Beginning detection with numenta for indycar/9-rpm-valid.csv
. . . . . . . . . . . . . . . . . . . . . . . . 0: Completed processing 23507 records at 2018-06-27 14:59:20.667672
0: Results have been written to /scratch_hdd/hpda/radlib/runnab/nab/results/numenta/indycar/numenta_9-rpm-valid.csv
```

### show the result

```
#extract the anomaly by set the threshold(default 0.65)
python -m indycar.nabcsv --input results/numenta/indycar/numenta_9-rpm-valid.csv --output anomaly_9.csv --threshold 0.65

#draw the figure
python -m indycar.plot --plot anomaly --input anomaly_9.csv --output 9-rpm-valid.pdf
```

Finally, download the figure file "9-rpm-valid.pdf" to your desktop.








