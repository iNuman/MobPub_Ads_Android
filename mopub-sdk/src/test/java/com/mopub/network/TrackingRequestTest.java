// Copyright 2018-2021 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// https://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.network;

import android.app.Activity;
import android.content.Context;

import com.mopub.common.test.support.SdkTestRunner;
import com.mopub.mobileads.VastErrorCode;
import com.mopub.mobileads.VastTracker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;

import java.util.Arrays;

import static com.mopub.common.VolleyRequestMatcher.isUrl;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SdkTestRunner.class)
public class TrackingRequestTest {

    @Mock
    private MoPubRequestQueue mockRequestQueue;
    private Context context;
    private String url;


    @Before
    public void setup() {
        context = (Context) Robolectric.buildActivity(Activity.class).create().get();
        url = "testUrl";
        Networking.setRequestQueueForTesting(mockRequestQueue);
    }

    @Test
    public void makeTrackingHttpRequest_shouldMakeTrackingHttpRequestWithWebViewUserAgent() throws Exception {
        TrackingRequest.makeTrackingHttpRequest(url, context);

        verify(mockRequestQueue).add(any(TrackingRequest.class));
    }

    @Test
    public void makeTrackingHttpRequest_withNullUrl_shouldNotMakeTrackingHttpRequest() throws Exception {
        TrackingRequest.makeTrackingHttpRequest((String) null, context);

        verify(mockRequestQueue, never()).add(any(TrackingRequest.class));
    }

    @Test
    public void makeTrackingHttpRequest_withNullContext_shouldNotMakeTrackingHttpRequest() throws Exception {
        TrackingRequest.makeTrackingHttpRequest(url, null);

        verify(mockRequestQueue, never()).add(any(TrackingRequest.class));
    }

    @Test
    public void makeVastTrackingTwoHttpRequest_shouldSubstituteMacros_shouldMakeSingleRequest() throws Exception {
        final VastTracker vastTracker = new VastTracker.Builder("uri?errorcode=[ERRORCODE]&contentplayhead=[CONTENTPLAYHEAD]&asseturi=[ASSETURI]").build();
        TrackingRequest.makeVastTrackingHttpRequest(
                Arrays.asList(vastTracker),
                VastErrorCode.UNDEFINED_ERROR,
                123,
                "assetUri",
                context
        );

        verify(mockRequestQueue).add(argThat(isUrl(
                "uri?errorcode=900&contentplayhead=00:00:00.123&asseturi=assetUri")));

        TrackingRequest.makeVastTrackingHttpRequest(
                Arrays.asList(vastTracker),
                VastErrorCode.UNDEFINED_ERROR,
                123,
                "assetUri",
                context
        );

        verifyNoMoreInteractions(mockRequestQueue);
    }

    @Test
    public void makeVastTrackingTwoHttpRequest_withRepeatableRequest_shouldMakeMultipleTrackingRequests() throws Exception {
        final VastTracker vastTracker = new VastTracker.Builder("uri?errorcode=[ERRORCODE]&contentplayhead=[CONTENTPLAYHEAD]&asseturi=[ASSETURI]").build();
        TrackingRequest.makeVastTrackingHttpRequest(
                Arrays.asList(vastTracker),
                VastErrorCode.UNDEFINED_ERROR,
                123,
                "assetUri",
                context
        );

        verify(mockRequestQueue).add(argThat(isUrl(
                "uri?errorcode=900&contentplayhead=00:00:00.123&asseturi=assetUri")));

        TrackingRequest.makeVastTrackingHttpRequest(
                Arrays.asList(vastTracker),
                VastErrorCode.UNDEFINED_ERROR,
                123,
                "assetUri",
                context
        );

        verify(mockRequestQueue).add(argThat(isUrl(
                "uri?errorcode=900&contentplayhead=00:00:00.123&asseturi=assetUri")));
    }
}