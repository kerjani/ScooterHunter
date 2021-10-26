package com.kernacs.scooterhunter.base

import androidx.annotation.CallSuper
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics


abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    abstract val trackedScreenName: String

    @CallSuper
    override fun onResume() {
        super.onResume()
        FirebaseAnalytics.getInstance(requireContext()).logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundleOf(FirebaseAnalytics.Param.SCREEN_NAME to trackedScreenName)
        )
    }
}