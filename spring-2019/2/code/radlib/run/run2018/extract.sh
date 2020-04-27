mkdir vspeed
cd vspeed/
for car in `cat ../carno `; do makenabinput.sh ../fulldata/indy2018-$car.csv 3 indy2018-$car-vspeed.csv; done
cd ..

mkdir vspeed-noyellowflag
cd vspeed-noyellowflag/
for car in `cat ../carno `; do python -m indycar.nabcsv --cmd prune --input ../vspeed/indy2018-$car-vspeed.csv --flagfile ../fulldata/flaginfo.csv --output indy2018-$car-vspeed-prune.csv; done
