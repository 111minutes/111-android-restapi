package com.the111min.android.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This class manages connection to server via POST, GET, PUT, DELETE methods.
 */
class HttpManager {

    private static DefaultHttpClient sHttpClient;

    public static HttpResponse sendRequest(HttpRequestBase request)
            throws IOException, URISyntaxException {
        return getHttpClient().execute(request);
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

}
