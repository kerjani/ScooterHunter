package com.kernacs.scooterhunter.util

import android.content.res.Resources
import com.kernacs.scooterhunter.R
import java.net.UnknownHostException


fun Throwable.toReadableRationale(resources: Resources) = when (this) {
    is UnknownHostException -> {
        resources.getString(R.string.error_no_network)
    }
    is EmptyDataException -> {
        resources.getString(emptyDataInfo)
    }
    else -> {
        resources.getString(R.string.error_general)
    }
}