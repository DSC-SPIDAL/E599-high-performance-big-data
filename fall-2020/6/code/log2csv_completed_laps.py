import glob
import csv

CarNo = '12'
files = ['IPBroadcaster_Input_2018-05-27_0.log']
# converts hexadecimal columns to decimal
def hex2dec(record, cols):
    for col in cols:
        record[col] = int(record[col], 16)


# Time columns are in ten thousandths sec. Converts to sec
def tenThousandsToMins(record, cols):
    for col in cols:
        record[col] = record[col] / 10000.0

# Convert Time format
# HH:MM:SS.sss to SS+60MM+3600HH
def Time2Float(record, cols):
    for col in cols:
        st = record[col]
        arr = st.split(':' )
        arr.reverse()
        sum = 0
        for i in range(len(arr)):
            sum = sum + float(arr[i])* (60**i)
        record[col] = sum


csv_file_path = "tele12.csv"
header= ['command','No','Time Of Day','Lap Distance','Vehicle Speed','Engine Speed','Gear','Brake','Throttle']
time_cols= ['Time Of Day']



csv_file = open(csv_file_path, 'w',  newline='')
writer = csv.DictWriter(csv_file, fieldnames=header)
writer.writeheader()

for file in files:
    with open(file,'r', encoding="cp1252") as f:
        for line in f:
            if line.startswith("$P"):
                raw_record = line.replace('\n', '').split('¦')
                record = {}
                for idx, val in enumerate(raw_record):
                    if (idx<9):
                        record[header[idx]] = val
                Time2Float(record, time_cols)
                # ignore wrong time format
                #if (record['Time Of Day'] < 10000):
                 #   continue
 
                if (record['No'] == CarNo and  record['Vehicle Speed'] != '0.000' and record['Lap Distance'] != '0.00'):
                    writer.writerow(record)
csv_file.close()

