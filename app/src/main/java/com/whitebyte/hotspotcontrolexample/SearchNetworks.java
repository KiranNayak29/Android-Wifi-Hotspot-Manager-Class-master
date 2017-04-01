package com.whitebyte.hotspotcontrolexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.Arrays;
import com.whitebyte.hotspotclients.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchNetworks extends AppCompatActivity {
    WifiManager mainWifi;
    WifiReceiver receiverWifi;

    StringBuilder sb = new StringBuilder();
    ArrayList<Map<String, String>> connections=new ArrayList<Map<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_networks);


        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if(mainWifi.isWifiEnabled()==false)
        {
            mainWifi.setWifiEnabled(true);
        }


        doInback();
    }

    public void doInback()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                receiverWifi = new WifiReceiver();
                registerReceiver(receiverWifi, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mainWifi.startScan();
                doInback();
            }
        }, 1000);

    }

    public void refresh(View view)
    {
        mainWifi.startScan();
    }


    @Override
    protected void onPause()
    {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiReceiver extends BroadcastReceiver
    {
        public void onReceive(Context c, Intent intent)
        {


            ArrayList<Float> Signal_Strenth= new ArrayList<Float>();

            sb = new StringBuilder();
            List<ScanResult> wifiList;
            wifiList = mainWifi.getScanResults();
            for(int i = 0; i < wifiList.size(); i++)
            {

                connections.add(createwifientry("wifi",wifiList.get(i).SSID));
            }

            ListView lv = (ListView) findViewById(R.id.listView);
            SimpleAdapter simpleAdpt = new SimpleAdapter(getApplicationContext(), connections,android.R.layout.simple_list_item_1, new String[] {"wifi"}, new int[]{android.R.id.text1});
            lv.setAdapter(simpleAdpt);
        }
    }

    private HashMap<String, String> createwifientry(String key, String name) {
        HashMap<String, String> wifi = new HashMap<String, String>();
        wifi.put(key, name);

        return wifi;
    }

}
