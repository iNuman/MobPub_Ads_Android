//package com.numan.mobpub.NativeAd
//
//
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.ImageView
//import androidx.fragment.app.Fragment
//
//class NativeAdFragment2 : Fragment() {
//    private var view: View? = null
//    private var adView: UnifiedNativeAdView? = null
//    private var adLoader: AdLoader? = null
//    var cardView: CardView? = null
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle
//    ): View? {
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.small_native_ad, container, false)
//        //        view= inflater.inflate(R.layout.ad_unified, container, false);
//        setNativeAd(view)
//        return view
//    }
//
//    private fun setNativeAd(view: View?) {
//        adView = view!!.findViewById(R.id.native_ad_view)
//
//        //   cardView=view.findViewById(R.id.native_card);
//
//
//        // Register the view used for each individual asset.
//        adView.setMediaView(adView.findViewById(R.id.media_view) as MediaView)
//        // Set other ad assets.
//        adView.setHeadlineView(adView.findViewById(R.id.primary))
//        adView.setBodyView(adView.findViewById(R.id.body))
//        adView.setCallToActionView(adView.findViewById(R.id.cta))
//        adView.setIconView(adView.findViewById(R.id.icon))
//
////        adView.setStarRatingView(adView.findViewById(R.id.rating_bar));
//        loadNativeAds()
//    }
//
//    private fun loadNativeAds() {
//        val native_ad_id: String = SharedPrefs.getString(view!!.context, SharedPrefs.native_ad)
//        //String native_id= AppSharedPrefrence.getInstance(getContext()).getNativeId();
////        String native_id= getResources().getString(R.string.native_ad);
//        val builder: AdLoader.Builder = Builder(activity, native_ad_id)
//        adLoader = builder.forUnifiedNativeAd { unifiedNativeAd ->
//            view!!.findViewById<View>(R.id.native_ad_view).visibility = View.VISIBLE
//            populateNativeAdView(unifiedNativeAd, adView)
//        }.withAdListener(
//            object : AdListener() {
//                fun onAdFailedToLoad(errorCode: Int) {
//                    adLoader.loadAds(Builder().build(), 1)
//                    // A native ad failed to load, check if the ad loader has finished loading
//                    // and if so, insert the ads into the list.
//                    Log.e(
//                        "MainActivityNativeAds",
//                        "The previous native ad failed to load. Attempting to"
//                                + " load another."
//                    )
//                    //                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new RecyclerMainFragment()).commit();
//                }
//
//                fun onAdLoaded() {
//                    super.onAdLoaded()
//                    adView.setVisibility(View.VISIBLE)
//                }
//            })
//            .build()
//
//
//        // Load the Native ads.
//        adLoader.loadAds(Builder().build(), 1)
//    }
//
//    private fun populateNativeAdView(
//        nativeAd: UnifiedNativeAd,
//        adView: UnifiedNativeAdView?
//    ) {
//        // Some assets are guaranteed to be in every UnifiedNativeAd.
//        (adView.getHeadlineView() as TextView).setText(nativeAd.getHeadline())
//        (adView.getBodyView() as TextView).setText(nativeAd.getBody())
//        (adView.getCallToActionView() as Button).setText(nativeAd.getCallToAction())
//
//        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
//        // check before trying to display them.
//        val icon: NativeAd.Image = nativeAd.getIcon()
//        if (icon == null) {
//            adView.getIconView().setVisibility(View.INVISIBLE)
//        } else {
//            (adView.getIconView() as ImageView).setImageDrawable(icon.getDrawable())
//            adView.getIconView().setVisibility(View.VISIBLE)
//        }
//
//        /*if (nativeAd.getPrice() == null) {
//            adView.getPriceView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getPriceView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//        }
//
//        if (nativeAd.getStore() == null) {
//            adView.getStoreView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getStoreView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//        }*/
//
////        if (nativeAd.getStarRating() == null) {
////            adView.getStarRatingView().setVisibility(View.INVISIBLE);
////        } else {
////            ((RatingBar) adView.getStarRatingView())
////                    .setRating(nativeAd.getStarRating().floatValue());
////            adView.getStarRatingView().setVisibility(View.VISIBLE);
////        }
//
//        /*  if (nativeAd.getAdvertiser() == null) {
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }
//*/
//        // Assign native ad object to the native view.
//        adView.setNativeAd(nativeAd)
//    }
//}