package com.example.weatherapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import androidx.appcompat.widget.SearchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchWeatherData("Chakwal")
        searchCity()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun searchCity() {
        val searchView = binding.searchView
        // Expand and focus search view properly
        searchView.isIconified = false
        searchView.clearFocus()
        // Change hint text and text color of SearchView
        val searchEditText = searchView.findViewById<android.widget.EditText>(
            androidx.appcompat.R.id.search_src_text
        )
        searchEditText.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.search, // your search icon drawable
            0, 0, 0
        )
        searchEditText.setHint("Search For A City") // set hint again explicitly
        searchEditText.setHintTextColor(resources.getColor(android.R.color.darker_gray))
        searchEditText.setTextColor(resources.getColor(android.R.color.black))
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || searchEditText.text.isNotEmpty()) {
                searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else {
                searchEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0)
            }
        }

        searchEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty() && !searchEditText.hasFocus()) {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0)
                } else {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                    searchView.clearFocus() // hide keyboard

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(
            cityName,
            "aa0361b6d49117aef702c19a43d6d9ce",
            "metric"
        )
        response.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min

                    binding.temp.text = "$temperature °C"
                    binding.weather.text = condition
                    binding.maxTemp.text = "Max Temp: $maxTemp °C"
                    binding.minTemp.text = "Min Temp: $minTemp °C"
                    binding.humidity.text = "$humidity %"
                    binding.windSpeed.text = "$windSpeed m/s"
                    binding.sunRise.text = "${time(sunRise)}"
                    binding.sunSet.text = "${time(sunSet)}"
                    binding.sea.text = "$seaLevel hPa"
                    binding.condition.text = condition
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.cityName.text = "$cityName"
                    changeimagesaccordingtoweathercondition(condition)
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Log.e(TAG, "API call failed: ${t.message}")
            }
        })
    }

    private fun changeimagesaccordingtoweathercondition(conditions:String) {
when(conditions){
    "Clear Sky", "Sunny", "Clear" -> {
        binding.root.setBackgroundResource(R.drawable.sunny_background)
        binding.lottieAnimationView.setAnimation(R.raw.sun)
    }

    "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy" -> {
        binding.root.setBackgroundResource(R.drawable.colud_background)
        binding.lottieAnimationView.setAnimation(R.raw.cloud)
    }

    "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
        binding.root.setBackgroundResource(R.drawable.rain_background)
        binding.lottieAnimationView.setAnimation(R.raw.rain)
    }

    "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
        binding.root.setBackgroundResource(R.drawable.snow_background)
        binding.lottieAnimationView.setAnimation(R.raw.snow)
    }
    else -> {
        binding.root.setBackgroundResource(R.drawable.sunny_background)
        binding.lottieAnimationView.setAnimation(R.raw.sun)
    }
}
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat( "HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }
    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
}
