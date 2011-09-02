/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.sineo.android.camera.ui;

import android.content.Context;

import it.sineo.android.camera.R;
import it.sineo.android.camera.IconListPreference;

class GpsIndicator extends BasicIndicator {

    private static final int GPS_ON_INDEX = 1;

    private ResourceTexture mNoSignalIcon;
    private boolean mHasSignal = false;

    public GpsIndicator(Context context, IconListPreference preference) {
        super(context, preference);
    }

    @Override
    protected ResourceTexture getIcon() {
        if (mIndex == GPS_ON_INDEX && !mHasSignal) {
            if (mNoSignalIcon == null) {
                Context context = getGLRootView().getContext();
                mNoSignalIcon = new ResourceTexture(
                        context, R.drawable.ic_viewfinder_gps_no_signal);
            }
            return mNoSignalIcon;
        }
        return super.getIcon();
    }

    public void setHasSignal(boolean hasSignal) {
        if (mHasSignal == hasSignal) return;
        mHasSignal = hasSignal;
        invalidate();
    }

    @Override
    protected void onPreferenceChanged(int newIndex) {
        mHasSignal = false;
        super.onPreferenceChanged(newIndex);
    }
}
