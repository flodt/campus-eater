package de.schmidt.campus.api
import android.widget.Toast
import com.google.gson.Gson
import java.lang.Exception
import java.net.URL
import kotlin.concurrent.thread

object Request {
    fun getWeeklyMenu(mensa: String, year: Int, weekNumber: Int, callback: (WeekMenu?) -> Unit) {
        //url: https://srehwald.github.io/eat-api/<location>/<year>/<week-number>.json
        val url: String = "https://srehwald.github.io/eat-api/$mensa/$year/$weekNumber.json"

        //do the network request
        thread {
            try {
                callback(Gson().fromJson(URL(url).readText(), WeekMenu::class.java))
            } catch (e: Exception) {
                callback(null)
            }
        }
    }
}