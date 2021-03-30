package com.numan.mobpub.native_

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mopub.nativeads.*
import com.mopub.nativeads.MoPubNative.MoPubNativeNetworkListener
import com.mopub.nativeads.NativeAd.MoPubNativeEventListener
import com.mopub.nativeads.RequestParameters.NativeAdAsset
import com.numan.mobpub.R
import com.numan.mobpub.databinding.NativeManualFragmentBinding
import com.numan.mobpub.utils_.MoPubSampleAdUnit
import java.util.*


class NativeManualFragment : Fragment(R.layout.native_manual_fragment) {


    /*
    * For adjusting multiple ad providers
    * */
    private val mAdConfiguration: MoPubSampleAdUnit? = null


    private var mMoPubNative: MoPubNative? = null
    private var mAdContainer: LinearLayout? = null
    private var mRequestParameters: RequestParameters? = null

    private var _binding: NativeManualFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = NativeManualFragmentBinding.bind(view)

        mAdContainer = binding.parentView
        val adUnitId = getString(R.string.ad_unit_id_native)



        mMoPubNative = MoPubNative(requireContext(), adUnitId, object : MoPubNativeNetworkListener {
            override fun onNativeLoad(nativeAd: NativeAd) {
                val moPubNativeEventListener: MoPubNativeEventListener =
                    object : MoPubNativeEventListener {

                        override fun onImpression(view: View?) {
                            // The ad has registered an impression. You may call any app logic that
                            // depends on having the ad view shown.
                            logToast(requireContext(), "$name impressed.")
                        }

                        override fun onClick(view: View?) {
                            logToast(requireContext(), "$name clicked.")
                        }
                    }


                // In a manual integration, any interval that is at least 2 is acceptable
                val adapterHelper = AdapterHelper(requireContext(), 0, 2)
                val adView: View = adapterHelper.getAdView(null, null, nativeAd, ViewBinder.Builder(0).build())
                 nativeAd.setMoPubNativeEventListener(moPubNativeEventListener)
                 mAdContainer?.addView(adView)

            }

            override fun onNativeFail(errorCode: NativeErrorCode) {
                logToast(requireContext(), "$name failed to load: $errorCode")
            }
        })

        updateRequestParameters()
        mAdContainer?.removeAllViews()

        if (mMoPubNative != null) {
            mMoPubNative?.makeRequest(mRequestParameters)
        } else {
            logToast(requireContext(), "$name failed to load. MoPubNative instance is null.")
        }


        /*
        * TODO: NativeManualFragment:: Layout Initialized Here
        * */
        val moPubStaticNativeAdRenderer = MoPubStaticNativeAdRenderer(
            ViewBinder.Builder(R.layout.native_ad_list_item)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .sponsoredTextId(R.id.native_sponsored_text_view)
                .build()
        )
        /*
         * TODO: Video Native Ads Settings
        val mediaViewBinder = MediaViewBinder.Builder(R.layout.native_video_ad_layout)
            .mediaLayoutId(R.id.native_ad_video_view)
            .iconImageId(R.id.native_ad_icon_image)
            .titleId(R.id.native_ad_title)
            .textId(R.id.native_ad_text)
            .build()
            */


        // Set up a renderer for Facebook video ads.
        val facebookAdRenderer = FacebookAdRenderer(
            FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.native_ad_fan_list_item)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .mediaViewId(R.id.native_media_view)
                .adIconViewId(R.id.native_icon)
                .callToActionId(R.id.native_cta)
                .adChoicesRelativeLayoutId(R.id.native_privacy_information_icon_layout)
                .build()
        )

        // Set up a renderer for AdMob ads.
        val googlePlayServicesAdRenderer = GooglePlayServicesAdRenderer(
            GooglePlayServicesViewBinder.Builder(R.layout.admob_video_ad_list_item)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .mediaLayoutId(R.id.native_media_layout)
                .iconImageId(R.id.native_icon_image)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build()
        )



        // The first renderer that can handle a particular native ad gets used.
        // We are prioritizing network renderers.

        mMoPubNative?.registerAdRenderer(facebookAdRenderer)
        mMoPubNative?.registerAdRenderer(googlePlayServicesAdRenderer)
        mMoPubNative?.registerAdRenderer(moPubStaticNativeAdRenderer)
        mMoPubNative?.makeRequest(mRequestParameters)

        /*
        TODO: Video Ads will be register like this
        val moPubVideoNativeAdRenderer = MoPubVideoNativeAdRenderer(mediaViewBinder)
        mMoPubNative?.registerAdRenderer(moPubVideoNativeAdRenderer)
        */


    }

    private fun updateRequestParameters() {

        // Setting desired assets on your request helps native ad networks and bidders
        // provide higher-quality ads.
        val desiredAssets = EnumSet.of(
            NativeAdAsset.TITLE,
            NativeAdAsset.TEXT,
            NativeAdAsset.ICON_IMAGE,
            NativeAdAsset.MAIN_IMAGE,
            NativeAdAsset.CALL_TO_ACTION_TEXT,
            NativeAdAsset.SPONSORED
        )
        mRequestParameters = RequestParameters.Builder()
            .desiredAssets(desiredAssets)
            .build()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ffnet", "0")
        if (mMoPubNative != null) {
            mMoPubNative?.destroy()
            mMoPubNative = null
        }
        if (mAdContainer != null) {
            mAdContainer?.removeAllViews()
            mAdContainer = null
        }
    }

    private val name: String
        private get() = if (mAdConfiguration == null || TextUtils.isEmpty(mAdConfiguration.headerName)) {
            MoPubSampleAdUnit.AdType.MANUAL_NATIVE.getName()
        } else mAdConfiguration.headerName

    companion object {
        fun logToast(context: Context?, message: String?) {
            Log.d("ffnet", message!!)
            if (context != null && context.applicationContext != null) {
                Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}