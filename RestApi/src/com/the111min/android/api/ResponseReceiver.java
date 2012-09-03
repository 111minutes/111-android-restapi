package com.the111min.android.api;

import android.app.Activity;

/**
 * Listening {@link Activity} must implements {@link ResponseReceiver} for
 * getting result from {@link ResponseHandler}
 */
public interface ResponseReceiver {

    /**
     * Notifies about successfull {@link Request} execution
     */
    public void onRequestSuccess(int token, Response response);

    /**
     * Notifies about error {@link Request} execution
     */
    public void onRequestError(int token, Exception errorCode);

}
