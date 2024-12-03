package com.example.weatherapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.weatherapplication.HttpRequest.Companion.executeGet
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    val city: String?= "Dhaka"
    val apiKey: String?= "7733400e03df3db253b6c1f5060ef9c0"

    lateinit var cityText: TextView
    lateinit var conditionText: TextView
    lateinit var temperatureText: TextView
    lateinit var humidityText: TextView
    lateinit var pressureText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        cityText= findViewById<TextView>(R.id.city)
        conditionText= findViewById<TextView>(R.id.condition)
        temperatureText= findViewById<TextView>(R.id.temperature)
        humidityText= findViewById<TextView>(R.id.humidity)
        pressureText= findViewById<TextView>(R.id.pressure)

        getWeather()


    }



    fun getWeather() {
        lifecycleScope.launch {
            val progressBar = findViewById<ProgressBar>(R.id.loader)
            val mainContainer = findViewById<RelativeLayout>(R.id.main)
            val errorText = findViewById<TextView>(R.id.errorText)

            progressBar.visibility = View.VISIBLE
            mainContainer.visibility = View.GONE
            errorText.visibility = View.GONE

            val url = "https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${apiKey}"
            val response = withContext(IO) {
                try {
                    executeGet(url)
                }catch (e: Exception)
                {
                    Log.e("WeatherApp", "Network error: ${e.message}")
                    null
                }
            }

            try {
                if (response!=null) {
                    val weatherData: JSONObject = Gson().fromJson(response, JSONObject::class.java)

                    val main = weatherData.getJSONObject("main")
                    val sys = weatherData.getJSONObject("sys")
                    val weather = weatherData.getJSONArray("weather").getJSONObject(0)

                    val temperature = Utils.toCelcius(main.getString("temp")) + "°C"
                    val humidity = main.getString("humidity")
                    val pressure = main.getString("pressure")

                    val condition = weather.getString("main")
                    val address = weatherData.getString("name") + ", " + sys.getString("country")

                    withContext(Dispatchers.Main) {
                        temperatureText.text = temperature
                        conditionText.text = condition
                        cityText.text = address
                        humidityText.text = humidity
                        pressureText.text = pressure
                    }

                    progressBar.visibility = View.GONE
                    mainContainer.visibility = View.VISIBLE
                }
            } catch (e: JSONException) {
                Log.e("WeatherApp", "Error parsing JSON", e)
                // Handle the error here, e.g., display an error message
                withContext(Dispatchers.Main) {
                    errorText.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }


}
