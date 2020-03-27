package com.aayush.fleetmanager.util.common

fun Double.format(digits: Int, suffix: String = "") = "%.${digits}f%s".format(this, suffix)

fun Number.format(suffix: String = ""): String = "$this$suffix"