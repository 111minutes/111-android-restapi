package com.the111min.android.api.request;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.the111min.android.api.response.DefaultResponseHandler;
import com.the111min.android.api.response.ResponseHandler;
import com.the111min.android.api.util.Logger;

import java.util.HashMap;

/**
 * Use for: 1. Set header and body parameters for http request; 2. Pass data
 * from {@link Activity} in {@link ResponseHandler}.
 */
public class Request implements Parcelable {

    private static final Logger LOG = Logger.getInstance(Request.class.getSimpleName());

    private final String mEndpoint;
    private final RequestMethod mRequestMethod;

    private final HashMap<String, String> mHeaders;
    private final Bundle mEntityData;
    private final Bundle mTemporaryData;

    private Class<? extends ResponseHandler> mResponseHandler;
    private Class<? extends RequestComposer> mRequestComposer;

    public enum RequestMethod {
        POST, GET, PUT, DELETE
    }

    private Request(String endpoint, RequestMethod requestMethod) {
        mEndpoint = endpoint;
        mRequestMethod = requestMethod;

        mHeaders = new HashMap<String, String>();
        mEntityData = new Bundle();
        mTemporaryData = new Bundle();

        mResponseHandler = DefaultResponseHandler.class;
        mRequestComposer = DefaultRequestComposer.class;
    }

    @SuppressWarnings("unchecked")
    private Request(Parcel in) {
        mEndpoint = in.readString();
        mHeaders = in.readHashMap(Request.class.getClassLoader());

        mEntityData = in.readBundle();
        mTemporaryData = in.readBundle();

        String handlerClassName = in.readString();
        try {
            mResponseHandler = (Class<? extends ResponseHandler>) Class.forName(handlerClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String composerClassName = in.readString();
        try {
            mRequestComposer = (Class<? extends RequestComposer>) Class.forName(composerClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        mRequestMethod = RequestMethod.valueOf(in.readString());
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
    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    /**
     * @return
     */
    public Bundle getEntityData() {
        return mEntityData;
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
            LOG.e(e.getMessage(), e);
        } catch (InstantiationException e) {
            LOG.e(e.getMessage(), e);
        }
        return new DefaultResponseHandler();
    }

    public RequestComposer getRequestComposer() {
        try {
            return mRequestComposer.newInstance();
        } catch (IllegalAccessException e) {
            LOG.e(e.getMessage(), e);
        } catch (InstantiationException e) {
            LOG.e(e.getMessage(), e);
        }
        return new DefaultRequestComposer();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEndpoint);
        dest.writeMap(mHeaders);
        dest.writeBundle(mEntityData);
        dest.writeBundle(mTemporaryData);

        dest.writeString(mResponseHandler.getName());
        dest.writeString(mRequestComposer.getName());
        dest.writeString(mRequestMethod.name());
    }

    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() {

        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public static class Builder {

        private final Request mRequest;

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
            mRequest.mResponseHandler = responseHandler;
            return this;
        }

        /**
         * @param responseHandler
         * @return
         */
        public Builder setRequestComposer(Class<? extends RequestComposer> requestComposer) {
            mRequest.mRequestComposer = requestComposer;
            return this;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder addHeaderParam(String key, String value) {
            mRequest.mHeaders.put(key, value);
            return this;
        }

        /**
         * @return
         */
        public Bundle getEntityData() {
            return mRequest.mEntityData;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder addTemporaryData(String key, int value) {
            mRequest.mTemporaryData.putInt(key, value);
            return this;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder addTemporaryData(String key, String value) {
            mRequest.mTemporaryData.putString(key, value);
            return this;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder addTemporaryData(String key, boolean value) {
            mRequest.mTemporaryData.putBoolean(key, value);
            return this;
        }

        public Request create() {
            return mRequest;
        }
    }

}
