package com.the111min.android.api.demo;

import android.content.Context;

import com.the111min.android.api.BaseApiHelper;
import com.the111min.android.api.request.Request;
import com.the111min.android.api.request.Request.RequestMethod;
import com.the111min.android.api.response.ResponseReceiver;

public class ApiHelper extends BaseApiHelper {

    protected ApiHelper(Context context, ResponseReceiver receiver) {
        super(context, receiver);
    }

    public void getCarMakes() {
        Request.Builder builder = new Request.Builder(
                "http://buyersguide.caranddriver.com/api/feed/?mode=json&q=make", RequestMethod.GET)
                .setResponseHandler(CarsResponseHandler.class);

        sendRequest(builder.create());
    }

}
