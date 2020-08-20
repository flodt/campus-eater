package de.schmidt.campus.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class MenuListActivity : AppCompatActivity() {
    private var dishes: MutableList<Dish> = mutableListOf()
    private lateinit var listView: ListView
    private lateinit var adapter: DishListViewAdapter

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var currentDateView: TextView
    private lateinit var currentDate: Date

    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Campus"
        setContentView(R.layout.activity_menu_list)

        //get current date text field
        currentDate = Date()
        currentDateView = findViewById(R.id.menu_list_date)

        //setup fab for date choosing
        floatingActionButton = findViewById(R.id.menu_list_fab_date)
        floatingActionButton.setOnClickListener { onFabInvokeDatePicker() }

        //setup swipe refresh layout
        swipeRefresh = findViewById(R.id.menu_list_swipe_refresh)
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeRefresh.setOnRefreshListener { refresh() }

        //setup ListView
        listView = findViewById(R.id.food_list)
        adapter = DishListViewAdapter(this, dishes)
        listView.adapter = adapter
        listView.isClickable = true
        listView.setOnItemClickListener { _, _, position, _ -> onDishClicked(position) }

        //todo set ingredients for allergen notes
        Ingredients.setAllergenWarnings(listOf("Mi"), this)
        Ingredients.setAllergenCautions(listOf("Kn"), this)
    }

    private fun onDishClicked(position: Int) {
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

        //later create a custom toast with colored allergens
        Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show()
    }

    private fun onFabInvokeDatePicker() {
        //setup dialog with current date and time
        val cal = Calendar.getInstance()
        cal.time = currentDate

        val listener: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener() { _, year, month, dayOfMonth ->
                currentDate = GregorianCalendar(year, month, dayOfMonth).time
                refresh()
            }

        val picker = DatePickerDialog(
            this,
            listener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        picker.show()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                refresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun refresh() {
        runOnUiThread { swipeRefresh.isRefreshing = true }

        val selectedLocation = getString(Locations.getSelectedLocation(this))

        val cal = Calendar.getInstance()
        cal.time = currentDate
        val selectedYear = cal.get(Calendar.YEAR)
        val selectedWeek = cal.get(Calendar.WEEK_OF_YEAR)
        val selectedWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1

        Request.getWeeklyMenu(selectedLocation, selectedYear, selectedWeek) {
            runOnUiThread {
                updateUI(it?.days?.get(selectedWeekDay)?.dishes ?: listOf())
                swipeRefresh.isRefreshing = false

                //display notice if we didn't get anything back
                if (it == null) {
                    Toast.makeText(this, R.string.error_no_data, Toast.LENGTH_SHORT).show()
                }
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

        //set the date correctly
        var formatted = SimpleDateFormat("EEEE, dd. MMMM yyyy").format(currentDate)
        val deltaDays = ((currentDate.time - Date().time) / (24 * 60 * 60 * 1000)).toInt()

        if (deltaDays.absoluteValue <= 31) {
            formatted += when {
                (deltaDays > 0) -> " (in $deltaDays days)"
                (deltaDays < 0) -> " (${-deltaDays} days ago)"
                else -> " (Today)"
            }
        }

        currentDateView.text = formatted
    }
}
