package de.schmidt.campus.api
import com.google.gson.Gson
import de.schmidt.campus.api.response.WeekMenu
import java.lang.Exception
import java.net.URL
import kotlin.concurrent.thread

object Request {
    fun getWeeklyMenu(mensa: String, year: Int, weekNumber: Int, callback: (WeekMenu?) -> Unit) {
        //https://tum-dev.github.io/eat-api/<loc>/<year>/<day>.json
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