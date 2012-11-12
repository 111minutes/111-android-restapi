package com.the111min.android.api;

import android.accounts.NetworkErrorException;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.the111min.android.api.request.Request;
import com.the111min.android.api.request.RequestComposer;
import com.the111min.android.api.response.ResponseHandler;
import com.the111min.android.api.util.Logger;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.ArrayList;

/**
 * RequestService is a {@link IntentService}s that handles {@link Request} asynchronously.
 * <p>Required permissions:
 * <ul>
 * <li>android.permission.INTERNET
 * <li>android.permission.ACCESS_NETWORK_STATE
 * <li>android.permission.WAKE_LOCK
 * </ul>
 * 
 * Manifest registration is required:
 * 
 * <pre>
 *  &lt;service android:name="com.the111min.android.api.RequestService" />
 * </pre>
 * 
 * </code>
 */
public class RequestService extends WakefulIntentService {

    private static final String TAG = RequestService.class.getSimpleName();
    private static final Logger LOG = Logger.getInstance(TAG);

    private static final String PACKAGE = "com.the111min.android.api.";

    static final String EXTRA_REQUESTS = PACKAGE + "EXTRA_REQUESTS";
    static final String EXTRA_STATUS_RECEIVER = PACKAGE + "EXTRA_RECEIVER";

    static final String EXTRA_RESPONSE_EXCEPTION = PACKAGE + "EXTRA_RESPONSE_EXCEPTION";
    static final String EXTRA_TOKEN = PACKAGE + "EXTRA_TOKEN";

    static final int STATUS_REQUEST_SUCCESS = 1;
    static final int STATUS_REQUEST_FAILED = 2;
    static final int STATUS_ERROR = 3;

    private ResultReceiver mReceiver;

    public RequestService() {
        super(TAG);
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        mReceiver = intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
        int token = intent.getIntExtra(EXTRA_TOKEN, BaseApiHelper.DEFAULT_TOKEN);

        if (!isInternetAvailable()) {
            sendError(token, new NetworkErrorException());
            return;
        }

        final ArrayList<Request> requests = intent.getParcelableArrayListExtra(EXTRA_REQUESTS);

        Bundle lastResultData = null;
        boolean lastResult = true;
        for (Request request : requests) {
            try {
                final RequestComposer composer = request.getRequestComposer();
                final HttpRequestBase httpRequest = composer.composeRequest(this, request);
                final HttpResponse httpResponse = HttpRequestSender.sendRequest(httpRequest);
                final ResponseHandler handler = request.getResponseHandler();

                lastResultData = new Bundle();
                lastResult = handler.handleResponse(this, httpResponse, request, lastResultData);
            } catch (Exception e) {
                sendError(token, e);
                return;
            }
        }

        if (lastResultData == null) lastResultData = new Bundle();
        lastResultData.putInt(EXTRA_TOKEN, token);

        if (lastResult) {
            sendResult(STATUS_REQUEST_SUCCESS, lastResultData);
        } else {
            sendResult(STATUS_REQUEST_FAILED, lastResultData);
        }
    }

    private void sendResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.send(resultCode, resultData);
        }
    }

    protected void sendError(int token, Exception e) {
        LOG.e(e.getMessage(), e);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RESPONSE_EXCEPTION, e);
        bundle.putInt(EXTRA_TOKEN, token);
        sendResult(STATUS_ERROR, bundle);
    }

    private boolean isInternetAvailable() {
        final ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = manager.getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return false;
        }
        return true;
    }

}
