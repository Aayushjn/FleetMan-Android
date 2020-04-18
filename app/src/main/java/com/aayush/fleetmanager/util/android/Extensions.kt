package com.aayush.fleetmanager.util.android

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun EditText.asString(): String = text.toString().trim()

fun EditText.isBlankOrEmpty(): Boolean = text.isEmpty() || text.isBlank()

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun View.showIf(constraint: Boolean) {
    visibility = if (constraint) View.VISIBLE else View.GONE
}

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

fun ConnectivityManager.isNetworkAvailable(): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = this.activeNetwork
        getNetworkCapabilities(network)?.run {
            when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } ?: false
    } else {
        activeNetworkInfo?.isConnectedOrConnecting ?: false
    }

fun NavController.navigateSafe(
    @IdRes currentFragment: Int,
    @IdRes navigateAction: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    if (currentDestination?.id == currentFragment) {
        navigate(navigateAction, args, navOptions, navigatorExtras)
    }
}