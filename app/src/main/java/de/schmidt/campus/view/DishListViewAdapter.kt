package de.schmidt.campus.view

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import de.schmidt.campus.R
import de.schmidt.campus.api.Dish
import de.schmidt.campus.api.Ingredients

class DishListViewAdapter constructor(
    private val context: Activity,
    private val dishes: List<Dish>
): BaseAdapter () {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.food_list_item, null)

        //fields
        val name = view.findViewById<TextView>(R.id.food_list_item_name)
        val ingredients = view.findViewById<TextView>(R.id.food_list_item_ingredients)

        //fill with dish data
        val dish: Dish = getItem(position) as Dish
        name?.text = dish.name

        var emojified = dish.ingredients.mapNotNull { Ingredients.emoji[it] }.joinToString(separator = "")
        if (emojified != "") emojified += " "
        emojified += dish.ingredients.joinToString(separator = ", ")

        ingredients?.text = emojified

        return view
    }

    override fun getItem(position: Int): Any = dishes[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = dishes.size
}