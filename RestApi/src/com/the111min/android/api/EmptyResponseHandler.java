package com.the111min.android.api;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

/**
 * Implement the interface to parsing response from server.
 */
class EmptyResponseHandler extends ResponseHandler {

    private static final Logger LOG = Logger.getLogger(EmptyResponseHandler.class);

    @Override
    public Response handleResponse(Context context, HttpResponse response, Request request) {
        LOG.debug(HttpUtils.readHttpResponse(response));
        return new Response(true);
    }

}
