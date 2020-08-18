package de.schmidt.campus.api

import android.content.Context
import de.schmidt.campus.R
import de.schmidt.campus.utils.PREF_KEY

val locations: Map<String, String> = mapOf(
    "mensa-arcisstr" to "Mensa Arcisstraße",
    "mensa-garching" to "Mensa Garching"//, etc. (and extract resources!)
)

fun getSelectedLocation(context: Context): String {
    return context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        .getString(context.getString(R.string.pref_key_location), "mensa-arcisstr") ?: "mensa-arcisstr"
}

fun setSelectedLocation(loc: String, context: Context) {
    context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        .edit()
        .putString(context.getString(R.string.pref_key_location), loc)
        .apply()
}
