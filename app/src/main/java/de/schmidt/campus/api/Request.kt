package de.schmidt.campus.api
import android.util.Log
import com.google.gson.Gson
import de.schmidt.campus.api.response.WeekMenu
import java.lang.Exception
import java.net.URL
import kotlin.concurrent.thread

object Request {
    fun getWeeklyMenu(mensa: String, year: Int, weekNumber: Int, callback: (WeekMenu?) -> Unit) {
        //https://tum-dev.github.io/eat-api/<loc>/<year>/<week>.json
        val url = "https://tum-dev.github.io/eat-api/$mensa/$year/$weekNumber.json"
        Log.d("NetworkRequest", "Request to $url")

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