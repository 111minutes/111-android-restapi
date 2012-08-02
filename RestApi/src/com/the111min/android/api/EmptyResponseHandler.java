package com.the111min.android.api;

import android.content.Context;

import org.apache.http.HttpResponse;

/**
 * Implement the interface to parsing response from server.
 */
class EmptyResponseHandler extends ResponseHandler {

    @Override
    public Response handleResponse(Context context, HttpResponse response, Request request) {
        return new Response(true);
    }

}
