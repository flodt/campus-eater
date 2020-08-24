package de.schmidt.campus.api.response


import android.content.Context
import com.google.gson.annotations.SerializedName
import de.schmidt.campus.R
import de.schmidt.campus.api.Roles.loadRoleFromPrefs

data class Prices(
    @SerializedName("guests")
    val guests: Guests = Guests(),
    @SerializedName("staff")
    val staff: Staff = Staff(),
    @SerializedName("students")
    val students: Students = Students()
) {
    fun getSelectedBundle(context: Context): PriceBundle {
        return when (loadRoleFromPrefs(context)) {
            R.string.visit_role_student -> students
            R.string.visit_role_guest -> guests
            R.string.visit_role_staff -> staff
            else -> throw IllegalStateException("Illegal category string")
        }
    }
}