package com.kernacs.scooterhunter.base

import androidx.annotation.CallSuper
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.kernacs.scooterhunter.R
import com.kernacs.scooterhunter.util.toReadableRationale


abstract class BaseFragment : Fragment() {

    abstract val trackedScreenName: String

    @CallSuper
    override fun onResume() {
        super.onResume()
        FirebaseAnalytics.getInstance(requireContext()).logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundleOf(FirebaseAnalytics.Param.SCREEN_NAME to trackedScreenName)
        )
    }

    fun showError(error: Throwable, refreshAction: () -> Unit) {
        view?.let {
            Snackbar.make(it, error.toReadableRationale(resources), Snackbar.LENGTH_INDEFINITE)
                .apply {
                    setAction(R.string.retry) {
                        refreshAction.invoke()
                    }
                    show()
                }
        }
    }
}