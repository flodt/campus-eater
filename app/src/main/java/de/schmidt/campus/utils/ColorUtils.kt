package de.schmidt.campus.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.ColorRes

fun String.colorWithHtml(color: String): String {
    return "<font color=\"$color\">$this</font>"
}

@SuppressLint("ResourceType")
fun String.colorWithHtml(context: Context, @ColorRes color: Int): String {
    return colorWithHtml("#" + context.resources.getString(color).substring(3))
}