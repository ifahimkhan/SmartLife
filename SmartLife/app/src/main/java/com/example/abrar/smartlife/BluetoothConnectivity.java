package com.example.abrar.smartlife;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class BluetoothConnectivity extends Activity {

    private static final String TAG = "BluetoothConnectivity";
    private static final int REQUEST_ENABLE_BT = 1;
    
    private Button onBtn;
    private Button offBtn;
    private Button listBtn;
    private Button findBtn;
    private TextView text;
    private BluetoothAdapter myBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView myListView;
    private ArrayAdapter<String> BTArrayAdapter;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connectivity);


        // take an instance of BluetoothAdapter - Bluetooth radio

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(myBluetoothAdapter == null) {

            onBtn.setEnabled(false);
            offBtn.setEnabled(false);
            listBtn.setEnabled(false);
            findBtn.setEnabled(false);

            text.setText("Status : not supported");

            Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth", Toast.LENGTH_LONG).show();

        }

        else {

            text = (TextView) findViewById(R.id.text);
            onBtn = (Button)findViewById(R.id.turnOn);

            onBtn.setOnClickListener(new OnClickListener() {

                @Override

                public void onClick(View v) {

                    // TODO Auto-generated method stub
                    on(v);
                }

            });


            offBtn = (Button)findViewById(R.id.turnOff);

            offBtn.setOnClickListener(new OnClickListener() {

                @Override

                public void onClick(View v) {

                    // TODO Auto-generated method stub
                    off(v);
                }

            });

            listBtn = (Button)findViewById(R.id.paired);

            listBtn.setOnClickListener(new OnClickListener() {

                @Override

                public void onClick(View v) {

                    // TODO Auto-generated method stub
                    list(v);

                }

            });

            findBtn = (Button)findViewById(R.id.scan);

            findBtn.setOnClickListener(new OnClickListener() {

                @Override

                public void onClick(View v) {

                    // TODO Auto-generated method stub
                    find(v);
                }

            });

            myListView = (ListView)findViewById(R.id.listView1);

            // create the arrayAdapter that contains the BTDevices, and set it to the ListView

            BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            myListView.setAdapter(BTArrayAdapter);
            myListView.setOnItemClickListener(mDeviceClickListener);
            if(myBluetoothAdapter.isEnabled()){
                text.setText("Status : Enabled");
            }
            if(!myBluetoothAdapter.isEnabled()){
                text.setText("Status : Disabled");
            }
        }

    }
    public void list(View view){

        // get paired devices

        pairedDevices = myBluetoothAdapter.getBondedDevices();

        // put it's one to the adapter

        for(BluetoothDevice device : pairedDevices)

            BTArrayAdapter.add(device.getName()+ "\n" + device.getAddress());

        if(myBluetoothAdapter.isEnabled())
            Toast.makeText(getApplicationContext(),"Showing Paired Devices", Toast.LENGTH_LONG).show();

        else
            Toast.makeText(getApplicationContext(),"Bluetooth must be Turned On", Toast.LENGTH_LONG).show();

    }

    public void find(View view) {
        if(myBluetoothAdapter.isEnabled()) {

            if (myBluetoothAdapter.isDiscovering()) {

                // the button is pressed when it discovers, so cancel the discovery

                myBluetoothAdapter.cancelDiscovery();

            } else {

                BTArrayAdapter.clear();

                myBluetoothAdapter.startDiscovery();


                registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Bluetooth must be Turned On", Toast.LENGTH_LONG).show();

        }

    }
    public void on(View view){

        if (!myBluetoothAdapter.isEnabled()) {

            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);

        }

        else{

            Toast.makeText(getApplicationContext(),"Bluetooth is already on", Toast.LENGTH_LONG).show();

        }

    }
/*
    @Override
    public void onResume()
    {
        super.onResume();
        //***************




        // Initialize array adapter for paired devices
        BTArrayAdapter = new ArrayAdapter<String>(this, R.layout.devicename);

        // Find and set up the ListView for paired devices
       ListView myListView = (ListView) findViewById(R.id.listView1);

        myListView.setOnItemClickListener(mDeviceClickListener);

        // Get the local Bluetooth adapter
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices and append to 'pairedDevices'
        Set<BluetoothDevice> pairedDevice = myBluetoothAdapter.getBondedDevices();

        // Add previosuly paired devices to the array
        if (pairedDevice.size() > 0) {
            //findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
            for (BluetoothDevice device : pairedDevice) {
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            BTArrayAdapter.add(noDevices);
        }
    } */

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {


            // Get the device MAC address, which is the last 17 chars in the View
                try{
              text.setText("Status : Connecting...");
              String info = ((TextView) v).getText().toString();
              String address = info.substring(info.length() - 17);

              // Make an intent to start next activity while taking an extra which is the MAC address.
              Intent i = new Intent(BluetoothConnectivity.this, MainActivity.class);
            i.putExtra(EXTRA_DEVICE_ADDRESS, address);
              startActivity(i);

        }catch(Exception e){
                    Intent i=new Intent(BluetoothConnectivity.this,BluetoothConnectivity.class);
                    startActivity(i);
                }

        }
    };




    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub

        if(requestCode == REQUEST_ENABLE_BT){

            if(myBluetoothAdapter.isEnabled()) {

                text.setText("Status : Enabled");

            } else {

                text.setText("Status : Disabled");

            }

        }

    }



    final BroadcastReceiver bReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // When discovery finds a device

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                // Get the BluetoothDevice object from the Intent

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // add the name and the MAC address of the object to the arrayAdapter

                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

                BTArrayAdapter.notifyDataSetChanged();

            }

        }

    };


    public void off(View view){

        if(myBluetoothAdapter.isEnabled()) {
            myBluetoothAdapter.disable();
            text.setText("Status : Disconnected");

            Toast.makeText(getApplicationContext(), "Bluetooth turned off", Toast.LENGTH_LONG).show();
        }
        else{

            Toast.makeText(getApplicationContext(), "Bluetooth is already off", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        createDialog();

    }

    private void createDialog()
    {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you want to Exit ?");
        alert.setCancelable(true);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BluetoothConnectivity.super.finish();
                myBluetoothAdapter.disable();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.create().show();
    }

    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(Process.myPid());
        super.onDestroy();
    }

}