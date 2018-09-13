package com.szcbit.SGFlexSys;

import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.szcbit.x1.IntrusionDetection;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Sample Activity for Intrusion Detection
 */
public class IntrusionActivity extends AppCompatActivity {

    protected IntrusionDetection id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrusion);

        setTitle(R.string.button_intrusion_detection);
        // create an IntrusionDetection object
        id = new IntrusionDetection(this, new ResultReceiver(new Handler()) {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                // when the IntrusionDetection object is ready, refresh intrusion detection status
                refreshStatus();
            }
        });
    }

    /*
     * Refresh intrusion detection status
     */
    protected void refreshStatus() {
        String strHacked = "N/A";
        String strDate = "N/A";

        try {
            boolean hacked = id.isHacked();
            Date hackedDate = id.getHackedTime();

            if (hacked) {
                strHacked = "HACKED";
                strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(hackedDate);
            }
        }
        catch(Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        ((TextView)findViewById(R.id.labelHacked)).setText(strHacked);
        ((TextView)findViewById(R.id.labelHackedDate)).setText(strDate);
    }

    public void onButtonIntrusionReset(View v) {
        try {
            // clear the intrusion detection flag
            id.reset();
            refreshStatus();
        }
        catch(Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
