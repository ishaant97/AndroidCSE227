package com.example.androidcse227.Unit3


import com.example.androidcse227.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RetrofitActivity : AppCompatActivity() {

    private val API_KEY = "315f293daca93eaf2847d326cec66326"   // <<< CHANGE THIS

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)

        val etCity = findViewById<EditText>(R.id.etCity)
        val btnGet = findViewById<Button>(R.id.btnGet)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnGet.setOnClickListener {

            val city = etCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Enter city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {

                tvResult.text = "Loading..."

                try {
                    val response = RetrofitInstance.api.getWeather(city, API_KEY)

                    if (response.isSuccessful) {
                        val weather = response.body()

                        val temp = weather?.main?.temp ?: "--"
                        val desc = weather?.weather?.firstOrNull()?.description ?: "--"
                        val hum = weather?.main?.humidity ?: "--"
                        val name = weather?.name ?: city

                        tvResult.text = """
                            City: $name
                            Temperature: $temp °C
                            Condition: $desc
                            Humidity: $hum%
                        """.trimIndent()

                    } else {
                        tvResult.text = "Error: ${response.code()}"
                    }

                } catch (e: IOException) {
                    tvResult.text = "Network error"
                } catch (e: HttpException) {
                    tvResult.text = "Server error"
                } catch (e: Exception) {
                    tvResult.text = "${e.message}"
                }
            }
        }
    }
}