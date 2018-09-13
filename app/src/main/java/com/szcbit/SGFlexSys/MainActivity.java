package com.szcbit.SGFlexSys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

//import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView result, officedet;

    Long securityID, tc, trch;
    final ClsTransactionCheck transcheck = new ClsTransactionCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("SG FlexSys");
        officedet = (TextView)findViewById(R.id.textView7);
        result = (TextView) findViewById(R.id.textView3);


        loadGameGroups();


        result.setText("Welcome to SG FlexSys,    " + common.userName +  "\n\n" + "Please select a choice from the side menu.");





        officedet.setText("Office Code: "   + common.OfficeCode + "\n\n" + "Office Name: " + common.officeName + common.GameGroupsString);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addMenuItemInNavMenuDrawer();




    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == 3) {

            Intent i = new Intent(MainActivity.this, Loginpage.class);
            startActivity(i);
            // Handle the camera action
        }
        else if (id == 2) {
            Intent i = new Intent(MainActivity.this, BarcodeActivity.class);
            startActivity(i);

        }

        else if (id == 5) {

            Intent i = new Intent(MainActivity.this, ActivatePacks.class);
            startActivity(i);

        } else if (id == 4) {

            Intent i = new Intent(MainActivity.this, ReceivePacks.class);
            startActivity(i);

        }

        else if (id == 18)

        {

            logout();
        }
        else if (id == 12)
        {
            Intent i = new Intent(MainActivity.this, PackReport.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }



    public void loadGameGroups()

    {
        long secID = common.securityID;

        Date datetime = new Date();
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        String strDate2= formatter2.format(datetime);

        String day = (strDate2.substring(0, 2));
        String month = (strDate2.substring(3, 5));
        String year = (strDate2.substring(6, 8));
        String hour = (strDate2.substring(9, 11));
        String min = (strDate2.substring(12, 14));
        String sec = (strDate2.substring(15, 17));

        String SecCode = (day + month + year+ hour+ min+ sec);

        trch = transcheck.TransactionCheck;

        long secco = Long.parseLong(SecCode);
        long userID = common.userID;

        JSONObject postData = new JSONObject();
        try {
            postData.put("SecurityID", secID);
            postData.put("UserID", userID);

            postData.put("TC", trch);
            postData.put("SC", secco);



        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (postData.length() > 0) {

         new GetGamefromserver().execute(String.valueOf(postData));

        }

    }

    class  GetGamefromserver extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            String JsonRes = null;
            String JDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("http://10.7.65.112/SGVSWebAPIs/API/Order/GetGameGroups");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JDATA);
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
                try {

                    JSONObject game = new JSONObject(new String(buffer));

                    common.GameGroupsString = game.getString("GameGroupsString");
                    common.returnmsg = game.getString("return_msg");
                    //common.retcode = Integer.parseInt(game.getString("return_code"));


                    return JsonRes;

                }
                catch (Exception e)

                {
                    e.printStackTrace();
                }

                //return null;

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
        protected void onPostExecute(String a) {

            super.onPostExecute(a);

            //result.setText(common.returnmsg);

            //officedet.setText(common.GameGroupsString + common.returnmsg);


        }



    }


    public void logout()
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

        tc = transcheck.TransactionCheck;

        String SecCode = (day + month + year+ hour+ min+ sec);

        long secco = Long.parseLong(SecCode);
        long userID = common.userID;


        JSONObject postData = new JSONObject();
        try {
            postData.put("SecurityID", securityID);
            postData.put("UserID", userID);

            postData.put("TC", tc);
            postData.put("SC", secco);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (postData.length() > 0) {

            new SendJsonDataToServer().execute(String.valueOf(postData));

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
                URL url = new URL("http://10.7.65.112/SGVSWebAPIs/API/Accounts/Logout");
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

                try {


                        Intent intent = new Intent(MainActivity.this, Loginpage.class);
                        startActivity(intent);
                        finish();

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

        }

    }

    private void addMenuItemInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        Menu submenu = menu.addSubMenu("SGFlexSys Mobile App");

        //String number = (common.Priv.substring((common.PRIV_VALIDATE_POS)) + 1 = "1");


        char activateticket = common.Priv.charAt(common.PRIV_ACTIVATE_TICKET_POS );

        if (activateticket == '1')

        {
            menu.add(0, 1, Menu.NONE,"Activate Ticket");
        }


        char singleticketvalidation = common.Priv.charAt(common.PRIV_VALIDATE_POS);

      if (singleticketvalidation  == '1')


      {
          menu.add(0, 2, Menu.NONE,"Single Ticket Validation");
      }

        char batchvalidate = common.Priv.charAt(common.PRIV_BATCHVALIDATE_POS);

        if (batchvalidate == '1')

        {
            menu.add(0, 3, Menu.NONE,"Batch Validate");
        }

        char receive = common.Priv.charAt(common.PRIV_RECEIVE_POS);

        if (receive == '1')

        {
            menu.add(0, 4, Menu.NONE,"Receive Packs");
        }

        char activatepacks = common.Priv.charAt(common.PRIV_ACTIVATE_PACKS_POS);

        if (activatepacks == '1')

        {
            menu.add(0, 5, Menu.NONE,"Activate Packs");
        }

        char returnpacks = common.Priv.charAt(common.PRIV_RETURN_PACKS_POS);

        if (returnpacks == '1')

        {
            menu.add(0, 6, Menu.NONE,"Return Packs");
        }

        char orders = common.Priv.charAt(common.PRIV_ORDER_POS);

        if (orders == '1')

        {
            menu.add(0, 7, Menu.NONE,"Orders");
        }
        char sellingpacks = common.Priv.charAt(common.PRIV_SELLING_PACKS_POS);

        if (sellingpacks == '1')

        {
            menu.add(0, 8, Menu.NONE,"Selling Packs");
        }

        char payment = common.Priv.charAt(common.PRIV_PAYMENT_POS);

        if (payment == '1')

        {
            menu.add(0, 9, Menu.NONE,"Payments");
        }

        char manageretailers = common.Priv.charAt(common.PRIV_MANAGE_RETAILERS_POS);

        if (manageretailers == '1')

        {
            menu.add(0, 10, Menu.NONE,"Manage Retailers");
        }

        char ticketreport = common.Priv.charAt(common.PRIV_TICKET_VAL_REPORTS_POS);

        if (ticketreport == '1')

        {
            menu.add(0, 11, Menu.NONE,"Ticket Validation Report");
        }

        char packreport = common.Priv.charAt(common.PRIV_PACK_REPORTS_POS);

        if (packreport == '1')

        {
            menu.add(0, 12, Menu.NONE,"Pack Report");
        }

        char inventoryreport = common.Priv.charAt(common.PRIV_INVENTORY_REPORT_POS);

        if (inventoryreport == '1')

        {
            menu.add(0, 13, Menu.NONE,"Inventory Report");
        }

        char accountreport = common.Priv.charAt(common.PRIV_ACCOUNT_REPORTS_POS);

        if (accountreport == '1')

        {
            menu.add(0, 14, Menu.NONE,"Account Report");
        }


        char reviewreceipts = common.Priv.charAt(common.PRIV_REVIEW_RECIEPTS_POS);

        if (reviewreceipts == '1')

        {
            menu.add(0, 15, Menu.NONE,"Review Receipts Report");
        }

        char transactionalreport = common.Priv.charAt(common.PRIV_TRANSACTION_REPORTS_POS);

        if (transactionalreport == '1')

        {
            menu.add(0, 16, Menu.NONE,"Transactional Report");
        }

        menu.add(0, 17, Menu.NONE,"App Setup");
        menu.add(0, 18, Menu.NONE,"Logout");

        navView.invalidate();
    }
}