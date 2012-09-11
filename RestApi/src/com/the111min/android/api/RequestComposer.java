package com.the111min.android.api;

import android.content.Context;

import org.apache.http.client.methods.HttpRequestBase;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public abstract class RequestComposer {

    public abstract HttpRequestBase composeRequest(Context context, Request request)
            throws UnsupportedEncodingException, URISyntaxException;

}
