package com.the111min.android.api.response;

import android.content.Context;
import android.os.Bundle;

import com.the111min.android.api.request.Request;

import org.apache.http.HttpResponse;

/**
 * Implement {@link ResponseHandler} for processing information sent from server.
 */
public abstract class ResponseHandler {

    /**
     * Implements the parsing data getting from {@link HttpResponse}.
     * @param context
     * @param response
     * @param request
     * @param result
     * @return true for successfull responses, false otherwise.
     * @throws Exception
     */
    public abstract boolean handleResponse(Context context, HttpResponse response,
            Request request, Bundle result) throws Exception;

}
