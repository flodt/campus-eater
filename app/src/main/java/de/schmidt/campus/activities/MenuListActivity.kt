package de.schmidt.campus.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
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
import de.schmidt.campus.api.response.Dish
import de.schmidt.campus.api.Ingredients
import de.schmidt.campus.api.Locations
import de.schmidt.campus.api.Request
import de.schmidt.campus.utils.getOrDefault
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

        //apply mode-sensitive colors
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.action_bar)))
        window?.statusBarColor = getColor(R.color.status_bar)

        title = "Campus"
        setContentView(R.layout.activity_menu_list)

        //get current date text field
        currentDate = Date()
        currentDateView = findViewById(R.id.menu_list_date)

        //setup fab for date choosing
        floatingActionButton = findViewById(R.id.menu_list_fab_date)
        floatingActionButton.setOnClickListener { onFabInvokeDatePicker() }

        //for debugging purposes: todo remove later
        floatingActionButton.isLongClickable = true
        floatingActionButton.setOnLongClickListener {
            currentDate = GregorianCalendar(2019, 11, 19).time
            refresh()
            true
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
        listView.setOnItemClickListener { _, _, position, _ -> onDishClicked(position) }
        listView.isLongClickable = true
        listView.setOnItemLongClickListener { _, _, position, _ -> onDishLongClicked(position) }

        Ingredients.updateStoreFromPrefs(this)
    }

    private fun onDishLongClicked(position: Int): Boolean {
        val dish = dishes[position]
        val builder = StringBuilder()

        builder.append(dish.ingredients.joinToString())

        if (Ingredients.containsAllergenWarnings(dish)) {
            builder.append("\n\nWarnings:\n")
            builder.append(
                dish.ingredients
                    .filter { Ingredients.getAllergenWarnings(this).contains(it) }
                    .joinToString(separator = "\n") { short ->
                        "• $short: " + getString(
                            Ingredients.ingredients[short] ?: 0
                        )
                    }
            )
        }

        if (Ingredients.containsAllergenCautions(dish)) {
            builder.append("\n\nCautions:\n")
            builder.append(
                dish.ingredients
                    .filter { Ingredients.getAllergenCautions(this).contains(it) }
                    .joinToString(separator = "\n") { short ->
                        "• $short: " + getString(
                            Ingredients.ingredients[short] ?: 0
                        )
                    }
            )
        }

        Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show()
        return true
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
            R.id.action_set_location -> {
                onSetLocation()
                true
            }
            R.id.action_set_allergy_warnings -> {
                onSetAllergyWarnings()
                true
            }
            R.id.action_set_allergy_cautions -> {
                onSetAllergyCautions()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onUpdateAllergySettings(elements: List<String>, updater: (List<String>) -> Unit) {
        val checked: BooleanArray = Ingredients.ingredients.keys
            .toList()
            .map { elements.contains(it) }
            .toBooleanArray()

        val described = Ingredients.ingredients.entries
            .map { it.key + " - " + getString(it.value) }
            .toTypedArray()

        AlertDialog.Builder(this)
            .setTitle(R.string.set_allergen_warnings)
            .setMultiChoiceItems(
                described,
                checked
            ) { _, which, isChecked ->
                checked[which] = isChecked
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setNeutralButton("Clear all") { dialog, _ ->
                dialog.dismiss()
                updater(listOf())
                refresh()
            }
            .setPositiveButton("OK") { dialog, _ ->
                //save the allergens
                checked.mapIndexed { index, checked ->
                    if (checked) Ingredients.ingredients.keys.toList()[index] else null
                }
                    .filterNotNull()
                    .let { updater(it) }

                dialog.dismiss()
                refresh()
            }
            .create()
            .show()
    }

    private fun onSetAllergyCautions() {
        onUpdateAllergySettings(Ingredients.getAllergenCautions(this)) { newCautions ->
            Ingredients.setAllergenCautions(newCautions, this)
        }
    }

    private fun onSetAllergyWarnings() {
        onUpdateAllergySettings(Ingredients.getAllergenWarnings(this)) { newWarnings ->
            Ingredients.setAllergenWarnings(newWarnings, this)
        }
    }

    private fun onSetLocation() {
        var selectedLocationId = Locations.getSelectedLocation(this)
        AlertDialog.Builder(this)
            .setTitle(R.string.set_location_title)
            .setSingleChoiceItems(
                Locations.names.values.toTypedArray().map { getString(it) }.toTypedArray(),
                Locations.names.keys.toTypedArray().indexOf(selectedLocationId)
            ) { _, which ->
                selectedLocationId = Locations.names.keys.toTypedArray()[which]
            }
            .setNegativeButton("Dismiss") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("OK") { dialog, _ ->
                Locations.setSelectedLocation(selectedLocationId, this)
                dialog.dismiss()
                refresh()
            }
            .create()
            .show()
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
                val fetched = it?.days?.getOrDefault(selectedWeekDay, null)?.dishes ?: listOf()
                updateUI(fetched)
                swipeRefresh.isRefreshing = false

                //display notice if we didn't get anything back
                if (fetched.isEmpty()) {
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
