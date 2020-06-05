package de.schmidt.campus.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import de.schmidt.campus.R
import de.schmidt.campus.api.Request

class MenuListActivity : AppCompatActivity() {
    //todo implement a listview here which displays the food for specific days
    //only show today's plan first in the list, allow changing via fab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Campus"
        setContentView(R.layout.activity_menu_list)
    }

    override fun onResume() {
        super.onResume()
        Request.getWeeklyMenu("mensa-garching", 2019, 51) {
            runOnUiThread {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
            Log.v("MenuListActivity", it.toString())
        }
    }
}
