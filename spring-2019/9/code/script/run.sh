#!/bin/bash

declare -a arr=("1000" "10000" "100000" "500000" "1000000")
time=()
#> out.txt

#echo "Executors: $1, Cores: $2, Executor Memory: $3" >> out.log

#for i in "${arr[@]}"
#do
   #filename="2006_"$i".csv"
   filename="2006.csv"
   START=$(date +%s)
   #/N/u/jaikumar/spark-2.1.1-bin-hadoop2.7/bin/spark-submit /N/u/jaikumar/script/rf.py $filename
   /N/u/jaikumar/spark-2.1.1-bin-hadoop2.7/bin/spark-submit --num-executors $1 --executor-cores $2 --executor-memory $3 --driver-memory 1g /N/u/jaikumar/script/rfcopy2.py $filename 2>&1 | tee run.log
   END=$(date +%s)
   DIFF=$(( $END - $START ))
   time+=($DIFF)
   echo "$filename:  Executors: $1, Cores: $2, Executor Memory: $3         $DIFF seconds		$accuracy" >> out.txt
   
#done

