package br.com.mrocigno.gameengine.utils

import android.content.res.Resources

fun Int.toDp(): Float {
    val density = Resources.getSystem().displayMetrics.density
    return this * density
}