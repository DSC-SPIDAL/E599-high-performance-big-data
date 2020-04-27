#!/bin/bash

if [ $# -ne '2' ] ; then
    echo 'Get telemetry data by car number'
    echo 'getbycar.sh <carno> <telemetryfile>'
    exit -1
fi

carno=$1
data=$2
grep -P "P\t${carno}\t" $data >car-${carno}.csv
