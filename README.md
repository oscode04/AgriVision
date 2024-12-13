
# AgriVision 🌱

<img src="https://github.com/oscode04/AgriVision/blob/main/Logo_AgriVision_Light.jpg" alt="AgriVision Logo" width="120" height="120">

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

## How to Use the Model 🎯

To use the machine learning model in the AgriVision app, follow the steps below:

### Preprocessing Input Data

1. The model accepts both numerical and categorical inputs. Categorical features such as soil type and crop type are encoded using `LabelEncoder` and `OneHotEncoder`.
2. You can preprocess input data using the provided `preprocess_input` function, which encodes categorical variables and merges them with numerical features.

```python
def preprocess_input(N, P, K, temperature, humidity, soil_type, crop_type):
    # ... preprocessing logic here ...
    return input_data.reshape(1, -1)  # Reshaped input ready for model inference
```

### Running Inference

1. Load the pre-trained TFLite model.
2. Preprocess the input data as described.
3. Run the inference using the pre-loaded model and retrieve the predicted fertilizer recommendation.

```python
# Load the model
interpreter = tf.lite.Interpreter(model_path='path_to_tflite_model.tflite')
interpreter.allocate_tensors()

# Preprocess input
test_data = preprocess_input(N=22, P=20, K=0, temperature=32, humidity=62, soil_type='Red', crop_type='Tobacco')

# Set input tensor
interpreter.set_tensor(input_details[0]['index'], test_data.astype(np.float32))

# Run inference
interpreter.invoke()

# Get prediction
prediction = interpreter.get_tensor(output_details[0]['index'])
fertilizer_name = fertilizer_encoder.inverse_transform([np.argmax(prediction)])
print(f"Predicted Fertilizer: {fertilizer_name[0]}")
```

### Example

Example of a test input:

```python
test_data = preprocess_input(
    N=22, 
    P=20, 
    K=0, 
    temperature=32, 
    humidity=62, 
    soil_type='Red', 
    crop_type='Tobacco'
)
```

### Output

The output will be the name of the recommended fertilizer, for example:

```
Predicted Fertilizer: Urea
```

## Folder Structure

```
- machine learning/
  - agrivision_model.keras  # Saved model
  - tflite/  # TFLite converted models
```
## Team Members 👥
| Bangkit ID       | Name                     | Learning Path      | University                        | Contact                                                                                       |
|-----------------|--------------------------|---------------------|-----------------------------------|------------------------------------------------------------------------------------------------|
| M177B4KY0206    | Ahmad Gaos Sanusi Sulalah  | Machine Learning     | Sekolah Tinggi Manajemen Informatika dan Komputer Indo Daya Suvana         | [LinkedIn](https://www.linkedin.com/in/ahmad-gaos-sanusi-sulalah/)                     |
| M384B4KX3926    | Rizky Asmi Fadillah            | Machine Learning     | Universitas Muhammadiyah Sumatera Utara         | [LinkedIn](https://www.linkedin.com/in/rizky-asmi-fadillah/)                                         |
| M277B4KX3401    | Novi Ken Sydney        | Machine Learning  | Universitas Negeri Jakarta        | [LinkedIn](https://www.linkedin.com/in/novi-ken-sydney-797200214/)                         |
| C128B4KX1067    | Devina Anggraini  | Cloud Computing    | Politeknik Negeri Jakarta | [LinkedIn](https://www.linkedin.com/in/devina-anggraini-b0488624b/)                               |
| C128B4KY0387    | Alif Rendina Pamungkas    | Cloud Computing    | Politeknik Negeri Jakarta | [LinkedIn](https://www.linkedin.com/in/lifrenkas/)                       |
| A128B4KY3459    | Okta Gabriel Sinsaku Sinaga        | Mobile Development    | Politeknik Negeri Jakarta | [LinkedIn](https://www.linkedin.com/in/okta-gabriel-sinsaku-sinaga-665106300/)                         |
| A128B4KY0966    | Daffa Ian Nabil        | Mobile Development    | Politeknik Negeri Jakarta | [LinkedIn](https://www.linkedin.com/in/bybeil/)                         |
