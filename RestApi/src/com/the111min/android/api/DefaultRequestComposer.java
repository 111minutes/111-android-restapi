package com.the111min.android.api;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.the111min.android.api.Request.RequestMethod;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

public class DefaultRequestComposer extends RequestComposer {

    private static final String TAG = DefaultRequestComposer.class.getSimpleName();
    private static final Logger LOG = LoggerFactory.getLogger(TAG);

    private static final String ENCODING_GZIP = "gzip";
    private static final String CONTENT_TYPE_GZIP_JSON = "gzip/json";

    @Override
    public HttpRequestBase composeRequest(Context context, Request request)
            throws UnsupportedEncodingException, URISyntaxException {
        final HttpRequestBase httpRequest = getHttpRequest(request);
        final Bundle headerParams = request.getHeaders();
        final ArrayList<NameValuePair> headerParamsPair = HttpUtils.getPairsFromBundle(headerParams);

        setupHeader(httpRequest, headerParamsPair);

        //TODO: fix all gzip-connected logic
        httpRequest.addHeader("Accept-Encoding", ENCODING_GZIP);

        return httpRequest;
    }

    private static void setupHeader(HttpRequestBase httpRequest,
            ArrayList<NameValuePair> headerParams) {
        for (NameValuePair nameValuePair : headerParams) {
            httpRequest.addHeader(nameValuePair.getName(), nameValuePair.getValue());
        }
    }

    private static HttpRequestBase getHttpRequest(Request request) throws URISyntaxException,
            UnsupportedEncodingException {
        final RequestMethod method = request.getRequestMethod();

        AbstractHttpEntity entity = null;
        if (TextUtils.isEmpty(request.getStringEntity())) {
            final ArrayList<NameValuePair> bodyParams = HttpUtils.getPairsFromBundle(request.getBodyParams());
            entity = new UrlEncodedFormEntity(bodyParams, "UTF-8");
            LOG.debug("request body: " + bodyParams.toString());
        } else {
            //TODO: temporary fix for TextBuster
            //entity = new StringEntity(request.getStringEntity(), "UTF-8");
            entity = toGzippedEntity(request.getStringEntity());
            //TODO: add ability to set content type
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_GZIP_JSON));
            entity.setContentEncoding(ENCODING_GZIP);

            LOG.debug("request body: " + request.getStringEntity());
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

    private static AbstractHttpEntity toGzippedEntity(String text) throws
            UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = null;

        try {
            gzos = new GZIPOutputStream(baos);
            gzos.write(text.getBytes("UTF-8"));
        } catch (IOException e1) {
            LOG.error(e1.getMessage(), e1);
        } finally {
            if (gzos != null) try {
                gzos.close();
            } catch (IOException ignore) {
            };
        }

        byte[] bytes = baos.toByteArray();
        ByteArrayEntity e = new ByteArrayEntity(bytes);

        return e;
    }

}
