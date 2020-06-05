package de.schmidt.campus.api
import com.google.gson.Gson
import java.net.URL
import kotlin.concurrent.thread

object Request {
    fun getWeeklyMenu(mensa: String, year: Int, weekNumber: Int, callback: (WeekMenu) -> Unit) {
        //url: https://srehwald.github.io/eat-api/<location>/<year>/<week-number>.json
        val url: String = "https://srehwald.github.io/eat-api/$mensa/$year/$weekNumber.json"

        //do the network request
        thread {
            callback(Gson().fromJson(URL(url).readText(), WeekMenu::class.java))
        }
    }
}