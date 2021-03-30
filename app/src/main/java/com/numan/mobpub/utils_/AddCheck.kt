package com.numan.mobpub.utils_

import android.content.Context

class AddCheck(private val mContext: Context) {
    fun bannerAdRequired(activity: String?): Boolean {
        return when (activity) {
            MySourceActivity -> {
                adEnabled = !inAdsFreePeriod()
                adEnabled
            }
            else -> false
        }
    }

    fun interstitialAdRequired(activity: String?): Boolean {
        return when (activity) {
            MySourceActivity -> {
                adEnabled = !inAdsFreePeriod()
                adEnabled
            }
            else -> false
        }
    }

    private fun inAdsFreePeriod(): Boolean {
        return try {
            val RSH = ReadSaveHandle(mContext)
            val adFreeEndPeriod = RSH.getKeyValue(MyConstants.keyForAdFreeEndDate)?.toDouble()
            val currentTime = System.currentTimeMillis().toDouble()
            adFreeEndPeriod!!.minus(currentTime) >= 0
        } catch (e: Exception) {
            false
        }
    }

    fun shouldLoadRewardVideo(): Boolean {
        adEnabled = !inAdsFreePeriod()
        return adEnabled
    }

    companion object {
        const val MySourceActivity = "MainActivity"
        var adEnabled = true // Assumes that by default ad is required
    }
}