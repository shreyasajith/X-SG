package com.szcbit.SGFlexSys;

import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import org.json.JSONStringer;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.*;
import android.app.AlertDialog;

import com.szcbit.x1.Device;

/*
 * Sample Activity - Main Menu
 */
public class DeviceTestActivity extends AppCompatActivity {

    protected Device dev = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_test);

        // create a Device object
        dev = new Device(this, new ResultReceiver(new Handler()) {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                // when the Device object is ready
                String ver = "N/A";

                try {
                    // get the Device Manager version
                  //  ver = dev.getSDKVersion();




                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DeviceTestActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
               // ((TextView) findViewById(R.id.labelSDKVer)).setText(ver);
            }
        });
    }

    public void onButtonSerial(View v) {
        startActivity(new Intent(this, HardwareSerialActivity.class));
    }

    public void onButtonIntrusion(View v) {
       // startActivity(new Intent(this, IntrusionActivity.class));
    }

    public void onButtonBarcode(View v) {
       // startActivity(new Intent(this, BarcodeActivity.class));
    }
}
