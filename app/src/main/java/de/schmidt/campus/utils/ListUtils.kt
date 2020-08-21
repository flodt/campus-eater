package de.schmidt.campus.utils

fun <E> List<E>.getOrDefault(index: Int, outOfBoundsReplacement: E): E =
    if (indices.contains(index)) get(index) else outOfBoundsReplacement