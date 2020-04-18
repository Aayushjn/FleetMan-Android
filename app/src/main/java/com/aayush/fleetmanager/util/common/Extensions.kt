package com.aayush.fleetmanager.util.common

fun Double.format(suffix: String = "", digits: Int = 3) = "%.${digits}f%s".format(this, suffix)