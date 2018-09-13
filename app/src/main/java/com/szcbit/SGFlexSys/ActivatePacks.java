package com.szcbit.SGFlexSys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.CompoundButton;
import android.widget.Toast;

import android.widget.TableRow.LayoutParams;



import com.szcbit.x1.Device;
import com.szcbit.x1.ScanEngine;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivatePacks extends AppCompatActivity {

    CheckBox singlescan;
    ScanEngine scanengine = null;
    TextView tolabel, fromlable ;
    EditText topack, frompack;
    String result, result2;
    Button addtolist, reset,activate;
    TableLayout t1;
    Long securityID, userID, tc, sc;
    String packranges, StatusString;
    final ClsTransactionCheck transcheck = new ClsTransactionCheck();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_packs);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        frompack = (EditText)findViewById(R.id.editText3);
        tolabel = (TextView) findViewById(R.id.textView6);
        topack = (EditText) findViewById(R.id.editText4);
        singlescan = (CheckBox) findViewById(R.id.checkBox);
        addtolist = (Button)findViewById(R.id.button4);

        t1 = (TableLayout) findViewById(R.id.tblayout);

        reset = (Button)findViewById(R.id.button5);
        activate = (Button)findViewById(R.id.button6);

        // when the trigger mode radio changes, reopen the scanner
        ((RadioGroup)findViewById(R.id.radioTrigger)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                ((Button)findViewById(R.id.btnSoftTrigger)).setEnabled(((RadioGroup)findViewById(R.id.radioTrigger)).getCheckedRadioButtonId() == R.id.radioSoft);
                openScanner();


            }
        });

     singlescan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tolabel.setVisibility(View.INVISIBLE);
                    topack.setVisibility(View.INVISIBLE);
                    topack.setText(common.ticketresultfrom);
                    }
                else
                    {
                    tolabel.setVisibility(View.VISIBLE);
                    topack.setVisibility(View.VISIBLE);
                    topack.setText("");
                }

            }
            });


        addtolist.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Long frmpck =  Long.parseLong(common.ticketresultfrom.substring(3,9));
                Long topck =  Long.parseLong(common.ticketresultto.substring(3,9));

                if(frompack.getText().length() < 9 || topack.getText().length() < 9)

                {
                  Toast.makeText(ActivatePacks.this, "Enter valid Packs", Toast.LENGTH_LONG).show();
                    frompack.setText("");
                    topack.setText("");
                    frompack.requestFocus();

                }



                else if( frmpck > topck)

                {
                    Toast.makeText(ActivatePacks.this, "To Pack is less than From Pack", Toast.LENGTH_LONG).show();
                    frompack.setText("");
                    topack.setText("");
                    frompack.requestFocus();

                }
                else
                {

                    singlescan.setVisibility(View.INVISIBLE);
                    addtolist.setVisibility(View.INVISIBLE);
                    reset.setVisibility(View.VISIBLE);
                    activate.setVisibility(View.VISIBLE);

                    common.counter++;

                    TableRow newRow = new TableRow(ActivatePacks.this);
                    TextView slno = new TextView(ActivatePacks.this);
                    TextView frm = new TextView(ActivatePacks.this);
                    TextView to = new TextView(ActivatePacks.this);


                    slno.setText(String.valueOf(common.counter));
                    frm.setText(frompack.getText());
                    to.setText(topack.getText());

                    newRow.addView(slno);
                    newRow.addView(frm);
                    newRow.addView(to);

                    t1.addView(newRow);

                    frompack.setText("");
                    topack.setText("");

                }

            }
        });


        activate.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Date datetime = new Date();
                SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
                String strDate2= formatter2.format(datetime);

                String day = (strDate2.substring(0, 2));
                String month = (strDate2.substring(3, 5));
                String year = (strDate2.substring(6, 8));
                String hour = (strDate2.substring(9, 11));
                String min = (strDate2.substring(12, 14));
                String sec = (strDate2.substring(15, 17));


                securityID = common.securityID;
                userID = common.userID;
                packranges = null;

                String frmpck = common.ticketresultfrom.substring(0,9);
                String topck = common.ticketresultto.substring(0,9);
                packranges = (frmpck +"," + topck);

                StatusString = "ACTIVATE";

                tc = transcheck.TransactionCheck;

                String SecCode = (day + month + year+ hour+ min+ sec);
                long secco = Long.parseLong(SecCode);


                JSONObject postData = new JSONObject();
                try {
                    postData.put("SecurityID", securityID);
                    postData.put("UserID", userID);
                    postData.put("TicketBarcode", packranges);

                    postData.put("TC", tc);
                    postData.put("SC", secco);
                    postData.put("StatusString", StatusString);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if (postData.length() > 0) {

                    //new BarcodeActivity.SendJsonDataToServer().execute(String.valueOf(postData));
                }

            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                addtolist.setVisibility(View.VISIBLE);
                reset.setVisibility(View.INVISIBLE);
                activate.setVisibility(View.INVISIBLE);

               common.counter = 0;


                t1.removeViewAt(1);
                singlescan.setVisibility(View.VISIBLE);
                singlescan.setChecked(false);


            }
        });

    }


    public void openScanner() {
        // get current selected trigger mode
        ScanEngine.TriggerMode mode;
        // boolean filterDuplicated = ((CheckBox)findViewById(R.id.checkDuplicated)).isChecked();
        int id = ((RadioGroup)findViewById(R.id.radioTrigger)).getCheckedRadioButtonId();
        switch(id) {

            case R.id.radioSoft:
                mode = ScanEngine.TriggerMode.Software;
                break;
            default:
                mode = ScanEngine.TriggerMode.Key;
                break;
        }
        // open scan engine
        try {
            scanengine.open(new ResultReceiver(new Handler()) {
                @Override
                public void onReceiveResult(int resultCode, Bundle resultData) {
                    super.onReceiveResult(resultCode, resultData);

                    result = resultData.getString("bar");

                  //  common.ticketresultfrom = result;


                    if (frompack.getText().length() <= 9 )
                    {
                        common.ticketresultfrom = result;

                        frompack.setText(common.ticketresultfrom);

                        topack.requestFocus();
                        //common.ticketresultfrom = null;

                    }

                    else
                    {
                        common.ticketresultto = result;

                        topack.setText(result);

                    }

                }
            }, ScanEngine.TriggerMode.Key, false);

        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    class SendJsonDataToServer extends AsyncTask<String,String,String> {

        @Override
        public String doInBackground(String... params) {

            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("http://10.7.65.112/SGVSWebAPIs/API/Pack/UpdatePackStatus");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
// json data
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
//input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }

                // JSONObject jsonObject = new JSONObject(reader);

                //JsonResponse = buffer.toString();

//response data
                // Log.i(TAG,JsonResponse);
                try {
//send to post execute
                    JSONObject vali = new JSONObject(new String(buffer));

                    if(vali.getString("Winner").equals("false"))
                    {
                        // resultfrmserver.setText("This is not a winning Ticket");


                    }
                    else
                    {


                    }

                    return JsonResponse;
                }

                catch (Exception e)

                {
                    e.printStackTrace();
                }

                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        //Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if(scanengine == null) {
            // create a ScanEngine object
            scanengine = new ScanEngine(this, new ResultReceiver(new Handler()) {
                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    // when the ScanEngine object is ready, open scanner immediately
                    openScanner();
                }
            });
        }
        else {
            // the ScanEngine already exists, just open the scanner
            openScanner();
        }
    }

    @Override
    public void onPause() {
        try {
            // release scanner when we are not foreground activity
            scanengine.close();

        }
        catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        super.onPause();
    }

    public void onSoftTrigger(View v) {
        try {
            // in software triggered mode, pull the trigger when the button is clicked
            scanengine.pullSoftTrigger();
        }
        catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}
