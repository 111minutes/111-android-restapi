package com.the111min.android.api;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.the111min.android.api.request.Request;
import com.the111min.android.api.response.ResponseReceiver;

import java.util.ArrayList;

/**
 * Implement to describe each http request.
 */
public abstract class BaseApiHelper {

    public static final int DEFAULT_TOKEN = -1;

    protected final Context mContext;
    protected DetachableResultReceiver mResultReceiver;

    protected BaseApiHelper(Context context, ResponseReceiver receiver) {
        mContext = context;
        if (receiver != null) {
            mResultReceiver = new DetachableResultReceiver(new Handler());
            mResultReceiver.setReceiver(receiver);
        }
    }

    protected void sendRequest(Request request) {
        sendRequest(request, DEFAULT_TOKEN);
    }

    protected void sendRequest(Request request, int token) {
        final ArrayList<Request> requests = new ArrayList<Request>(1);
        requests.add(request);
        sendRequest(requests, token);
    }

    protected void sendRequest(ArrayList<Request> requests, int token) {
        final Intent intent = new Intent(mContext, RequestService.class);

        intent.putParcelableArrayListExtra(RequestService.EXTRA_REQUESTS, requests);
        intent.putExtra(RequestService.EXTRA_TOKEN, token);
        if (mResultReceiver != null) {
            intent.putExtra(RequestService.EXTRA_STATUS_RECEIVER, mResultReceiver);
        }
        mContext.startService(intent);
    }

}
