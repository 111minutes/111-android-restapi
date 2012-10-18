package com.the111min.android.api.request;

import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

public class StringEntityRequestComposer extends RequestComposer {

    public static final String ENTITY_DATA = "entity_data";

    @Override
    protected final AbstractHttpEntity getEntity(Request request) throws UnsupportedEncodingException {
        final String body = request.getEntityData().getString(ENTITY_DATA);
        return new StringEntity(body);
    }

}
