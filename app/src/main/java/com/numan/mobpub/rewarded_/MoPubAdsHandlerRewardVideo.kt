package com.numan.mobpub.rewarded_

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.mopub.common.MoPub
import com.mopub.common.MoPubReward
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubRewardedVideoListener
import com.mopub.mobileads.MoPubRewardedVideos
import com.numan.mobpub.utils_.AddCheck
import com.numan.mobpub.utils_.MyConstants
import com.numan.mobpub.utils_.ReadSaveHandle

class MoPubAdsHandlerRewardVideo(private val mContext: Context, private val sActivity: String) :
    MoPubRewardedVideoListener {
     val adCheck: AddCheck
    private var loadingRewardVideoRequested = false
    private var adsInitializationCompleted = false
    private var rewardVideoLoaded = false
    private var loadRewardVideoOnInitializationComplete = false

    fun handleRewardVideo() {
        if (adCheck.shouldLoadRewardVideo()) {
            MoPub.onCreate((mContext as Activity))
            if (adsInitializationCompleted) {
                MoPubRewardedVideos.loadRewardedVideo(MyConstants.myRewardVideoID)
                loadingRewardVideoRequested = true
            } else {
                //initializeLoadRewardVideo = true;
                loadRewardVideoOnInitializationComplete = true
                initializeMoPubSDK(MyConstants.myRewardVideoID)
            }
            MoPubRewardedVideos.setRewardedVideoListener(this)
        }
    }

    fun loadRewardVideo() {
        if (!rewardVideoLoaded && !loadingRewardVideoRequested && AddCheck.adEnabled) {
            handleRewardVideo()
            Toast.makeText(
                mContext,
                "loading reward video, please wait, video will be displayed once loading is complete",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun showRewardVideo(): Boolean {
        if (!rewardVideoLoaded && !loadingRewardVideoRequested && AddCheck.adEnabled) {
            handleRewardVideo()
            Toast.makeText(
                mContext,
                "loading reward video, please wait, video will be displayed once loading is complete",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (sActivity == AddCheck.MySourceActivity) {
            if (rewardVideoLoaded && AddCheck.adEnabled) {
                // Display Ad
                MoPubRewardedVideos.showRewardedVideo(MyConstants.myRewardVideoID)
            }
        }
        return false
    }

    fun destroyRequested() {
        MoPub.onDestroy((mContext as Activity)) //for rewarded video ads only
    }

    fun backPressed() {
        MoPub.onBackPressed((mContext as Activity)) // for rewarded video ads only
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
        MoPub.initializeSdk(mContext, sdkConfiguration, initSdkListener())
    }

    private fun initSdkListener(): SdkInitializationListener {
        return SdkInitializationListener { /* MoPub SDK initialized.
	   Check if you should show the consent dialog here, and make your ad requests. */
            adsInitializationCompleted = true
            Toast.makeText(mContext, "Initialization completed", Toast.LENGTH_SHORT).show()
            if (loadRewardVideoOnInitializationComplete) {
                loadRewardVideoOnInitializationComplete = false
                MoPubRewardedVideos.loadRewardedVideo(MyConstants.myRewardVideoID)
                loadingRewardVideoRequested = true
                loadRewardVideoOnInitializationComplete = false
            }
        }
    }

    override fun onRewardedVideoLoadSuccess(adUnitId: String) {
        // Called when the video for the given adUnitId has loaded. At this point you should be able to call MoPubRewardedVideos.showRewardedVideo(String) to show the video.
        if (adUnitId == MyConstants.myRewardVideoID) {
            rewardVideoLoaded = true
            loadingRewardVideoRequested = false

            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(mContext)
            builder.setMessage("Reward video loaded successfully, show now ?")
                .setTitle("Reward Video")
                .setPositiveButton("Yes") { dialog, id -> // Display Ad
                    MoPubRewardedVideos.showRewardedVideo(MyConstants.myRewardVideoID)
                }
                .setNegativeButton("No") { dialog, id ->
                    // User not in agreement, ignore.
                }
            builder.create()
            builder.show()
        }
    }

    override fun onRewardedVideoLoadFailure(adUnitId: String, errorCode: MoPubErrorCode) {
        // Called when a video fails to load for the given adUnitId. The provided error code will provide more insight into the reason for the failure to load.
    }

    override fun onRewardedVideoStarted(adUnitId: String) {
        // Called when a rewarded video starts playing.
    }

    override fun onRewardedVideoPlaybackError(adUnitId: String, errorCode: MoPubErrorCode) {
        //  Called when there is an error during video playback.
    }

    override fun onRewardedVideoClicked(adUnitId: String) {
        //  Called when a rewarded video is clicked.
    }

    override fun onRewardedVideoClosed(adUnitId: String) {
        // Called when a rewarded video is closed. At this point your application should resume.
        //ResultScientificCalculator.setText("Rewarded video closed");
    }

    override fun onRewardedVideoCompleted(adUnitIds: Set<String>, reward: MoPubReward) {
        // Called when a rewarded video is completed and the user should be rewarded.
        // You can query the reward object with boolean isSuccessful(), String getLabel(), and int getAmount().
        rewardVideoLoaded = false
        AddCheck.adEnabled = false
        val startPeriod = System.currentTimeMillis().toDouble()
        val endPeriod = startPeriod + MyConstants.rewardedPeriod
        val RSH = ReadSaveHandle(mContext)
        try {
            RSH.saveKeyValue(MyConstants.keyForAdFreeStartDate, startPeriod.toString())
            RSH.saveKeyValue(MyConstants.keyForAdFreeEndDate, endPeriod.toString())
            RSH.showToastMessage(MyConstants.videoAdRewardedMessage)
        } catch (e: Exception) {
            //
        }
    }

    init {
        adCheck = AddCheck(mContext)
    }
}