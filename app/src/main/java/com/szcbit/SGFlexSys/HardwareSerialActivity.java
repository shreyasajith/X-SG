package com.szcbit.SGFlexSys;

import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.szcbit.x1.Device;


public class HardwareSerialActivity extends AppCompatActivity {

    protected String hardwareSN = "N/A";

    protected Device dev = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware_serial);

        setTitle(R.string.button_serial_number);

        // create a Device object
        dev = new Device(this, new ResultReceiver(new Handler()) {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                // when the Device object is ready
                try {
                        // get those hardware related IDs
                    hardwareSN = dev.getHardwareSerial();
                }
                catch(Exception e)

                {
                    e.printStackTrace();
                 //   Toast.makeText(HardwareSerialActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                ((TextView)findViewById(R.id.labelSerial)).setText(hardwareSN);
            }
        });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          }
}
