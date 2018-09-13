package com.szcbit.SGFlexSys;

import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.View;
import org.json.JSONObject;
import org.json.JSONArray;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONStringer;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.*;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;


import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.net.URLEncoder;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.szcbit.SGFlexSys.Loginpage;
import com.szcbit.x1.Device;

public class Loginpage extends AppCompatActivity {


    EditText LoginUsername, Password;
    protected String hardwareSN = "N/A";
    protected Device devi = null;
    TextView result;
    Button Login;
    JSONArray user;
    JSONObject hay;
    String UserIDBarcode, DeviceSerial, AppVersion, Name, password, userid, deviceserial, appversion, mesaage, security, ID;
    int PIN, LoginMode, Language, pin, loginmode, lang;
    long tc, TC, SC, sc, secco, secID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        final ClsTransactionCheck transcheck = new ClsTransactionCheck();

        devi = new Device(this, new ResultReceiver(new Handler()) {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                // when the Device object is ready
                try {
                    // get those hardware related IDs
                    hardwareSN = devi.getHardwareSerial();
                }
                catch(Exception e)

                {
                    e.printStackTrace();
                    //   Toast.makeText(HardwareSerialActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

               // ((TextView)findViewById(R.id.textView)).setText(hardwareSN);
            }
        });


         LoginUsername = (EditText) findViewById(R.id.editText);
        Password = (EditText) findViewById(R.id.editText2);
        result = (TextView) findViewById(R.id.textView);
        UserIDBarcode = "123";
        PIN = 0;
        LoginMode = 1;
        DeviceSerial = hardwareSN;
        Language = 0;
        AppVersion = "1.0.0";

        Login = (Button) findViewById(R.id.button);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name = LoginUsername.getText().toString();
                password = Password.getText().toString();
                userid = UserIDBarcode;
                pin = PIN;
                loginmode = LoginMode;
                lang = Language;
                transcheck.GetTransactionCheck(hardwareSN);
                tc = transcheck.TransactionCheck;
                sc = transcheck.GetSeceretCode(secco);
                appversion = AppVersion;
                deviceserial = hardwareSN;

                JSONObject postData = new JSONObject();
                try {
                    postData.put("UserName", Name);
                    postData.put("Password", password);
                    postData.put("UserIDBarcode", userid);
                    postData.put("PIN", pin);
                    postData.put("LoginMode", loginmode);
                    postData.put("DeviceSerial", deviceserial);
                    postData.put("Language", lang);
                    postData.put("TC", tc);
                    postData.put("SC", sc);
                    postData.put("AppVersion", appversion);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if (postData.length() > 0) {
                 new SendJsonDataToServer().execute(String.valueOf(postData));

                }


            }

        });

    }
    class SendJsonDataToServer extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("http://10.7.65.112/SGVSWebAPIs/API/Accounts/Login");
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


//response data
                // Log.i(TAG,JsonResponse);
                try {
//send to post execute
                  JSONObject login = new JSONObject(new String(buffer));

                  common.message = login.getString("return_msg");

                    if(login.getString("return_code").equals("0"))
                    {
                        //String res = login.get("return_msg");

                       // result.setText(login.getString("return_msg"));

                        common.userID = Long.parseLong(login.getString("userID"));
                        common.securityID = Long.parseLong(login.getString("securityID"));
                        common.officeName = login.getString("officeName");
                        common.Priv = login.getString("privilege");
                        common.userName = login.getString("userName");
                        common.OfficeCode = login.getString("officeCode");



                        Intent i = new Intent(Loginpage.this, MainActivity.class);
                        startActivity(i);

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

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

          //  result.setText(common.message);


        }

    }






}



















