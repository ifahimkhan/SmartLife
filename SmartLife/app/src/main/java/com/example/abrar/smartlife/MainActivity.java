package com.example.abrar.smartlife;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;



public class MainActivity extends Activity {

    Button b1,b2,b3,b4,b5,b6;
    private BluetoothAdapter bt=null;
    private ToggleButton tb;
    private ImageView img1,img2;
   // Switch s1,s2,s3,s4,s5,s6;
    RadioButton r1,r2,r3,r4,r5,r6;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;
    public static final int MESSAGE_WRITE = 1;
    public static final int MESSAGE_READ = 2;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;
   private final  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WRITE:
                    //Do something when writing
                    break;
                case MESSAGE_READ:
                    //Get the bytes from the msg.obj
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                   // String readMessage = new String(buffer, 0, bytes);
               //     t12.setText(readMessage);

                    if (readMessage.equals("1")) {
                        r1.setVisibility(View.VISIBLE);
                        r1.setChecked(true);
                        img1.setImageResource(R.drawable.pic_bulbon);
                    }
                    else if (readMessage.equals("2")) {
                    r1.setVisibility(View.INVISIBLE);
                        img1.setImageResource(R.drawable.pic_bulboff);

                }

                    else if (readMessage.equals("3")) {
                        r2.setVisibility(View.VISIBLE);
                        r2.setChecked(true);
                        img2.setImageResource(R.drawable.pic_bulbon);
                    }else if (readMessage.equals("4")) {
                    r2.setVisibility(View.INVISIBLE);
                        img2.setImageResource(R.drawable.pic_bulboff);
                }
                    else if (readMessage.equals("5")) {
                        r3.setVisibility(View.VISIBLE);
                        r3.setChecked(true);
                    }
                    else if (readMessage.equals("6")) {
                        r3.setVisibility(View.INVISIBLE);
                    }else {}


                    //  t14.setText("on");

                    break;
            }
        }
    };

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
                MainActivity.super.finish();
                bt.disable();
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
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  s1=(Switch) findViewById(R.id.switch1);
        s2=(Switch) findViewById(R.id.switch2);
        s3=(Switch) findViewById(R.id.switch3);
        s1.setClickable(false);
        s2.setClickable(false);
        s3.setClickable(false);
        s4=(Switch) findViewById(R.id.switch4);
        s4.setClickable(false);
        s5=(Switch) findViewById(R.id.switch5);
        s5.setClickable(false);
        s6=(Switch) findViewById(R.id.switch6);
        s6.setClickable(false);
*/
        img1=(ImageView) findViewById(R.id.imageView2);
        img2=(ImageView) findViewById(R.id.imageView3);

        r1=(RadioButton) findViewById(R.id.radioButton);
        r2=(RadioButton) findViewById(R.id.radioButton2);
        r3=(RadioButton) findViewById(R.id.radioButton3);
        r4=(RadioButton) findViewById(R.id.radioButton4);
        r5=(RadioButton) findViewById(R.id.radioButton5);
        r6=(RadioButton) findViewById(R.id.radioButton6);
        r4.setClickable(false);
        r5.setClickable(false);
        r6.setClickable(false);r1.setClickable(false);
        bt = BluetoothAdapter.getDefaultAdapter();

        addListenerOnButtonClick();

        b1 = (Button) findViewById(R.id.on1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Light1 is on", Toast.LENGTH_SHORT).show();
                mConnectedThread.write("1");    // Send "1" via Bluetooth


            }
        });


        b2 = (Button) findViewById(R.id.off1);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getBaseContext(), "Light1 is off", Toast.LENGTH_SHORT).show();
                mConnectedThread.write("0");    // Send "0" via Bluetooth


            }
        });


        b3 = (Button) findViewById(R.id.on2);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Light2 is on", Toast.LENGTH_SHORT).show();
                mConnectedThread.write("3");


            }
        });


        b4 = (Button) findViewById(R.id.off2);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mConnectedThread.write("2");
                Toast.makeText(getBaseContext(), "Light2 is off", Toast.LENGTH_SHORT).show();


            }
        });


/*        s3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (s3.isChecked()) {
                    Toast.makeText(getBaseContext(), "Fan is on", Toast.LENGTH_SHORT).show();
                    mConnectedThread.write("5");    // Send "1" via Bluetooth
                } else {
                    Toast.makeText(getBaseContext(), "Fan is off", Toast.LENGTH_SHORT).show();
                    mConnectedThread.write("4");    // Send "1" via Bluetooth

                }
            }
        });
  */
        b5 = (Button) findViewById(R.id.on3);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Fan is on", Toast.LENGTH_SHORT).show();
                mConnectedThread.write("5");


            }
        });


        b6 = (Button) findViewById(R.id.off3);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Fan is off", Toast.LENGTH_SHORT).show();
                mConnectedThread.write("4");


            }
        });
    }
  /*  public void function_disable(View view){
Toast.makeText(getBaseContext(),"Function is not in used",Toast.LENGTH_SHORT).show();
    }
*/
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }  @Override
       public void onResume() {
        super.onResume();

        //Get MAC address from BluetoothConnectivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(BluetoothConnectivity.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = bt.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }
    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }


    private void addListenerOnButtonClick() {

        tb = (ToggleButton) findViewById(R.id.toggleButton);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!tb.isChecked()) {
                    //commands to switch off the all devices

                   mConnectedThread.write("0");
                   mConnectedThread.write("2");
                    mConnectedThread.write("4");

                   Toast.makeText(getBaseContext(), "ALL APPLIANCES OFF", Toast.LENGTH_SHORT).show();
                }
                if(tb.isChecked()) {
                    //commands to switch on the all devices
                    Toast.makeText(getBaseContext(), "ALL APPLIANCES ON", Toast.LENGTH_SHORT).show();
                    mConnectedThread.write("1");
                    mConnectedThread.write("3");
                    mConnectedThread.write("5");
                }


            }

        });
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                     // Send the obtained bytes to the UI Activity via handler

                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
