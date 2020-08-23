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
import de.schmidt.campus.api.response.Dish
import de.schmidt.campus.api.Ingredients
import de.schmidt.campus.utils.orIfBlank

class DishListViewAdapter constructor(
    private val context: Activity,
    private val dishes: List<Dish>
): BaseAdapter () {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.food_list_item, null)
        view.setBackgroundColor(context.getColor(R.color.background))

        //fields
        val name = view.findViewById<TextView>(R.id.food_list_item_name)
        val ingredients = view.findViewById<TextView>(R.id.food_list_item_ingredients)

        //fill with dish data
        val dish: Dish = getItem(position) as Dish

        when {
            Ingredients.containsAllergenWarnings(dish) -> {
                name?.apply {
                    //set the name with allergens
                    text = "❌ ${dish.name}"
                    //paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                view.setBackgroundColor(context.resources.getColor(R.color.background_shade))
            }
            Ingredients.containsAllergenCautions(dish) -> {
                name?.apply {
                    text = "⚠️ ${dish.name}"
                }
            }
            else -> {
                //remove strikethrough
                name?.apply {
                    text = dish.name
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }

        //emojify and tag ingredients by color according to allergen settings
        var emojified = "\uD83C\uDF74" + dish.ingredients.mapNotNull { Ingredients.emoji[it] }.joinToString(separator = "")
        if (emojified != "\uD83C\uDF74") emojified += " "
        emojified += dish
            .ingredients
            .joinToString(separator = ", ") { Ingredients.flagAllergensIn(it, context) }
            .orIfBlank("–")

        ingredients?.text = Html.fromHtml(emojified)

        return view
    }

    override fun getItem(position: Int): Any = dishes[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = dishes.size
}