package com.the111min.android.api.request;

import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

public class DefaultRequestComposer extends RequestComposer {

    @Override
    protected AbstractHttpEntity getEntity(Request request) throws UnsupportedEncodingException {
        return new StringEntity("");
    }
}
