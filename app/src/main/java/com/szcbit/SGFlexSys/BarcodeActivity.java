package com.szcbit.SGFlexSys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.szcbit.x1.ScanEngine;

import org.json.JSONException;
import org.json.JSONObject;


/*
 * Sample Activity for scanning bar codes
 */
public class BarcodeActivity extends AppCompatActivity {

         TextView textResult, resultfrmserver;
         EditText Validate;
        ScanEngine scanengine = null;
        String Ticket_no, result, sec, validationdate, FirstName, LastName, NationalID, PassportNo, Winner, winningamount,Cashed;
        Long userID, securityID, tc,sc, seccode;
        int ShowPayNowLater,ShownPlayerTaxDetails, Resident;

        Button clear;
    final ClsTransactionCheck transcheck = new ClsTransactionCheck();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        setTitle("Validate Ticket");
        textResult = (TextView)findViewById(R.id.textView2);
        resultfrmserver = (TextView)findViewById(R.id.textView4);
        Validate = (EditText)findViewById(R.id.editText6);
        clear = (Button) findViewById(R.id.button3);

                // when the trigger mode radio changes, reopen the scanner
        ((RadioGroup)findViewById(R.id.radioTrigger)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                ((Button)findViewById(R.id.btnSoftTrigger)).setEnabled(((RadioGroup)findViewById(R.id.radioTrigger)).getCheckedRadioButtonId() == R.id.radioSoft);
                openScanner();

                }
                });

    }

    /*
     * open the scan engine
     */
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
                    // when barcode is scanned, show it

                     result = resultData.getString("bar");
                    //ScanEngine.Symbology sym = new ScanEngine.Symbology(resultData.getInt("symbology"));
                   // textResult.setText("Ticket_number" + "\n" + result);
                    common.ticketresult = result;
                   // textResult.setText("Ticket_number" + "\n" + common.ticketresult);
                    Validate.setText(result);
                    //resultfrmserver.setText(common.ticketresult + common.userID + common.securityID);

                    Validate();

                 /*   if (common.winnerfalse == "false")
                    {
                        resultfrmserver.setText("This is not a winning Ticket");
                    }*/


                }
            }, ScanEngine.TriggerMode.Key, true);

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Validate.getText().clear();
                    resultfrmserver.setText("");



                }
            });
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
                URL url = new URL("http://10.7.65.112/SGVSWebAPIs/API/Ticket/TicketValidation");
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

                        Winner = vali.getString("Winner");
                        winningamount = vali.getString("WinningAmount");
                        Cashed = vali.getString("Cashed");
                        resultfrmserver.setText("Winner :" + Winner +"\n\n"  + "Winning Amount:" +  winningamount + "\n\n"  + "Cashed :" +Cashed);
                    }
                    else
                    {
                        Winner = vali.getString("Winner");
                        winningamount = vali.getString("WinningAmount");
                        Cashed = vali.getString("Cashed");

                        resultfrmserver.setText("Winner :" + Winner +"\n\n"+ "Winning Amount:" +  winningamount + "\n\n" + "Cashed :" +Cashed);

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
   /* public long GetSeceretCode(long SeceretCode)
    {
       *//* Calendar now = null;
        now = Calendar.getInstance();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        String strDate= formatter.format(date);

        String day = (strDate.substring(0, 2));
        String month = (strDate.substring(3, 5));
        String year = (strDate.substring(6, 8));
        String hour = (strDate.substring(9, 11));
        String min = (strDate.substring(12, 14));
        String sec = (strDate.substring(15, 17));

        String SecCode = (day + month + year+ hour+ min+ sec);

        long secco = Long.parseLong(SecCode);

        return  secco;*//*

    }*/
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

    public void Validate()

    {
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
        Ticket_no = result;

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate= formatter.format(date);

        validationdate = strDate;
        tc = transcheck.TransactionCheck;
        String SecCode = (day + month + year+ hour+ min+ sec);

        long secco = Long.parseLong(SecCode);

        ShowPayNowLater = 0;
        ShownPlayerTaxDetails = 0;
        Resident = 0;
        FirstName = null;
        LastName = null;
        NationalID = null;
        PassportNo = null;

        JSONObject postData = new JSONObject();
        try {
            postData.put("SecurityID", securityID);
            postData.put("UserID", userID);
            postData.put("TicketBarcode", Ticket_no);
            postData.put("ValidationDate", validationdate);
            postData.put("TC", tc);
            postData.put("SC", secco);
            postData.put("ShowPayNowLater", ShowPayNowLater);
            postData.put("ShownPlayerTaxDetails", ShownPlayerTaxDetails);
            postData.put("Resident", Resident);
            postData.put("FirstName", FirstName);
            postData.put("LastName", LastName);
            postData.put("NationalID", NationalID);
            postData.put("PassportNo", PassportNo);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (postData.length() > 0) {

            new SendJsonDataToServer().execute(String.valueOf(postData));

        }
    }


}
