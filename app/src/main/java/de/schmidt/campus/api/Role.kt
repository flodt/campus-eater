package de.schmidt.campus.api

import androidx.annotation.StringRes
import de.schmidt.campus.R

enum class Role(@StringRes val role: Int) {
    STUDENT(R.string.visit_role_student), GUEST(R.string.visit_role_guest), STAFF(R.string.visit_role_staff)
}