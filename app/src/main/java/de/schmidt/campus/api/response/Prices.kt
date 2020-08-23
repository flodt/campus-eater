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

    fun getSelectedBasePrice(context: Context, dish: Dish): Double {
        return when (loadRoleFromPrefs(context)) {
            R.string.visit_role_student -> dish.prices.students.basePrice
            R.string.visit_role_guest -> dish.prices.guests.basePrice
            R.string.visit_role_staff -> dish.prices.staff.basePrice
            else -> throw IllegalStateException("Illegal category string")
        }
    }

    fun getSelectedUnitPrice(context: Context, dish: Dish): Double {
        return when (loadRoleFromPrefs(context)) {
            R.string.visit_role_student -> dish.prices.students.pricePerUnit
            R.string.visit_role_guest -> dish.prices.guests.pricePerUnit
            R.string.visit_role_staff -> dish.prices.staff.pricePerUnit
            else -> throw IllegalStateException("Illegal category string")
        }
    }

    fun getSelectedUnit(context: Context, dish: Dish): String {
        return when (loadRoleFromPrefs(context)) {
            R.string.visit_role_student -> dish.prices.students.unit
            R.string.visit_role_guest -> dish.prices.guests.unit
            R.string.visit_role_staff -> dish.prices.staff.unit
            else -> throw IllegalStateException("Illegal category string")
        }
    }
}