package com.the111min.android.api.request;

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
import java.util.zip.GZIPOutputStream;

public class GzipEntityRequestComposer extends RequestComposer {

    private static final String TAG = DefaultRequestComposer.class.getSimpleName();
    private static final Logger LOG = LoggerFactory.getLogger(TAG);

    private static final String ENCODING_GZIP = "gzip";
    private static final String CONTENT_TYPE_GZIP_JSON = "gzip/json";

    public static final String ENTITY_DATA = "entity_data";

    @Override
    protected AbstractHttpEntity getEntity(Request request) throws UnsupportedEncodingException {
        final String body = request.getEntityData().getString(ENTITY_DATA);
        return newGzippedEntity(body);
    }

    private AbstractHttpEntity newGzippedEntity(String text) throws UnsupportedEncodingException {
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

    @Override
    protected void setupEntity(AbstractHttpEntity entity) {
        super.setupEntity(entity);
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_GZIP_JSON));
        entity.setContentEncoding(ENCODING_GZIP);
    }

    @Override
    protected void setupRequestExtras(HttpRequestBase httpRequest, Request request) {
        httpRequest.addHeader("Accept-Encoding", ENCODING_GZIP);
    }

}
