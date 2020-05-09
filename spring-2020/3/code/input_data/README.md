The cleaned dataset can be read through the following line

```python
clean_dataset = pd.read_excel('telemetry_cleaned.xlsx', index_col=0) 
```
Then matrix needs normalized through:

```python
scaler = MinMaxScaler()
scaled = scaler.fit_transform(new_data)  
``` 

Use 10% as trainning (38182), and 5% (19065) as test, and select speed and distance as two features
