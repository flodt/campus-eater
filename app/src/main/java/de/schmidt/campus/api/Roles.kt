package de.schmidt.campus.api

import android.content.Context
import de.schmidt.campus.R
import de.schmidt.campus.utils.PREF_KEY

object Roles {
    fun saveRoleToPrefs(context: Context, role: Int) {
        context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            .edit()
            .putInt(context.getString(R.string.pref_key_price_role), role)
            .apply()
    }

    fun loadRoleFromPrefs(context: Context): Int {
        return context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            .getInt(context.getString(R.string.pref_key_price_role), R.string.visit_role_student)
    }
}