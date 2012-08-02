package com.the111min.android.api;

import android.os.Bundle;
import android.util.Log;

import com.the111min.android.api.Request.RequestMethod;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * This class manages connection to server via POST, GET, PUT, DELETE methods.
 * @author Dmitry Dobritskiy
 */
class HttpManager {

    private static final String TAG = HttpManager.class.getName();

    public static HttpResponse sendRequest(Request request)
            throws IOException,
            URISyntaxException {
        final HttpRequestBase httpRequest = getHttpRequest(request);

        final Bundle headerParams = request.getHeaders();

        final ArrayList<NameValuePair> headerParamsPair = HttpUtils.getPairsFromBundle(headerParams);

        setupHeader(httpRequest, headerParamsPair);

        final DefaultHttpClient client = new DefaultHttpClient();

        final HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT) 
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        client.setParams(httpParameters);

        return client.execute(httpRequest);
    }

    private static void setupHeader(HttpRequestBase httpRequest,
            ArrayList<NameValuePair> headerParams) {
        for (NameValuePair nameValuePair : headerParams)
            httpRequest.addHeader(nameValuePair.getName(), nameValuePair.getValue());

    }

    private static HttpRequestBase getHttpRequest(Request request) throws URISyntaxException,
            UnsupportedEncodingException {
        final RequestMethod method = request.getRequestMethod();

        final ArrayList<NameValuePair> bodyParams = HttpUtils.getPairsFromBundle(request.getBodyParams());
        if (BuildConfig.DEBUG) Log.d(TAG, "request body: " + bodyParams.toString());

        final URI uri = new URI(request.getEndpoint().replace(" ", "%20"));

        switch (method) {
            case GET:
                if (BuildConfig.DEBUG) Log.d(TAG, "Sending GET " + request.getEndpoint());
                return new HttpGet(uri);

            case POST:
                if (BuildConfig.DEBUG) Log.d(TAG, "Sending POST " + request.getEndpoint());
                final HttpPost post = new HttpPost(uri);
                post.setEntity(new UrlEncodedFormEntity(bodyParams, "UTF-8"));
                return post;

            case PUT:
                if (BuildConfig.DEBUG) Log.d(TAG, "Sending PUT " + request.getEndpoint());
                final HttpPut put = new HttpPut(uri);
                put.setEntity(new UrlEncodedFormEntity(bodyParams, "UTF-8"));
                return put;

            case DELETE:
                if (BuildConfig.DEBUG) Log.d(TAG, "Sending DELETE " + request.getEndpoint());
                return new HttpDelete(uri);

            default:
                throw new IllegalStateException("Unknown request type. Must be POST, PUT or GET");
        }
    }

}