// Copyright 2018-2021 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// https://www.mopub.com/legal/sdk-license-agreement/
package com.numan.mobpub.utils_

import com.mopub.common.Preconditions
import com.mopub.common.SdkConfiguration

internal object SampleActivityUtils {
    fun addDefaultNetworkConfiguration(builder: SdkConfiguration.Builder) {
        Preconditions.checkNotNull(builder)

        // We have no default networks to initialize
    }
}