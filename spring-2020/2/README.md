PROJECT NAME:  
Ranking Prediction for Indianapolis 500 Race

GROUP MEMBERS:  
Yashaswini Dhatrika and Utkarsh Kumar 

PROJECT SUMMARY:  
While being one of the most challenging problems, forecasting and predicting ranking is being explored and has been gaining popularity with availability of more data and improved prediction techniques in real time. We present the ranking prediction algorithms which predicts the leading car in Indianapolis 500 race which is held every year by using 7 years of data from IndyStats website. Our approach is to extract and transform data to the desirable form, then perform exploratory analysis in order to understand the key features for the ranking and then apply different algorithms (Linear, Bagging, Boosting and DeepLearningModels(LSTM,Seq2Seq, Seq2SeqwithAttention) models for predicting the rank. As the last step of process the comparative study is performed to analysis the pros/cons of each model deployed.

METHODOLOGY:  
The steps followed for the lead rank prediction are data extraction, data pre-processing, exploratory data analysis and application of
various models like Linear Models(ARIMA), Random Forest, Gradient Boosting Regression, Deep Learning Models(LSTM, Encoder-Decoder with attention). And in the final step the models are compared based on various factors like accuracy, stability, time complexity etc.

CONCLUSION:  
(1) From EDA, the starting position, last pit stop displayed correlation with rank  
(2) By deploying various models, we found the Seq2Seq Attention Model has performed well while compared to other models.  
(3) But when you consider the stability or variation of accuracy then the Random Forest and Gradient Boosting models are stable over other Deep Learning Models.  
  
![alt text](https://github.com/utkarsh3142/E599-high-performance-big-data/blob/master/spring-2020/2/code/output_data/comparison.PNG)
