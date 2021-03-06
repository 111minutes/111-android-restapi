package com.the111min.android.api.util;

import android.os.Bundle;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class HttpUtils {

    /**
     * Retrieve params from bundle and converts them to list of NameValuePair
     * @param params - bundle with params
     * @return list of NameValuePair
     */
    public static ArrayList<NameValuePair> getPairsFromBundle(Bundle params) {
        final ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
        final Set<String> keySet = params.keySet();

        final Object[] keys = keySet.toArray();
        for (int i = 0; i < params.size(); i++) {
            NameValuePair pair = new BasicNameValuePair((String) keys[i], params.getString((String) keys[i]));
            pairs.add(pair);
        }

        return pairs;
    }

    /**
     * Reads http-response from server and converts it into String
     * @param response - Response from server
     * @return string representation of a response
     */
    public static String readHttpResponse(HttpResponse response) {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer("");

        Header contentEncoding = response.getFirstHeader("Content-Encoding");
        InputStream is;
        try {
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                is = new GZIPInputStream(response.getEntity().getContent());
            } else {
                is = response.getEntity().getContent();
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
