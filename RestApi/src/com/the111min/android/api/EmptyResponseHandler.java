package com.the111min.android.api;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement the interface to parsing response from server.
 */
public class EmptyResponseHandler extends ResponseHandler {

    private static final String TAG = EmptyResponseHandler.class.getSimpleName();
    private static final Logger LOG = LoggerFactory.getLogger(TAG);

    @Override
    public Response handleResponse(Context context, HttpResponse response, Request request) {
        LOG.debug("Response: {}", HttpUtils.readHttpResponse(response));
        return new Response(true);
    }

}
