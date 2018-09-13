package com.szcbit.SGFlexSys;

import android.content.Context;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szcbit.x1.ScanEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * Sample Activity for Bar Code Symbology
 */
public class SymbologyActivity extends AppCompatActivity {

    protected ScanEngine scanengine;

    /*
     * Adapter for symbology list view
     */
    public class SymbologyListAdapter extends BaseAdapter {
        protected ArrayList<Integer> listID = new ArrayList<Integer>();
        protected ArrayList<String> listName = new ArrayList<String>();
        protected ArrayList<Boolean> listEnabled = new ArrayList<Boolean>();

        public SymbologyListAdapter(Map<ScanEngine.Symbology, Boolean> list) {
            for (Map.Entry<ScanEngine.Symbology, Boolean> entry : list.entrySet()) {
                listID.add(entry.getKey().getID());
                listName.add(entry.getKey().getName());
                listEnabled.add(entry.getValue());
            }

            for(int i = 0; i < listID.size() - 1; i ++) {
                for(int j = i + 1; j < listID.size(); j ++) {
                    if(listName.get(i).compareToIgnoreCase(listName.get(j)) > 0) {
                        int id = listID.get(i); listID.set(i, listID.get(j)); listID.set(j, id);
                        String name = listName.get(i); listName.set(i, listName.get(j)); listName.set(j, name);
                        Boolean enabled = listEnabled.get(i); listEnabled.set(i, listEnabled.get(j)); listEnabled.set(j, enabled);
                    }
                }
            }
        }

        @Override
        public int getCount() {
            return listID.size();
        }

        @Override
        public Object getItem(int i) {
            return listName.get(i);
        }

        @Override
        public long getItemId(int i) {
            return listID.get(i);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_symbology, null);

            ((CheckBox)view.findViewById(R.id.list_enabled)).setChecked(listEnabled.get(i));
            if(view.getTag() == null) {
                ((TextView) view.findViewById(R.id.list_name)).setText(listName.get(i));

                final int position = i;
                // when the check box of a certain symbology is clicked, enable or disable it
                ((CheckBox) view.findViewById(R.id.list_enabled)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (listEnabled.get(position) != b) {
                            HashMap<ScanEngine.Symbology, Boolean> syms = new HashMap<ScanEngine.Symbology, Boolean>();
                            syms.put(new ScanEngine.Symbology(listID.get(position)), b);
                            try {
                                scanengine.setSymbologies(syms);
                                listEnabled.set(position, b);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(SymbologyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                view.setTag(view);
            }

            return view;
        }
    }

    protected ListView list = null;
    protected Map<ScanEngine.Symbology, Boolean> symbologies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symbology);

        setTitle(R.string.button_symbologies);

        // create a ScanEngine object
        scanengine = new ScanEngine(this, new ResultReceiver(new Handler()) {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                // when the ScanEngine object is ready
                try {
                    // obtain all symbologies to fill the list view
                    symbologies = scanengine.getSymbologies();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SymbologyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                list = (ListView) findViewById(R.id.listSymbologies);
                list.setAdapter(new SymbologyListAdapter(symbologies));
            }
        });
    }
}
