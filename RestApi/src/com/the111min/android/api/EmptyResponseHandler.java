package com.the111min.android.api;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;

/**
 * Implement the interface to parsing response from server.
 */
public class EmptyResponseHandler extends ResponseHandler {

    private static final String TAG = EmptyResponseHandler.class.getSimpleName();

    @Override
    public Response handleResponse(Context context, HttpResponse response, Request request) {
        Log.d(TAG, HttpUtils.readHttpResponse(response));
        return new Response(true);
    }

}
