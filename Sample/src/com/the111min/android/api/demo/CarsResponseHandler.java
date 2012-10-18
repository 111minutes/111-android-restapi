package com.the111min.android.api.demo;

import android.content.Context;
import android.os.Bundle;

import com.the111min.android.api.HttpUtils;
import com.the111min.android.api.request.Request;
import com.the111min.android.api.response.ResponseHandler;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarsResponseHandler extends ResponseHandler {

    @Override
    public boolean handleResponse(Context context, HttpResponse response, Request request,
            Bundle result) throws Exception {
        final String text = HttpUtils.readHttpResponse(response);

        final JSONObject obj = new JSONObject(text);
        final JSONArray items = obj.getJSONArray("data");

        final ArrayList<String> strings = new ArrayList<String>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            strings.add(item.getString("name"));
        }

        result.putStringArrayList("list", strings);

        return true;
    }

}
