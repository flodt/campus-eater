package de.schmidt.campus.utils

fun String.orIfBlank(other: String): String = if (this.isBlank()) other else this

fun String.appendIfNotBlank(partial: String): String = if (this.isBlank()) this else this + partial