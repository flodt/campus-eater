package de.schmidt.campus.activities

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.schmidt.campus.R
import de.schmidt.campus.api.Dish
import de.schmidt.campus.api.Ingredients
import de.schmidt.campus.api.Locations
import de.schmidt.campus.api.Request
import de.schmidt.campus.view.DishListViewAdapter

class MenuListActivity : AppCompatActivity() {
    private var dishes: MutableList<Dish> = mutableListOf()
    private lateinit var listView: ListView
    private lateinit var adapter: DishListViewAdapter

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var currentDate: TextView

    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Campus"
        setContentView(R.layout.activity_menu_list)

        //get current date text field
        currentDate = findViewById(R.id.menu_list_date)

        //setup fab for date choosing
        floatingActionButton = findViewById(R.id.menu_list_fab_date)
        floatingActionButton.setOnClickListener {
            runOnUiThread {
                Toast.makeText(this, "Select date...", Toast.LENGTH_SHORT).show()
            }
        }

        //setup swipe refresh layout
        swipeRefresh = findViewById(R.id.menu_list_swipe_refresh)
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeRefresh.setOnRefreshListener { refresh() }

        //setup ListView
        listView = findViewById(R.id.food_list)
        adapter = DishListViewAdapter(this, dishes)
        listView.adapter = adapter
        listView.isClickable = true
        listView.setOnItemClickListener { _, _, position, _ ->
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

            builder.append(dish.ingredients.joinToString { Ingredients.flagAllergensIn(it, this) })

            Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show()
        }

        //todo set ingredients for allergen notes
        Ingredients.setAllergenWarnings(listOf("Mi"), this)
        Ingredients.setAllergenCautions(listOf("Sl"), this)
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        runOnUiThread { swipeRefresh.isRefreshing = true }

        val selectedLocation = getString(Locations.getSelectedLocation(this))
        val selectedYear = 2019
        val selectedWeek = 51
        val selectedWeekDay = 0

        Request.getWeeklyMenu(selectedLocation, selectedYear, selectedWeek) {
            runOnUiThread {
                updateUI(it.days[selectedWeekDay].dishes)
                swipeRefresh.isRefreshing = false
            }
            Log.v("MenuListActivity", it.toString())
        }
    }

    private fun updateUI(newData: List<Dish>) {
        //update list view via adapter
        dishes.clear()
        dishes.addAll(newData)

        //sort data according to allergen notices
        dishes.sortBy {
            when {
                Ingredients.containsAllergenWarnings(it) -> 2
                Ingredients.containsAllergenCautions(it) -> 1
                else -> 0
            }
        }

        adapter.notifyDataSetChanged()
        listView.invalidateViews()
        listView.refreshDrawableState()

        //set title correctly
        Locations.names[Locations.getSelectedLocation(this)]?.let { setTitle(it) }
    }
}
