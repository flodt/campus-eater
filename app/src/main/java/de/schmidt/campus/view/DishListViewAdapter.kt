package de.schmidt.campus.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Paint
import android.text.Html
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

        if (Ingredients.containsAllergenWarnings(dish)) {
            name?.apply {
                //set the name with allergens
                text = "❌ ${dish.name}"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        } else {
            //remove strikethrough
            name?.apply {
                text = dish.name
                paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        //emojify and tag ingredients by color according to allergen settings
        var emojified = dish.ingredients.mapNotNull { Ingredients.emoji[it] }.joinToString(separator = "")
        if (emojified != "") emojified += " "
        emojified += dish.ingredients.joinToString(separator = ", ") { Ingredients.flagAllergensIn(it, context) }

        ingredients?.text = Html.fromHtml(emojified)

        return view
    }

    override fun getItem(position: Int): Any = dishes[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = dishes.size
}