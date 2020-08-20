package de.schmidt.campus.api

import android.content.Context
import de.schmidt.campus.R
import de.schmidt.campus.utils.PREF_KEY

object Locations {
    val names: Map<Int, Int> = mapOf(
        R.string.short_mensa_arcisstr to R.string.full_mensa_arcisstr,
        R.string.short_mensa_garching to R.string.full_mensa_garching,
        R.string.short_mensa_leopoldstr to R.string.full_mensa_leopoldstr,
        R.string.short_mensa_lothstr to R.string.full_mensa_lothstr,
        R.string.short_mensa_martinsried to R.string.full_mensa_martinsried,
        R.string.short_mensa_pasing to R.string.full_mensa_pasing,
        R.string.short_mensa_weihenstephan to R.string.full_mensa_weihenstephan,
        R.string.short_stubistro_arcisstr to R.string.full_stubistro_arcisstr,
        R.string.short_stubistro_goethestr to R.string.full_stubistro_goethestr,
        R.string.short_stubistro_grosshadern to R.string.full_stubistro_grosshadern,
        R.string.short_stubistro_rosenheim to R.string.full_stubistro_rosenheim,
        R.string.short_stubistro_schellingstr to R.string.full_stubistro_schellingstr,
        R.string.short_stucafe_adalbertstr to R.string.full_stucafe_adalbertstr,
        R.string.short_stucafe_akademie_weihenstephan to R.string.full_stucafe_akademie_weihenstephan,
        R.string.short_stucafe_boltzmannstr to R.string.full_stucafe_boltzmannstr,
        R.string.short_stucafe_garching to R.string.full_stucafe_garching,
        R.string.short_stucafe_karlstr to R.string.full_stucafe_karlstr,
        R.string.short_stucafe_pasing to R.string.full_stucafe_pasing,
        R.string.short_fmi_bistro to R.string.full_fmi_bistro,
        R.string.short_ipp_bistro to R.string.full_ipp_bistro
    )

    fun getSelectedLocation(context: Context): Int {
        return context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            .getInt(context.getString(R.string.pref_key_location), R.string.short_mensa_arcisstr)
    }

    fun setSelectedLocation(loc: Int, context: Context) {
        context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            .edit()
            .putInt(context.getString(R.string.pref_key_location), loc)
            .apply()
    }
}
