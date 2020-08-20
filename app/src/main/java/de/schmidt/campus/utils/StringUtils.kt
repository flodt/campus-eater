package de.schmidt.campus.utils

fun String.orIfBlank(other: String): String = if (this.isBlank()) other else this