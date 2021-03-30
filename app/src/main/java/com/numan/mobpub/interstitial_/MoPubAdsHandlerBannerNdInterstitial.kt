package com.numan.mobpub.interstitial_

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubInterstitial
import com.mopub.mobileads.MoPubInterstitial.InterstitialAdListener
import com.mopub.mobileads.MoPubView
import com.numan.mobpub.R
import com.numan.mobpub.utils_.AddCheck
import com.numan.mobpub.utils_.MyConstants

class MoPubAdsHandlerBannerNdInterstitial(
    private val mContext: Context,
    private val sActivity: String
) : InterstitialAdListener {

    private val adCheck: AddCheck
    private var moPubView: MoPubView? = null
     var mInterstitial: MoPubInterstitial? = null
    private var doNotSkipAd = false
    private var interstitialLoaded = false
    private var interstitialAlreadyDisplayed = false
    private var adsInitializationCompleted = false
    private var loadBannerOnInitializationComplete = false
    private var loadInterstitialOnInitializationComplete = false

    fun handleInterstitialAds() {
        if (sActivity == AddCheck.MySourceActivity) {
            doNotSkipAd = adCheck.interstitialAdRequired(AddCheck.MySourceActivity)
            if (MyConstants.hasAd && doNotSkipAd) {
                if (adsInitializationCompleted) {
                    mInterstitial = MoPubInterstitial((mContext as Activity), MyConstants.myMainActivityInterstitialID)
                    mInterstitial!!.interstitialAdListener = this
                    mInterstitial!!.load()
                } else {
                    loadInterstitialOnInitializationComplete = true
                    initializeMoPubSDK(MyConstants.myMainActivityInterstitialID)
                }
            }
        } else {
            Toast.makeText(
                mContext,
                "No Add strategy defined for this activity",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun handleBannerAds() {
        if (sActivity == AddCheck.MySourceActivity) {
            doNotSkipAd = adCheck.bannerAdRequired(AddCheck.MySourceActivity)
            if (MyConstants.hasAd && doNotSkipAd) {
                val activity = mContext as Activity
                moPubView = activity.findViewById<View>(R.id.mainActivityBanner) as MoPubView
                if (adsInitializationCompleted) {
                    moPubView?.setAdUnitId(MyConstants.myMainActivityBannerID) // Enter your Ad Unit ID from www.mopub.com
                    //moPubView.setAdSize(MoPubAdSize); // Call this if you are not setting the ad size in XML or wish to use an ad size other than what has been set in the XML. Note that multiple calls to `setAdSize()` will override one another, and the MoPub SDK only considers the most recent one.
                    //moPubView.loadAd(MoPubAdSize); // Call this if you are not calling setAdSize() or setting the size in XML, or if you are using the ad size that has not already been set through either setAdSize() or in the XML
                    moPubView?.loadAd()
                } else {
                    loadBannerOnInitializationComplete = true
                    initializeMoPubSDK(MyConstants.myMainActivityInterstitialID)
                }
            }
        } else {
            Toast.makeText(
                mContext,
                "No Add strategy defined for this activity",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun showInterstitial(): Boolean {
        if (interstitialAlreadyDisplayed && AddCheck.adEnabled) {
            mInterstitial!!.load() //load again so that it can be displayed in the next call
            interstitialAlreadyDisplayed = false
            return false
        }
        if (sActivity == AddCheck.MySourceActivity) {
            if (mInterstitial != null && AddCheck.adEnabled) {
                if (mInterstitial!!.isReady) {
                    mInterstitial!!.show()
                    interstitialAlreadyDisplayed = true
                    return true
                }
            }
        }
        return false
    }
    fun loadInterstitial(): Boolean {
        if (interstitialAlreadyDisplayed && AddCheck.adEnabled) {
            mInterstitial!!.load() //load again so that it can be displayed in the next call
            interstitialAlreadyDisplayed = false
            return false
        }
        return false
    }

    fun destroyRequested() {
        if (mInterstitial != null) {
            mInterstitial!!.destroy()
        }
    }

    fun pauseRequested() {
        MoPub.onPause((mContext as Activity))
    }

    fun stopRequested() {
        MoPub.onStop((mContext as Activity))
    }

    fun resumeRequested() {
        MoPub.onResume((mContext as Activity))
    }

    override fun onInterstitialLoaded(interstitial: MoPubInterstitial) {
        interstitialLoaded = true
    }

    override fun onInterstitialFailed(interstitial: MoPubInterstitial, errorCode: MoPubErrorCode) {}
    override fun onInterstitialShown(interstitial: MoPubInterstitial) {}
    override fun onInterstitialClicked(interstitial: MoPubInterstitial) {}
    override fun onInterstitialDismissed(interstitial: MoPubInterstitial) {}
    private fun initializeMoPubSDK(adUnit: String) {
        // configurations required to initialize
        /*
        Map<String, String> mediatedNetworkConfiguration1 = new HashMap<>();
        mediatedNetworkConfiguration1.put("<custom-adapter-class-data-key>", "<custom-adapter-class-data-value>");
        Map<String, String> mediatedNetworkConfiguration2 = new HashMap<>();
        mediatedNetworkConfiguration2.put("<custom-adapter-class-data-key>", "<custom-adapter-class-data-value>");
         */
        val sdkConfiguration = SdkConfiguration.Builder(adUnit) /*.withMediationSettings("MEDIATION_SETTINGS")
                .withAdditionalNetworks(CustomAdapterConfiguration.class.getName())
                .withMediatedNetworkConfiguration(CustomAdapterConfiguration1.class.getName(), mediatedNetworkConfiguration)
                .withMediatedNetworkConfiguration(CustomAdapterConfiguration2.class.getName(), mediatedNetworkConfiguration)
                .withMediatedNetworkConfiguration(CustomAdapterConfiguration1.class.getName(), mediatedNetworkConfiguration1)
                .withMediatedNetworkConfiguration(CustomAdapterConfiguration2.class.getName(), mediatedNetworkConfiguration2)
                .withLogLevel(LogLevel.Debug)*/
            .withLegitimateInterestAllowed(false)
            .build()
        MoPub.initializeSdk(mContext, sdkConfiguration, initSdkListener())
    }

    private fun initSdkListener(): SdkInitializationListener {
        return SdkInitializationListener {
            /* MoPub SDK initialized.
           Check if you should show the consent dialog here, and make your ad requests. */
            adsInitializationCompleted = true
            Toast.makeText(mContext, "Initialization completed", Toast.LENGTH_SHORT).show()
            if (loadInterstitialOnInitializationComplete) {
                mInterstitial = MoPubInterstitial(
                    (mContext as Activity),
                    MyConstants.myMainActivityInterstitialID
                )
                mInterstitial?.interstitialAdListener = this@MoPubAdsHandlerBannerNdInterstitial
                mInterstitial?.load()
                loadInterstitialOnInitializationComplete = false
            }
            if (loadBannerOnInitializationComplete) {
                moPubView?.setAdUnitId(MyConstants.myMainActivityBannerID)  // Enter your Ad Unit ID from www.mopub.com
                //moPubView.setAdSize(MoPubAdSize); // Call this if you are not setting the ad size in XML or wish to use an ad size other than what has been set in the XML. Note that multiple calls to `setAdSize()` will override one another, and the MoPub SDK only considers the most recent one.
                //moPubView.loadAd(MoPubAdSize); // Call this if you are not calling setAdSize() or setting the size in XML, or if you are using the ad size that has not already been set through either setAdSize() or in the XML
                moPubView?.loadAd()
                loadBannerOnInitializationComplete = false
            }
        }
    }

    init {
        adCheck = AddCheck(mContext)
    }
}