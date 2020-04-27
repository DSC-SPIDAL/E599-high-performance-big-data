#!/bin/bash

#
# this demo runs on indy2018 data logs
#

SOURCE="${BASH_SOURCE[0]}"
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
#echo "source=$SOURCE, and ,dir=$DIR"
source $DIR/../../bin/init_env.sh

if [ -z "$_radlibproject_" ]; then
    echo "Radlib should be initialized, quit! Run init cmds like:"
    echo "  source RADLIB_REPO_HOMEDIR/bin/init_env.sh"
    echo "  or"
    echo "  source ~/radlib/bin/init_env.sh"
    exit 0
fi

if [ $# -ne 1 ] ; then
    echo "usage: run2018.sh <logfile>"
    exit -1
fi

#logfile=IPBroadcaster_Input_2018-05-27_0.log
logfile=$(readlink -f $1)

if [ ! -f "${logfile}" ]; then
    echo "logfile ${logfile}  not found, quit"
    exit -1
fi

#
# start
#
#mkdir -p run2018
cd run2018

#
# extract telemetry and flaginfo from logfile
#
#extract telemetry
python -m indycar.rplogex --extract --input ${logfile} --output indy2018
#convert flaginfo
python -m indycar.nabcsv --input indy2018-flag.csv --output flaginfo.csv --cmd flag

#
# prepare nab env
#
#mkdir runnab
cd runnab
#cp -r $_radlibproject_/sub/NAB/data .
#cp -r $_radlibproject_/sub/NAB/labels .
#cp -r $_radlibproject_/sub/NAB/config .

#car=1
detector=numenta
# 1 3 4 6 7 9 10 12 13 14 15 17 18 19 20 21 22 23 24 25 26 27 28 29 30 32 33 59 60 64 66 88 98
for car in 1
do
  #
  # run anomaly detection on carno=1 with metric vspeed
  #
  # extract column 3 vspeed
  makenabinput.sh ../indy2018-${car}.csv 3 indy2018-${car}-vspeed.csv
  # make nab files
  makejson.sh indy2018-${car}-vspeed.csv
  # runnab
  python -m indycar.runnab --skipConfirmation -d ${detector} --dataDir indycar --windowsFile indycar/indycar_labels.json
  # get results
  makepdf.sh indy2018-${car}-vspeed 0.992 ${detector}
done

#cp indy2018-${car}-vspeed*.pdf ../../

#done
echo ""
echo ""
echo "========================================================="
echo "Done, check indy2018-${car}-vspeed*.pdf"
