package de.schmidt.campus.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import de.schmidt.campus.R
import de.schmidt.campus.api.Dish
import de.schmidt.campus.api.Ingredients
import de.schmidt.campus.api.Request
import de.schmidt.campus.view.DishListViewAdapter
import java.lang.StringBuilder

class MenuListActivity : AppCompatActivity() {
    //todo implement a listview here which displays the food for specific days
    //only show today's plan first in the list, allow changing via fab
    private var dishes: MutableList<Dish> = mutableListOf()
    private lateinit var listView: ListView
    private lateinit var adapter: DishListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Campus"
        setContentView(R.layout.activity_menu_list)

        //setup ListView
        listView = findViewById(R.id.food_list)
        adapter = DishListViewAdapter(this, dishes)
        listView.adapter = adapter
        listView.isClickable = true
        listView.setOnItemClickListener { parent, view, position, id ->
            //get the tapped dish
            val dish = dishes[position]
            val builder = StringBuilder()

            builder.append(dish.name)
            builder.append("\n")
            builder.append(dish.ingredients.mapNotNull { Ingredients.emoji[it] }.joinToString(separator = ""))
            builder.append("\n")

            //get descriptions for all the ingredients
            builder.append(
                dish.ingredients.joinToString(separator = "\n") { short ->
                    "• $short: " + getString(
                        Ingredients.ingredients[short] ?: 0
                    )
                }
            )

            Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        Request.getWeeklyMenu("mensa-garching", 2019, 51) {
            runOnUiThread {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                //todo build date selection logic into here
                updateUI(it.days[0].dishes)
            }
            Log.v("MenuListActivity", it.toString())
        }
    }

    private fun updateUI(newData: List<Dish>) {
        dishes.clear()
        dishes.addAll(newData)
        adapter.notifyDataSetChanged()
        listView.invalidateViews()
        listView.refreshDrawableState()
    }
}
