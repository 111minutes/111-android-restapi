package com.the111min.android.api;

import android.content.Context;

import org.apache.http.HttpResponse;

/**
 * Implement ResponseHandler for processing information sent from server.
 */
public abstract class ResponseHandler {

    /**
     * Implements the parsing data getting from {@link HttpResponse}.
     */
    public abstract Response handleResponse(Context context, HttpResponse response,
            Request request) throws Exception;

}
