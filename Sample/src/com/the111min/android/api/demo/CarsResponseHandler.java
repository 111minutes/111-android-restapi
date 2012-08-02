package com.the111min.android.api.demo;

import android.content.Context;

import com.the111min.android.api.HttpUtils;
import com.the111min.android.api.Request;
import com.the111min.android.api.Response;
import com.the111min.android.api.ResponseHandler;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarsResponseHandler extends ResponseHandler {

    @Override
    public Response handleResponse(Context context, HttpResponse response, Request request)
            throws Exception {
        final String text = HttpUtils.readHttpResponse(response);

        final JSONObject obj = new JSONObject(text);
        final JSONArray items = obj.getJSONArray("data");

        final ArrayList<String> strings = new ArrayList<String>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            strings.add(item.getString("name"));
        }

        final Response result = new Response(true);
        result.addStringArrayResult("list", strings);

        return result;
    }

}
