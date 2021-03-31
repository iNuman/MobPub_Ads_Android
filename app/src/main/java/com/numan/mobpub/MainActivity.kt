package com.numan.mobpub

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.CAMERA
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mopub.common.Constants
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener
import com.mopub.common.logging.MoPubLog
import com.mopub.common.privacy.ConsentDialogListener
import com.mopub.common.privacy.ConsentStatusChangeListener
import com.mopub.common.privacy.PersonalInfoManager
import com.mopub.common.util.DeviceUtils
import com.mopub.mobileads.MoPubConversionTracker
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubInterstitial
import com.numan.mobpub.databinding.ActivityMainBinding
import com.numan.mobpub.interstitial_.MoPubAdsHandlerBannerNdInterstitial
import com.numan.mobpub.native_.NativeManualFragment
import com.numan.mobpub.rewarded_.MoPubAdsHandlerRewardVideo
import com.numan.mobpub.utils_.AddCheck
import com.numan.mobpub.*
import com.numan.mobpub.utils_.ConnectivityCheckup.hasInternetConnection
import com.numan.mobpub.utils_.SampleActivityUtils
import java.util.*

/*
*  Documentation:
*  https://developers.mopub.com/publishers/android/integrate/
*  https://developers.mopub.com/publishers/integrate/
*  https://ayltai.medium.com/a-complete-guide-to-mopub-native-ad-mediation-on-android-e6ecc2afff24
*  https://www.youtube.com/watch?v=4Kyp7ZmKzTo
*
*  Integrating:: Different Ad Networks
*  https://developers.mopub.com/publishers/mediation/integrate/
*
*  New MobPub Integration:
*  https://github.com/mopub/mopub-android-mediation
*
*  Mediation:
*  https://developers.mopub.com/publishers/mediation/integrate-android/
*
* */

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        var REQUIRED_DANGEROUS_PERMISSIONS: MutableList<String> = ArrayList()
        private const val SHOWING_CONSENT_DIALOG_KEY = "ShowingConsentDialog"
        private const val RC_BARCODE_CAPTURE = 9001


        fun start(context: Context) {
            var intent = Intent(context, TestThis::class.java)
            context.startActivity(intent)
        }


        fun logToast(context: Context?, message: String?) {
            Log.d("ffnet", message!!)
            if (context != null && context.applicationContext != null) {
                Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var mConsentStatusChangeListener: ConsentStatusChangeListener? = null
    var mPersonalInfoManager: PersonalInfoManager? = null
    private var mShowingConsentDialog = false

    var showInterstitial: Button? = null
    var showRewardVideo: Button? = null
    var nativeAdShow: Button? = null
    var loadRewardVideo: Button? = null

    var adsShownCounter = 0
    var adsShownSinceLastRewardVideoPrompt = 0

    var adsHandlerBannerNdInterstitial: MoPubAdsHandlerBannerNdInterstitial? = null
    var adsHandlerRewardVideo: MoPubAdsHandlerRewardVideo? = null
    var allready_purchased: Boolean = false
    var allow_ads: String? = null

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = _binding!!.root
        setContentView(view)

//        importantInitializations()
        initViews()
        intiAds()


    }

    private fun importantInitializations() {

        REQUIRED_DANGEROUS_PERMISSIONS.add(ACCESS_COARSE_LOCATION)
        REQUIRED_DANGEROUS_PERMISSIONS.add(CAMERA)


        mConsentStatusChangeListener = initConsentChangeListener()
        mPersonalInfoManager = MoPub.getPersonalInformationManager()
        if (mPersonalInfoManager != null) {
            mPersonalInfoManager!!.subscribeConsentStatusChangeListener(mConsentStatusChangeListener)
        }

        val permissionsToBeRequested: MutableList<String> = ArrayList()
        for (permission in REQUIRED_DANGEROUS_PERMISSIONS) {
            if (!DeviceUtils.isPermissionGranted(this, permission)) {
                permissionsToBeRequested.add(permission)
            }
        }

        // Request dangerous permissions

        // Request dangerous permissions
        if (permissionsToBeRequested.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToBeRequested.toTypedArray(),
                Constants.UNUSED_REQUEST_CODE
            )
        }

        // Set location awareness and precision globally for your app:

        // Set location awareness and precision globally for your app:
        MoPub.setLocationAwareness(MoPub.LocationAwareness.TRUNCATED)
        MoPub.setLocationPrecision(4)


        val configBuilder = SdkConfiguration.Builder("b195f8dd8ded45fe847ad89ed1d016da")
        if (BuildConfig.DEBUG) {
            configBuilder.withLogLevel(MoPubLog.LogLevel.DEBUG)
        } else {
            configBuilder.withLogLevel(MoPubLog.LogLevel.INFO)
        }

        SampleActivityUtils.addDefaultNetworkConfiguration(configBuilder)

        MoPub.initializeSdk(this, configBuilder.build(), initSdkListener())

    }

    private fun initSdkListener(): SdkInitializationListener {
        return SdkInitializationListener {
            logToast(this@MainActivity, "SDK initialized.")
            if (mPersonalInfoManager != null && mPersonalInfoManager!!.shouldShowConsentDialog()) {
                mPersonalInfoManager?.loadConsentDialog(initDialogLoadListener())
            }
            MoPubConversionTracker(this@MainActivity).reportAppOpen()
        }
    }

    private fun initConsentChangeListener(): ConsentStatusChangeListener {
        return ConsentStatusChangeListener { oldConsentStatus, newConsentStatus, canCollectPersonalInformation ->
            logToast(this@MainActivity, "Consent: " + newConsentStatus.name)
            if (mPersonalInfoManager != null && mPersonalInfoManager!!.shouldShowConsentDialog()) {
                mPersonalInfoManager!!.loadConsentDialog(initDialogLoadListener())
            }
        }
    }

    private fun initDialogLoadListener(): ConsentDialogListener {
        return object : ConsentDialogListener {
            override fun onConsentDialogLoaded() {
                if (mPersonalInfoManager != null) {
                    mPersonalInfoManager?.showConsentDialog()
                    mShowingConsentDialog = true
                }
            }

            override fun onConsentDialogLoadFailed(moPubErrorCode: MoPubErrorCode) {
                logToast(this@MainActivity, "Consent dialog failed to load.")
            }
        }
    }

    private fun nativeAds() {

        if (hasInternetConnection(this)) {

//            allready_purchased = AdPreManager.getInstance(this).productPurchased

//            if (!allready_purchased) {
//                Log.d(tag, "onCreate: $allready_purchased")
//                allow_ads = SharedPrefs.getString(this, SharedPrefs.allow_ads_on_back_press)
            supportFragmentManager.beginTransaction().add(R.id.native_ad, NativeManualFragment())
                .commit()
//                playInterstitialAds()
//            }

        }
    }

    private fun intiAds() {
        adsHandlerBannerNdInterstitial = MoPubAdsHandlerBannerNdInterstitial(this, AddCheck.MySourceActivity)
        adsHandlerRewardVideo = MoPubAdsHandlerRewardVideo(this, AddCheck.MySourceActivity)


        adsHandlerBannerNdInterstitial?.handleBannerAds()
        adsHandlerBannerNdInterstitial?.handleInterstitialAds()
        adsHandlerRewardVideo?.handleRewardVideo()

        showInterstitial?.setOnClickListener {
            adsHandlerBannerNdInterstitial?.showInterstitial()
            adsHandlerBannerNdInterstitial?.mInterstitial?.interstitialAdListener =
                object : MoPubInterstitial.InterstitialAdListener {
                    override fun onInterstitialLoaded(interstitial: MoPubInterstitial?) {}
                    override fun onInterstitialFailed(
                        interstitial: MoPubInterstitial?,
                        errorCode: MoPubErrorCode?
                    ) {
                    }

                    override fun onInterstitialShown(interstitial: MoPubInterstitial?) {}
                    override fun onInterstitialClicked(interstitial: MoPubInterstitial?) {}

                    override fun onInterstitialDismissed(interstitial: MoPubInterstitial?) {
                        adsHandlerBannerNdInterstitial?.loadInterstitial()
                        Toast.makeText(
                            this@MainActivity,
                            "Do Something in onDismissed",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

        }

        showRewardVideo?.setOnClickListener {
            adsHandlerRewardVideo?.loadRewardVideo()
            adsHandlerRewardVideo?.showRewardVideo()
        }
//        nativeAds()
        nativeAdShow?.setOnClickListener {
            nativeAds()
        }
        nativeAds()
    }

    private fun initViews() {
        showInterstitial = binding.interstitialAd
        showRewardVideo = binding.rewardVideo
        nativeAdShow = binding.nativeAdShow
        binding.testThis.setOnClickListener { start(this@MainActivity) }

    }

    override fun onDestroy() {
        super.onDestroy()
        adsHandlerBannerNdInterstitial?.destroyRequested()
        adsHandlerRewardVideo?.destroyRequested()
    }

    override fun onResume() {
        super.onResume()
//        nativeAds()
    }

    override fun onPause() {
        super.onPause()
        Log.d("ffnet", "onPause")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        adsHandlerRewardVideo?.backPressed()
    }


}