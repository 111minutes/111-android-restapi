package com.the111min.android.api;

import android.os.Bundle;
import android.text.TextUtils;
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
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * This class manages connection to server via POST, GET, PUT, DELETE methods.
 */
class HttpManager {

    private static final String TAG = RequestService.class.getSimpleName();

    private static DefaultHttpClient sHttpClient;

    public static HttpResponse sendRequest(Request request)
            throws IOException, URISyntaxException {
        final HttpRequestBase httpRequest = getHttpRequest(request);
        final Bundle headerParams = request.getHeaders();
        final ArrayList<NameValuePair> headerParamsPair = HttpUtils.getPairsFromBundle(headerParams);

        setupHeader(httpRequest, headerParamsPair);

        return getHttpClient().execute(httpRequest);
    }

    private static DefaultHttpClient getHttpClient() {
        if (sHttpClient == null) {
            sHttpClient = new DefaultHttpClient();
            final HttpParams httpParameters = new BasicHttpParams();

            int timeoutConnection = 30000;
            int timeoutSocket = 30000;

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            sHttpClient.setParams(httpParameters);
        }
        return sHttpClient;
    }

    private static void setupHeader(HttpRequestBase httpRequest,
            ArrayList<NameValuePair> headerParams) {
        for (NameValuePair nameValuePair : headerParams)
            httpRequest.addHeader(nameValuePair.getName(), nameValuePair.getValue());
    }

    private static HttpRequestBase getHttpRequest(Request request) throws URISyntaxException,
            UnsupportedEncodingException {
        final RequestMethod method = request.getRequestMethod();

        AbstractHttpEntity entity = null;
        if (TextUtils.isEmpty(request.getStringEntity())) {
            final ArrayList<NameValuePair> bodyParams = HttpUtils.getPairsFromBundle(request.getBodyParams());
            entity = new UrlEncodedFormEntity(bodyParams, "UTF-8");
            Log.d(TAG, "request body: " + bodyParams.toString());
        } else {
            entity = new StringEntity(request.getStringEntity(), "UTF-8");
            //TODO: add ability to set content type
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            Log.d(TAG, "request body: " + request.getStringEntity());
        }

        final URI uri = new URI(request.getEndpoint().replace(" ", "%20"));

        switch (method) {
            case GET:
                Log.d(TAG, "Sending GET " + request.getEndpoint());
                return new HttpGet(uri);

            case POST:
                Log.d(TAG, "Sending POST " + request.getEndpoint());
                final HttpPost post = new HttpPost(uri);
                post.setEntity(entity);
                return post;

            case PUT:
                Log.d(TAG, "Sending PUT " + request.getEndpoint());
                final HttpPut put = new HttpPut(uri);
                put.setEntity(entity);
                return put;

            case DELETE:
                Log.d(TAG, "Sending DELETE " + request.getEndpoint());
                return new HttpDelete(uri);

            default:
                throw new IllegalArgumentException("Unknown request type. Must be GET, POST, PUT or DELETE");
        }
    }

}
