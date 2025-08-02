# 🌤️ Weather App - Real-Time Weather Forecast

The **Weather App** is a sleek and modern Android application developed in Kotlin using Android Studio. It allows users to check real-time weather conditions for any city around the world. The app fetches data from the OpenWeatherMap API and presents it in a user-friendly interface, with dynamic backgrounds and temperature visuals based on weather conditions.

---

## 🚀 Features Overview

### 🌍 1. **City-Based Weather Search**
- Users can search weather by city name.
- Autocomplete with suggestions (optional enhancement).
- Fetches real-time data from **OpenWeatherMap API**.

### 📱 2. **Dynamic UI**
- Changes background or theme based on weather condition (e.g., sunny, rainy, cloudy).
- Real-time temperature, humidity, wind speed, and pressure displayed.
- Icons and animations for various weather types.

### 🕒 3. **Current Weather Details**
- Temperature (°C)
- Humidity (%)
- Wind Speed (km/h)
- Atmospheric Pressure (hPa)
- Weather Description (e.g., Light Rain, Clear Sky)

### 📍 4. **Location Integration (Planned)**
- Use GPS to fetch current location’s weather.
- Request and handle runtime location permissions.

### 🔍 5. **Search History (Planned Enhancement)**
- Store last 5 searched cities using `SharedPreferences` or local database.
- Quick access buttons to re-check previous cities.

### 🌐 6. **Internet Connectivity Check**
- Alert the user if there is no internet connection.
- Gracefully handle API errors or empty responses.

---

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: XML Layouts with ConstraintLayout
- **API**: [OpenWeatherMap API](https://openweathermap.org/api)
- **Architecture**: MVVM (optional for future upgrades)
- **Networking**: Retrofit / Volley / HttpURLConnection
- **Image Icons**: From OpenWeatherMap or custom vector drawables

---

## 📦 Dependencies Used
- Retrofit (or similar) for API communication
- Gson for JSON parsing
- Glide (optional) for image loading
- Material Design components

---

## 📌 Future Enhancements
- Add 7-day forecast.
- Dark Mode toggle.
- Search with voice command.
- Temperature unit toggle (Celsius ↔ Fahrenheit).
- Widgets for home screen weather display.

---

## 🔐 Permissions Required
- **INTERNET**: To fetch weather data.
