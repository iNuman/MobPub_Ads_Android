package com.numan.mobpub

import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener
import com.numan.mobpub.utils_.MyConstants

class TestingApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initializeMoPubSDK(MyConstants.adUnitId)
    }


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
        MoPub.initializeSdk(this, sdkConfiguration, initSdkListener())
    }

    private fun initSdkListener(): SdkInitializationListener {
        return SdkInitializationListener { /* MoPub SDK initialized.
	   Check if you should show the consent dialog here, and make your ad requests. */
//            adsInitializationCompleted = true
//            Toast.makeText(this, "Initialization completed", Toast.LENGTH_SHORT).show()
//            if (loadRewardVideoOnInitializationComplete) {
//                loadRewardVideoOnInitializationComplete = false
////                MoPubNative.MoPubNativeNetworkListener(MyConstants.myRewardVideoID)
//                loadingRewardVideoRequested = true
////                loadRewardVideoOnInitializationComplete = false
//            }
        }
    }

}