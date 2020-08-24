package de.schmidt.campus.api.response


import android.content.Context
import com.google.gson.annotations.SerializedName
import de.schmidt.campus.R
import de.schmidt.campus.utils.PREF_KEY

data class Prices(
    @SerializedName("guests")
    val guests: Guests = Guests(),
    @SerializedName("staff")
    val staff: Staff = Staff(),
    @SerializedName("students")
    val students: Students = Students()
) {
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

    fun getSelectedBundle(context: Context): PriceBundle {
        return when (loadRoleFromPrefs(context)) {
            R.string.visit_role_student -> students
            R.string.visit_role_guest -> guests
            R.string.visit_role_staff -> staff
            else -> throw IllegalStateException("Illegal category string")
        }
    }
}