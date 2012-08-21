package com.the111min.android.api;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Returned result through {@link ResponseReceiver} in the listening
 * {@link Activity}.
 */
public class Response implements Parcelable {

    private boolean mIsSuccessed;

    private String mError;
    private Bundle mResults;

    public Response(boolean isSuccessed) {
        mIsSuccessed = isSuccessed;
        mResults = new Bundle();
    }

    public void addResult(String key, int value) {
        mResults.putInt(key, value);
    }

    public void addResult(String key, String value) {
        mResults.putString(key, value);
    }

    public void addResult(String key, boolean value) {
        mResults.putBoolean(key, value);
    }

    public void addResult(String key, float value) {
        mResults.putFloat(key, value);
    }

    public void addResult(String key, Parcelable value) {
        mResults.putParcelable(key, value);
    }

    public void addStringArrayResult(String key, ArrayList<String> value) {
        mResults.putStringArrayList(key, value);
    }

    public void addParcelableArrayResult(String key, ArrayList<? extends Parcelable> value) {
        mResults.putParcelableArrayList(key, value);
    }

    public void setResultBundle(Bundle bundle) {
        mResults = bundle;
    }

    public boolean isSuccessed() {
        return mIsSuccessed;
    }

    public String getErrorMessage() {
        return mError;
    }

    public void setErrorMessage(String error) {
        mError = error;
    }

    public Bundle getResults() {
        return mResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIsSuccessed ? "true" : "false");
        dest.writeBundle(mResults);
    }

    private Response(Parcel in) {
        mIsSuccessed = Boolean.parseBoolean(in.readString());
        mResults = in.readBundle();
    }

    public static final Parcelable.Creator<Response> CREATOR = new Parcelable.Creator<Response>() {

        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    @Override
    public String toString() {
        return "Response [mIsSuccessed=" + mIsSuccessed + ", mError=" + mError
                + ", mResults=" + mResults + "]";
    }

}
