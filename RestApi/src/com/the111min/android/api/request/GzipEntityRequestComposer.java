package com.the111min.android.api.request;

import com.the111min.android.api.util.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;

public class GzipEntityRequestComposer extends RequestComposer {

    private static final Logger LOG = Logger.getInstance(DefaultRequestComposer.class.getSimpleName());

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
            LOG.e(e1.getMessage(), e1);
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
    protected void setupEntity(HttpEntity entity) {
        super.setupEntity(entity);
        AbstractHttpEntity absEntity = (AbstractHttpEntity) entity;
        absEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_GZIP_JSON));
        absEntity.setContentEncoding(ENCODING_GZIP);
    }

    @Override
    protected void setupRequestExtras(HttpRequestBase httpRequest, Request request) {
        httpRequest.addHeader("Accept-Encoding", ENCODING_GZIP);
    }

}
