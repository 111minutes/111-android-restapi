package com.the111min.android.api.util;

import android.util.Log;

import com.the111min.android.api.BuildConfig;

public final class Logger {

    private static boolean sIsEnabled = BuildConfig.DEBUG;

    private final String mTag;

    public static boolean isEnabled() {
        return sIsEnabled;
    }

    public static void setEnabled(boolean isEnabled) {
        sIsEnabled = isEnabled;
    }

    public static Logger getInstance(String tag) {
        return new Logger(tag);
    }

    private Logger(String tag) {
        mTag = tag;
    }

    public void d(String message) {
        if (sIsEnabled) {
            Log.d(mTag, message);
        }
    }

    public void d(String message, Throwable cause) {
        if (sIsEnabled) {
            Log.d(mTag, message, cause);
        }
    }

    public void e(String message) {
        if (sIsEnabled) {
            Log.e(mTag, message);
        }
    }

    public void e(String message, Throwable cause) {
        if (sIsEnabled) {
            Log.e(mTag, message, cause);
        }
    }

}
