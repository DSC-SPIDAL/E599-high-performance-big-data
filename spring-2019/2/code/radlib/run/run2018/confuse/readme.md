confuse
============

To investigate the usage of timestamp in nab.

Modify the timestamp to a random series and then test the result of nab anomaly detection, will it be different?


```
# make confusion
python -m indycar.nabcsv --cmd confuse --input indy2018-1-vspeed.csv --output indy2018-1-vspeed-confuse.csv

#prepare data

# train the model
python -m indycar.runnab --skipConfirmation --detector -d numenta --dataDir indycar --windowsFile indycar/indycar_labels.json

# get result
makepdf.sh indy2018-1-vspeed-confuse 0.20

```

result: they are different~
