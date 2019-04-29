Any dependencies that are required; throw a module error can be installed by doing `!pip install package_name` on the notebook itself.

INSTRUCTIONS ON HOW TO RUN THE NOTEBOOK:

FIRST: UNZIP THE ZIPPED CSV FILE. Note the location

EXPERIMENT 1: Naive Bayes:

1. Open `HPC - Naive Bayes.ipynb` 
2. HIT `CTRL + F` and search for `data location`
3. Enter file path to the unzipped CSV file `cleaned_HPC.csv`
4. Run the notebook


EXPERIMENT 2: Sequence Models:

1. Open `HPC - Sequence Models.ipynb` 

2. HIT `CTRL + F` and search for `data location`
3. Enter file path to the unzipped CSV file `cleaned_HPC.csv`

4. HIT `CTRL + F` and search for `#Change thread num here`
5. Change the number of threads

Similarly
6. HIT `CTRL + F` and search for `#Change number of epochs here` to change the number of epochs
7. HIT `CTRL + F` and search for `#Change number of units here` to change the number of LSTM units
8. HIT `CTRL + F` and search for `batch_size=1024`, and change the `batch_size`
9. Run all cells in the notebook sequentially

