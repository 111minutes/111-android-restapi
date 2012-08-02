package com.the111min.android.api;

import android.os.Bundle;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

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

        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
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
