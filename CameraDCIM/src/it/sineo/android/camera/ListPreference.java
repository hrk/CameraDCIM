/*
 * Copyright (C) 2009 The Android Open Source Project
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

package it.sineo.android.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import it.sineo.android.camera.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A type of <code>CameraPreference</code> whose number of possible values
 * is limited.
 */
public class ListPreference extends CameraPreference {

    private final String mKey;
    private String mValue;
    private final String mDefaultValue;

    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private boolean mLoaded = false;

    public ListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.ListPreference, 0, 0);

        mKey = Util.checkNotNull(
                a.getString(R.styleable.ListPreference_key));
        mDefaultValue = a.getString(R.styleable.ListPreference_defaultValue);

        setEntries(a.getTextArray(R.styleable.ListPreference_entries));
        setEntryValues(a.getTextArray(
                R.styleable.ListPreference_entryValues));
        a.recycle();
    }

    public String getKey() {
        return mKey;
    }

    public CharSequence[] getEntries() {
        return mEntries;
    }

    public CharSequence[] getEntryValues() {
        return mEntryValues;
    }

    public void setEntries(CharSequence entries[]) {
        mEntries = entries == null ? new CharSequence[0] : entries;
    }

    public void setEntryValues(CharSequence values[]) {
        mEntryValues = values == null ? new CharSequence[0] : values;
    }

    public String getValue() {
        if (!mLoaded) {
            mValue = getSharedPreferences().getString(mKey, mDefaultValue);
            mLoaded = true;
        }
        return mValue;
    }

    public void setValue(String value) {
        if (findIndexOfValue(value) < 0) throw new IllegalArgumentException();
        mValue = value;
        persistStringValue(value);
    }

    public void setValueIndex(int index) {
        setValue(mEntryValues[index].toString());
    }

    public int findIndexOfValue(String value) {
        for (int i = 0, n = mEntryValues.length; i < n; ++i) {
            if (Util.equals(mEntryValues[i], value)) return i;
        }
        return -1;
    }

    public String getEntry() {
        return mEntries[findIndexOfValue(getValue())].toString();
    }

    protected void persistStringValue(String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(mKey, value);
        editor.apply();
    }

    @Override
    public void reloadValue() {
        this.mLoaded = false;
    }

    public void filterUnsupported(List<String> supported) {
        ArrayList<CharSequence> entries = new ArrayList<CharSequence>();
        ArrayList<CharSequence> entryValues = new ArrayList<CharSequence>();
        for (int i = 0, len = mEntryValues.length; i < len; i++) {
            if (supported.indexOf(mEntryValues[i].toString()) >= 0) {
                entries.add(mEntries[i]);
                entryValues.add(mEntryValues[i]);
            }
        }
        int size = entries.size();
        mEntries = entries.toArray(new CharSequence[size]);
        mEntryValues = entryValues.toArray(new CharSequence[size]);
    }
}
