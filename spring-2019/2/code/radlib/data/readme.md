IndyCar Dataset
====================

original log file:  eRPGenerator_TGMLP_20170528_Indianapolis500_Race.log

### extract telemetry

```
python -m indycar.rplog --extract --cmdType \$P --input eRPGenerator_TGMLP_20170528_Indianapolis500_Race.log --output telemetry-all.csv
```

all telemetry-all data extracted:   telemetry-all.csv

### get one car's telemetry by car number

```
./telebycar.sh 9 telemetry-all.csv
```

the output is car-9.csv
