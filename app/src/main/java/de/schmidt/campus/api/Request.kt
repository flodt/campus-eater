package de.schmidt.campus.api
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import java.lang.Exception
import java.net.URL
import kotlin.concurrent.thread

object Request {
    fun getWeeklyMenu(mensa: String, year: Int, weekNumber: Int, callback: (WeekMenu?) -> Unit) {
        //todo new API url: https://github.com/TUM-Dev/eat-api
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