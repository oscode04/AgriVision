# AgriVision 🌱

![AgriVision Logo](https://github.com/oscode04/AgriVision/blob/main/Logo%20AgriVision.png)

AgriVision is a smart farming solution designed to empower farmers with cutting-edge technology for efficient farming. This application provides many features that are very useful for farmers. These features consist of fertilizer recommendation features, agricultural insights and chatbots that can help farmers in cultivating their agricultural land.

## Features 🚀

- **Fertilizer Recommendations**: Get accurate fertilizer suggestions based on your soil type, crop type, and environmental conditions.
- **weather forecast**: Real-time weather forecasts to help farmers plan planting and harvesting activities, avoiding potential weather-related risks.
- **Chatbots**: Interactive AI-based chatbot that answers questions on farming techniques, crop care, and provides personalized guidance based on user input.
- **User-Friendly Interface**: Easy-to-use platform for farmers, agricultural professionals, and researchers alike.

## Tech Stack 🛠️

- **Backend**: Flask API (deployed on Google Cloud)
- **Frontend**: kotlin for the Android mobile app
- **Machine Learning**: TensorFlow (TFLite for mobile inference)
- **Deployment**: Google Cloud Run for backend services
- **Weather API** Integrated with a third-party weather prediction API to provide real-time weather updates

## Machine Learning Model 🎯

The AgriVision app uses a custom-trained model on agricultural datasets, leveraging TensorFlow for the backend and TFLite for efficient mobile predictions. The model considers factors such as **soil type, nitrogen levels, phosphorous levels, potassium content, temperature**, and **humidity** to provide accurate recommendations.

### Model Training

- The model is trained using open-source datasets from [Kaggle](https://www.kaggle.com/datasets/dsmlindian/crop-fertilizers-for-crop).
- The training process involves preprocessing of both numerical and categorical data, with feature engineering to improve model accuracy.

## Deployment 🌍

The backend is deployed on **Google Cloud Run**, ensuring scalability and availability, while the frontend runs as a **Kotlin** mobile application.
