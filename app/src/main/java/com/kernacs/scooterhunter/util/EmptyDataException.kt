package com.kernacs.scooterhunter.util

import androidx.annotation.StringRes
import com.kernacs.scooterhunter.R

class EmptyDataException(
    @StringRes val emptyDataInfo: Int = R.string.error_empty_data
) : Throwable()
