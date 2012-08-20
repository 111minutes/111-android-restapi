package com.the111min.android.api;

import android.os.Bundle;
import android.text.TextUtils;

import com.the111min.android.api.Request.RequestMethod;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

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
    private final static Logger LOG = Logger.getLogger(HttpManager.class);

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

        HttpEntity entity = null;
        if (TextUtils.isEmpty(request.getStringEntity())) {
            final ArrayList<NameValuePair> bodyParams = HttpUtils.getPairsFromBundle(request.getBodyParams());
            entity = new UrlEncodedFormEntity(bodyParams, "UTF-8");
            LOG.debug("request body: " + bodyParams.toString());
        } else {
            entity = new StringEntity(request.getStringEntity(), "UTF-8");
        }

        final URI uri = new URI(request.getEndpoint().replace(" ", "%20"));

        switch (method) {
            case GET:
                LOG.debug("Sending GET " + request.getEndpoint());
                return new HttpGet(uri);

            case POST:
                LOG.debug("Sending POST " + request.getEndpoint());
                final HttpPost post = new HttpPost(uri);
                post.setEntity(entity);
                return post;

            case PUT:
                LOG.debug("Sending PUT " + request.getEndpoint());
                final HttpPut put = new HttpPut(uri);
                put.setEntity(entity);
                return put;

            case DELETE:
                LOG.debug("Sending DELETE " + request.getEndpoint());
                return new HttpDelete(uri);

            default:
                throw new IllegalArgumentException("Unknown request type. Must be GET, POST, PUT or DELETE");
        }
    }

}
