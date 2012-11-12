package com.the111min.android.api.request;

import android.content.Context;

import com.the111min.android.api.request.Request.RequestMethod;
import com.the111min.android.api.util.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public abstract class RequestComposer {

    private static final Logger LOG = Logger.getInstance(RequestComposer.class.getSimpleName());

    protected abstract HttpEntity getEntity(Request request) throws UnsupportedEncodingException;

    public HttpRequestBase composeRequest(Context context, Request request) throws URISyntaxException,
            UnsupportedEncodingException {
        final HttpRequestBase httpRequest = getHttpRequest(request);

        setupRequestHeaders(httpRequest, request);
        setupRequestExtras(httpRequest, request);

        return httpRequest;
    }

    /**
     * Override to add content type, content encoding
     * @param entity
     */
    protected void setupEntity(HttpEntity entity) {
    }

    /**
     * Use this to add extra headers, whatever
     * @param httpRequest
     * @param request
     */
    protected void setupRequestExtras(HttpRequestBase httpRequest, Request request) {
    }

    private HttpRequestBase getHttpRequest(Request request) throws URISyntaxException, UnsupportedEncodingException {
        final URI uri = new URI(request.getEndpoint().replace(" ", "%20"));
        final RequestMethod method = request.getRequestMethod();

        switch (method) {
            case GET:
                LOG.d("Sending GET " + request.getEndpoint());
                return new HttpGet(uri);

            case POST:
                LOG.d("Sending POST " + request.getEndpoint());
                final HttpPost post = new HttpPost(uri);
                final HttpEntity postEntity = getEntity(request);
                setupEntity(postEntity);
                post.setEntity(postEntity);

                return post;

            case PUT:
                LOG.d("Sending PUT " + request.getEndpoint());
                final HttpPut put = new HttpPut(uri);
                final HttpEntity putEntity = getEntity(request);
                setupEntity(putEntity);
                put.setEntity(putEntity);

                return put;

            case DELETE:
                LOG.d("Sending DELETE " + request.getEndpoint());
                return new HttpDelete(uri);

            default:
                throw new IllegalArgumentException("Unknown request type. Must be GET, POST, PUT or DELETE");
        }

    }

    private void setupRequestHeaders(HttpRequestBase httpRequest, Request request) {
        final HashMap<String, String> headers = request.getHeaders();
        for (String key : headers.keySet()) {
            final String value = headers.get(key);
            LOG.d("Adding header " + key + " : " + value);
            httpRequest.addHeader(key, value);
        }
    }

}
