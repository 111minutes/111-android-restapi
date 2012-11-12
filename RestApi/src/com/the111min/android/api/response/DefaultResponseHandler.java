package com.the111min.android.api.response;

import android.content.Context;
import android.os.Bundle;

import com.the111min.android.api.request.Request;
import com.the111min.android.api.util.HttpUtils;
import com.the111min.android.api.util.Logger;

import org.apache.http.HttpResponse;

/**
 * Implement the interface to handle response from server.
 */
public class DefaultResponseHandler extends ResponseHandler {

    private static final Logger LOG = Logger.getInstance(DefaultResponseHandler.class.getSimpleName());

    @Override
    public boolean handleResponse(Context context, HttpResponse response, Request request,
            Bundle result) {
        LOG.d("Response: " + HttpUtils.readHttpResponse(response));
        return true;
    }

}
