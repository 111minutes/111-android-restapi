package com.the111min.android.api;

import android.accounts.NetworkErrorException;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * RequestService is a {@link IntentService}s that handles {@link Request}
 * asynchronously.
 * <p>
 * Required permissions:
 * <ul>
 * <li>android.permission.INTERNET
 * <li>android.permission.ACCESS_NETWORK_STATE
 * </ul>
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

    private static final String PACKAGE = "com.the111min.android.api.";

    public static final String EXTRA_REQUESTS = PACKAGE + "EXTRA_REQUESTS";
    public static final String EXTRA_STATUS_RECEIVER = PACKAGE + "EXTRA_RECEIVER";
    public static final String EXTRA_RESPONSE_ERROR_CODE = PACKAGE + "EXTRA_ERROR";
    public static final String EXTRA_RESPONSE = PACKAGE + "EXTRA_RESPONSE";
    public static final String EXTRA_TOKEN = PACKAGE + "EXTRA_TOKEN";
    public static final String EXTRA_RESPONSE_ERROR_MESSAGE = PACKAGE + "EXTRA_ERROR_MESSAGE";

    public static final int STATUS_ERROR = 1;
    public static final int STATUS_OK = 2;

    private final Logger log = Logger.getLogger(RequestService.class);

    private ResultReceiver mReceiver;

    public RequestService() {
        super(TAG);
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        mReceiver = intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
        int token = intent.getIntExtra(EXTRA_TOKEN, BaseApiHelper.DEFAULT_TOKEN);

        if (!isInternetAvailable()) {
            sendError(new NetworkErrorException(), token);
            return;
        }

        final ArrayList<Request> requests = intent.getParcelableArrayListExtra(EXTRA_REQUESTS);

        Response lastResponse = null;
        for (Request request : requests) {
            try {
                final HttpResponse httpResponse = HttpManager.sendRequest(request);
                final ResponseHandler handler = request.getResponseHandler();

                lastResponse = handler.handleResponse(this, httpResponse, request);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                sendError(e, token);
                return;
            } catch (URISyntaxException e) {
                log.error(e.getMessage(), e);
                sendError(e, token);
                return;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sendError(e, token);
                return;
            }
        }

        final Bundle resultBundle = new Bundle();
        if (lastResponse == null) lastResponse = new Response(true);

        resultBundle.putInt(EXTRA_TOKEN, token);
        resultBundle.putParcelable(EXTRA_RESPONSE, lastResponse);

        sendResult(STATUS_OK, resultBundle);
    }

    private void sendResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.send(resultCode, resultData);
        }
    }

    protected void sendError(Exception exception, int token) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RESPONSE_ERROR_CODE, exception);
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
