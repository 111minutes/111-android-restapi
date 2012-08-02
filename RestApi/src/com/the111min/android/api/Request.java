package com.the111min.android.api;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Use for: 1. Set header and body parameters for http request; 2. Pass data
 * from {@link Activity} in {@link ResponseHandler}.
 */
public class Request implements Parcelable {

    private static final String TAG = "Request";

    private String mEndpoint;

    private Bundle mBodyParams;
    private Bundle mHeaderParams;
    private Bundle mTemporaryData;

    private RequestMethod mRequestMethod;

    private Class<? extends ResponseHandler> mResponseHandler;

    public enum RequestMethod {
        POST, GET, PUT, DELETE
    }

    private Request(String endpoint, RequestMethod requestMethod) {
        mEndpoint = endpoint;

        mBodyParams = new Bundle();
        mTemporaryData = new Bundle();
        mHeaderParams = new Bundle();

        mRequestMethod = requestMethod;
        mResponseHandler = EmptyResponseHandler.class;
    }

    /**
     * @return
     */
    public RequestMethod getRequestMethod() {
        return mRequestMethod;
    }

    /**
     * @return
     */
    public String getEndpoint() {
        return mEndpoint;
    }

    /**
     * @return
     */
    public Bundle getHeaders() {
        return mHeaderParams;
    }

    /**
     * @return
     */
    public Bundle getBodyParams() {
        return mBodyParams;
    }

    /**
     * @return
     */
    public Bundle getTemporaryData() {
        return mTemporaryData;
    }

    /**
     * @return
     */
    public ResponseHandler getResponseHandler() {
        try {
            return mResponseHandler.newInstance();
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    private void setResponseHandler(Class<? extends ResponseHandler> responseHandler) {
        mResponseHandler = responseHandler;
    }

    private void addHeaderParam(String key, String value) {
        mHeaderParams.putString(key, value);
    }

    private void addBodyParam(String key, String value) {
        mBodyParams.putString(key, value);
    }

    private void addBodyParam(String key, int value) {
        mBodyParams.putString(key, String.valueOf(value));
    }

    private void addTemporaryData(String key, int value) {
        mTemporaryData.putInt(key, value);
    }

    private void addTemporaryData(String key, String value) {
        mTemporaryData.putString(key, value);
    }

    private void addTemporaryData(String key, boolean value) {
        mTemporaryData.putBoolean(key, value);
    }

    @SuppressWarnings("unchecked")
    private Request(Parcel in) {
        mEndpoint = in.readString();
        mBodyParams = in.readBundle();
        mTemporaryData = in.readBundle();
        mHeaderParams = in.readBundle();
        String handlerClassName = in.readString();
        mRequestMethod = RequestMethod.valueOf(in.readString());
        try {
            mResponseHandler = (Class<? extends ResponseHandler>) Class.forName(handlerClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEndpoint);
        dest.writeBundle(mBodyParams);
        dest.writeBundle(mTemporaryData);
        dest.writeBundle(mHeaderParams);
        dest.writeString(mResponseHandler.getName());
        dest.writeString(mRequestMethod.name());
    }

    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() {

        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @author Kirill
     *
     */
    public static class Builder {

        private Request mRequest;

        /**
         * @param endpoint
         * @param method
         */
        public Builder(String endpoint, RequestMethod method) {
            mRequest = new Request(endpoint, method);
        }

        /**
         * @param responseHandler
         * @return
         */
        public Builder setResponseHandler(Class<? extends ResponseHandler> responseHandler) {
            mRequest.setResponseHandler(responseHandler);
            return this;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder addHeaderParam(String key, String value) {
            mRequest.addHeaderParam(key, value);
            return this;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder addBodyParam(String key, String value) {
            mRequest.addBodyParam(key, value);
            return this;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder addBodyParam(String key, int value) {
            mRequest.addBodyParam(key, value);
            return this;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder addTemporaryData(String key, int value) {
            mRequest.addTemporaryData(key, value);
            return this;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder addTemporaryData(String key, String value) {
            mRequest.addTemporaryData(key, value);
            return this;
        }

        public Builder addTemporaryData(String key, boolean value) {
            mRequest.addTemporaryData(key, value);
            return this;
        }

        public Request create() {
            return mRequest;
        }
    }

}
