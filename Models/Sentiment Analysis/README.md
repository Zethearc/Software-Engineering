# Sentiment Analysis

- Model: MobileBERT model 
- Model file: model_tr.tflite
- Details: 4.3x smaller and 5.5x faster than BERT-Base while achieving competitive results, suitable for on-device applications.
- Size: 24.5 MB
- Database: Kaggle Twitter Reddit dataset
- Link: https://www.kaggle.com/datasets/cosmos98/twitter-and-reddit-sentimental-analysis-dataset?datasetId=429085&sortBy=voteCount
- Code: BERT_Model_Sentiment_Analysis_Tutorial.ipynb 

# Google Colab

The BERT_Model_Sentiment_Analysis_Tutorial.ipynb file create the tflite model (model.tflite) using the Kaggle dataset and MobilBert model to train and test. The new model generated must be used in Android Studio to test.
