package com.aayush.fleetmanager.util.android

import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import com.aayush.fleetmanager.App

fun EditText.asString(): String = text.toString().trim()

fun EditText.isBlankOrEmpty(): Boolean = text.isEmpty() || text.isBlank()

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context.getApp(): App = applicationContext as App

fun Context.getNotificationManager(): NotificationManager =
    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

fun ExpandableListView.setViewHeight(group: Int) {
    val adapter: ExpandableListAdapter = expandableListAdapter
    var totalHeight = 0
    val desiredWidth: Int = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)

    var groupItem: View
    var listItem: View
    var adapterChildrenCount: Int
    for (i in 0 until adapter.groupCount) {
        groupItem = adapter.getGroupView(i, false, null, this)
        groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
        totalHeight += groupItem.measuredHeight

        if ((isGroupExpanded(i) && i != group) || (!isGroupExpanded(i) && i == group)) {
            adapterChildrenCount = adapter.getChildrenCount(i)
            for (j in 0 until adapterChildrenCount) {
                listItem = adapter.getChildView(i, j, false, null, this)
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                totalHeight += listItem.measuredHeight
            }
        }
    }

    val params: ViewGroup.LayoutParams = layoutParams
    var height = totalHeight + (dividerHeight * (adapter.groupCount - 1))
    if (height < 10) {
        height = 200
    }
    params.height = height
    layoutParams = params
    requestLayout()
}

fun isNetworkNotAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network: Network = connectivityManager.activeNetwork ?: return true
        val activeNetwork: NetworkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return true
        when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> false
            else -> true
        }
    } else {
        connectivityManager.activeNetworkInfo?.isConnected ?: true
    }
}