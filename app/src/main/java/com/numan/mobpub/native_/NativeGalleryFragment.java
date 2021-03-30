//// Copyright 2018-2021 Twitter, Inc.
//// Licensed under the MoPub SDK License Agreement
//// https://www.mopub.com/legal/sdk-license-agreement/
//
//package com.numan.mobpub.NativeAd;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentStatePagerAdapter;
//import androidx.viewpager.widget.ViewPager;
//import com.mopub.nativeads.MoPubNativeAdLoadedListener;
//import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
//import com.mopub.nativeads.MoPubStreamAdPlacer;
//
//import com.mopub.nativeads.RequestParameters;
//import com.mopub.nativeads.ViewBinder;
//import com.numan.mobpub.R;
//
//import java.util.EnumSet;
//
//import static com.mopub.nativeads.RequestParameters.NativeAdAsset;
//
//public class NativeGalleryFragment extends Fragment implements MoPubNativeAdLoadedListener {
//
//    private ViewPager mViewPager;
//    private CustomPagerAdapter mPagerAdapter;
//    private MoPubStreamAdPlacer mStreamAdPlacer;
//    private RequestParameters mRequestParameters;
//
//    @Override
//    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
//            final Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
////        mAdConfiguration = MoPubSampleAdUnit.fromBundle(getArguments());
//        final View view = inflater.inflate(R.layout.native_gallery_fragment, container, false);
//
//
//        // Set up a renderer for a static native ad.
//        final MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(
//                new ViewBinder.Builder(R.layout.native_ad_list_item)
//                        .titleId(R.id.native_title)
//                        .textId(R.id.native_text)
//                        .mainImageId(R.id.native_main_image)
//                        .iconImageId(R.id.native_icon_image)
//                        .callToActionId(R.id.native_cta)
//                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
//                        .sponsoredTextId(R.id.native_sponsored_text_view)
//                        .build());
//
//
//        mStreamAdPlacer.registerAdRenderer(moPubStaticNativeAdRenderer);
//        mStreamAdPlacer.setAdLoadedListener(this);
//
//        mPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), mStreamAdPlacer);
//        mViewPager.setAdapter(mPagerAdapter);
//
//        return view;
//    }
//
//    public MoPubStreamAdPlacer getAdPlacer() {
//        return mStreamAdPlacer;
//    }
//
//    @Override
//    public void onDestroyView() {
//        // You must call this or the ad adapter may cause a memory leak.
//        mStreamAdPlacer.destroy();
//        super.onDestroyView();
//    }
//
//    @Override
//    public void onResume() {
//        // MoPub recommends reloading ads when the user returns to a view.
//        mStreamAdPlacer.loadAds(getString(R.string.ad_unit_id_native), mRequestParameters);
//        super.onResume();
//    }
//
//    @Override
//    public void onAdLoaded(final int position) {
//        mViewPager.invalidate();
//        mPagerAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onAdRemoved(final int position) {
//        mViewPager.invalidate();
//        mPagerAdapter.notifyDataSetChanged();
//    }
//
//    private static class CustomPagerAdapter extends FragmentStatePagerAdapter {
//        private static final int ITEM_COUNT = 30;
//        private MoPubStreamAdPlacer mStreamAdPlacer;
//
//        public CustomPagerAdapter(final FragmentManager fragmentManager,
//                MoPubStreamAdPlacer streamAdPlacer) {
//            super(fragmentManager);
//            this.mStreamAdPlacer = streamAdPlacer;
//            streamAdPlacer.setItemCount(ITEM_COUNT);
//        }
//
//        @Override
//        public int getItemPosition(@NonNull final Object object) {
//            if (object instanceof AdFragment) {
//                return ((AdFragment) object).mAdPosition;
//            }
//            // This forces all items to be recreated when invalidate() is called on the ViewPager.
//            return POSITION_NONE;
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(final int i) {
//            mStreamAdPlacer.placeAdsInRange(i - 5, i + 5);
//            if (mStreamAdPlacer.isAd(i)) {
//                return AdFragment.newInstance(i);
//            }
//            return ContentFragment.newInstance(mStreamAdPlacer.getOriginalPosition(i));
//        }
//
//        @Override
//        public int getCount() {
//            return mStreamAdPlacer.getAdjustedCount(ITEM_COUNT);
//        }
//
//        @Override
//        public CharSequence getPageTitle(final int position) {
//            if (mStreamAdPlacer.isAd(position)) {
//                return "Advertisement";
//            }
//            return "Content Item " + mStreamAdPlacer.getOriginalPosition(position);
//        }
//
//    }
//
//    public static class ContentFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public static ContentFragment newInstance(int sectionNumber) {
//            ContentFragment fragment = new ContentFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
//                final Bundle savedInstanceState) {
//            // Inflate the view.
//            View rootView = inflater.inflate(R.layout.native_gallery_content, container, false);
//            return rootView;
//        }
//    }
//
//    public static class AdFragment extends Fragment {
//        private static final String ARG_AD_POSITION = "ad_position";
//        private MoPubStreamAdPlacer mAdPlacer;
//        private int mAdPosition = -2; // POSITION_NONE
//
//        public static AdFragment newInstance(int adPosition) {
//            AdFragment fragment = new AdFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt(ARG_AD_POSITION, adPosition);
//            fragment.setArguments(bundle);
//            fragment.mAdPosition = adPosition;
//            return fragment;
//        }
//
//    }
//}