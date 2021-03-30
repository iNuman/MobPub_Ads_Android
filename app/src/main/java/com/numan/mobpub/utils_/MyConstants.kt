package com.numan.mobpub.utils_

object MyConstants {
    //Below are test IDs use only this when testing
    const val adUnitId = "b195f8dd8ded45fe847ad89ed1d016da"
    const val myMainActivityBannerID = "b195f8dd8ded45fe847ad89ed1d016da"
    const val myMainActivityInterstitialID = "24534e1901884e398f1253216226017e"
    const val myRewardVideoID = "920b6145fb1546cf8b5cf2ac34638bb7"

    //below is for facebook ads testing, HashID of the device can be found in the run log when running an app with facebook SDK
    //look for something like I/AdInternalSettings: Test mode device hash: c9d1da04-c84c-4d36-a315-0aa7b6a8a7a8
    //final static String MyEmulatorHashID = "c9d1da04-c84c-4d36-a315-0aa7b6a8a7a8";
    const val hasAd = true
    const val keyForAdCounter = "TotalAdsCount"
    const val keyForAdFreeStartDate = "AdFreeStart"
    const val keyForAdFreeEndDate = "AdFreeEnd"
    const val rewardedPeriod = 60.0 * 1000.0 * 4.0 //for Four minutes (testing)

    //final static double rewardedPeriod = 60.0*1000.0*60.0*24.0*30.0; // 30 days equivalent in miliseconds
    const val videoAdRewardedMessage =
        "Congratulations! \n Thank you for support, you can now enjoy Ad free App for 5 minutes" //final static String videoAdRewardedMessage = "Congratulations! \n Thank you for support, you can now enjoy Ad free App for 1 month";
}