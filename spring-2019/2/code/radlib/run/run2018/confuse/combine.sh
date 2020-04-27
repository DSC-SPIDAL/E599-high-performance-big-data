 gawk -F, '{print $3}' numenta_indy2018-1-vspeed-confuse.csv > score.out
 gawk -F, '{printf("%s,%s,\n",$1,$2)}' numenta_indy2018-1-vspeed.csv > input.out
 paste input.out score.out |gawk -F, '{printf("%s,%s,%s\n",$1,$2,$3)}' >combined.result
 cp combined.result ../results/numenta/indycar/numenta_indy2018-1-vspeed-confuse.csv
