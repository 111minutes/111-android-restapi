package com.the111min.android.api.demo;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.the111min.android.api.ResponseReceiver;

import java.util.ArrayList;

public class DemoActivity extends ListActivity implements ResponseReceiver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Loading list", Toast.LENGTH_LONG).show();
        new ApiHelper(this, this).getCarMakes();
    }

    @Override
    public void onRequestSuccess(int token, Bundle result) {
        ArrayList<String> list = result.getStringArrayList("list");
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
    }

    @Override
    public void onRequestFailure(int token, Bundle result) {
        Toast.makeText(this, "Handle error in ResponseHandler", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(int token, Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

}
