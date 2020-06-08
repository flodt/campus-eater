package de.schmidt.campus.api

import android.content.Context
import de.schmidt.campus.R

private const val PREF_KEY = "CampusEater"

object Ingredients {
    private val allergens: MutableList<String> = mutableListOf()

    val ingredients: Map<String, Int> = mapOf(
        "GQB" to R.string.bavaria_quality,
        "MSC" to R.string.marine_stewardship,
        "1" to R.string.with_dyestuff,
        "2" to R.string.with_preservative,
        "3" to R.string.with_antioxidant,
        "4" to R.string.with_flavour_enhancers,
        "5" to R.string.with_sulphur,
        "6" to R.string.with_blackened,
        "7" to R.string.with_waxed,
        "8" to R.string.with_phosphate,
        "9" to R.string.with_sweeteners,
        "10" to R.string.with_phenylalanine,
        "11" to R.string.with_sweet,
        "13" to R.string.with_cocoa_grease,
        "14" to R.string.with_gelatin,
        "99" to R.string.with_alcohol,
        "f" to R.string.with_meatless,
        "v" to R.string.with_vegan,
        "S" to R.string.with_pork,
        "R" to R.string.with_beef,
        "K" to R.string.with_veal,
        "G" to R.string.with_poultry,
        "W" to R.string.with_wild_meat,
        "L" to R.string.with_lamb,
        "Kn" to R.string.with_garlic,
        "Ei" to R.string.with_chegg,
        "En" to R.string.with_peanut,
        "Fi" to R.string.with_fish,
        "Gl" to R.string.with_gluten,
        "GlW" to R.string.with_wheat,
        "GlR" to R.string.with_rye,
        "GlG" to R.string.with_barley,
        "GlH" to R.string.with_oats,
        "GlD" to R.string.with_spelt,
        "Kr" to R.string.with_crustaceans,
        "Lu" to R.string.with_lupines,
        "Mi" to R.string.with_milk_lactose,
        "Sc" to R.string.with_shellfruits,
        "ScM" to R.string.with_almonds,
        "ScH" to R.string.with_hazelnuts,
        "ScW" to R.string.with_walnuts,
        "ScC" to R.string.with_cashew,
        "ScP" to R.string.with_pistachios,
        "Se" to R.string.with_sesame,
        "Sf" to R.string.with_mustard,
        "Sl" to R.string.with_celery,
        "So" to R.string.with_soy,
        "Sw" to R.string.with_sulfur_dioxide,
        "Wt" to R.string.with_mollusks
    )

    val emoji: Map<String, String> = mapOf(
        "99" to "\uD83C\uDF77",
        "S" to "\uD83D\uDC37",
        "R" to "\uD83D\uDC2E",
        "Ei" to "\uD83E\uDD5A",
        "En" to "\uD83E\uDD5C",
        "Fi" to "\uD83D\uDC1F",
        "GlW" to "\uD83C\uDF3E",
        "Mi" to "\uD83E\uDD5B"
    )

    private fun loadFromPrefs(context: Context) {
        context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            .getString(context.getString(R.string.pref_key_allergens), "")
            ?.split(",")
            ?.apply {
                allergens.clear()
                allergens.addAll(this)
            }
    }

    private fun saveToPrefs(context: Context) {
        context
            .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            .edit()
            .putString(context.getString(R.string.pref_key_allergens), allergens.joinToString(separator = ","))
            .apply()
    }

    fun containsAllergens(dish: Dish): Boolean = allergens.intersect(dish.ingredients).isNotEmpty()

    fun setAllergens(newData: List<String>, context: Context) {
        allergens.clear()
        allergens.addAll(newData)
        saveToPrefs(context)
    }
}