package com.szcbit.SGFlexSys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;



import com.szcbit.SGFlexSys.PackReport;

public class PackReport extends AppCompatActivity {

    private Spinner spinner;

    public String separated[];
   public String[] seperated2;
    public String[] seperated3;
    public String Details[];
    public String det[];
    public String str[];
    public String games[];

    public String sep[];
    public String sep2[];
    public String sep3[];

    List<String> list = new ArrayList<String>();




    public TextView mDisplayDate;
    public String Deatisl[];
   // private static final String[] paths = {"item 1", "item 2", "item 3"};

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        spinner = (Spinner)findViewById(R.id.spinner);

         //separated = common.GameGroupsString.split("~");

        String[] separated = common.GameGroupsString.split("~");

        for(int i = 0 ; i < separated.length; i++ )
        {
            sep = separated[i].split("!");

           sep2 = sep[0].split("\\^");

            list.add(sep2[0]);

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        mDisplayDate = (TextView) findViewById(R.id.textView10);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        PackReport.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;


                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };




    }

}
