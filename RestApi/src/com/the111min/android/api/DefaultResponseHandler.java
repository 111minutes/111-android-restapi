package com.the111min.android.api;

import android.content.Context;
import android.os.Bundle;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement the interface to parsing response from server.
 */
public class DefaultResponseHandler extends ResponseHandler {

    private static final String TAG = DefaultResponseHandler.class.getSimpleName();
    private static final Logger LOG = LoggerFactory.getLogger(TAG);

    @Override
    public boolean handleResponse(Context context, HttpResponse response, Request request,
            Bundle result) {
        LOG.debug("Response: {}", HttpUtils.readHttpResponse(response));
        return true;
    }

}
