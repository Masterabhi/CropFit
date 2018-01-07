package com.cropfit.cropfit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import java.util.logging.LogRecord;

public class Main3Activity extends AppCompatActivity {

    //////initial declerations
    BluetoothAdapter mBluetoothAdapter;
    private Spinner spinner;
    private Button favCrop;
    public static UUID id ;
    public static String address;
     private ConnectedThread mConnectedThread;
    Handler bluetoothIn;



    public final int handlerState = 0;                        //used to identify handler message
    public BluetoothAdapter btAdapter = null;
    public BluetoothSocket btSocket = null;
    public StringBuilder recDataString = new StringBuilder();
    ////////
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;
    private TextView tempR,humR,solmR,preR,PhR;
    private int sensors = 0;
    boolean found = false;
    ////////initial declerations ends

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //Spinner start
        spinner = (Spinner) findViewById(R.id.spinner);
        favCrop = (Button) findViewById(R.id.fav_crop);
        Resources res = getResources();
        String[] districtList = res.getStringArray(R.array.district);
        List<String> spinnerList = new ArrayList<>(Arrays.asList(districtList));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_single_item, spinnerList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        spinner.setAdapter(spinnerArrayAdapter);
        String text = spinner.getSelectedItem().toString();
        ///////////end spinner


        ////////views declerations starts
        tempR = (TextView) findViewById(R.id.temp_val);
        humR = (TextView) findViewById(R.id.hum_val);
        solmR = (TextView) findViewById(R.id.Mos_val);
        preR = (TextView) findViewById(R.id.pre_val);
        PhR = (TextView) findViewById(R.id.ph_val);
        Button Connect = (Button) findViewById(R.id.Conn);
        ///////views decleartions ends
        //Bluetooth activity start
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if (mBluetoothAdapter == null) {
            Toast.makeText(Main3Activity.this, "Device Don't Support Bluetooth", Toast.LENGTH_LONG).show();

        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 574);
        }


        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        String deviceName;
        String deviceMacAddress;
        BluetoothDevice devic = null;
        if (pairedDevices.size() > 0) {

            for (BluetoothDevice device : pairedDevices) {
                deviceName = device.getName();
                deviceMacAddress = device.getAddress();
                devic = device;
                //Log.d("MyTag",deviceName+" "+deviceMacAddress);

                Toast.makeText(Main3Activity.this, deviceName + " " + deviceMacAddress, Toast.LENGTH_SHORT).show();
            }
        }

        BluetoothSocket tmp = null;
        try {


            id = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            Toast.makeText(Main3Activity.this, id.toString(), Toast.LENGTH_SHORT).show();
            tmp = devic.createRfcommSocketToServiceRecord(id);

            address = devic.getAddress();
            //String  ad=devic.getAddress().toString();
            //address=ad.substring(ad.length()-17);


            //address="00:21:13:01:23:E1";
            Toast.makeText(Main3Activity.this, address.toString(), Toast.LENGTH_SHORT).show();
            //String address="00805F9B34FB";


        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothSocket = tmp;
        //////////////////////////////////////////////////////////////////////////////////////////

        ///////receiving code


        //Toast.makeText(Main3Activity.this,"reached first half", Toast.LENGTH_LONG).show();
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                Toast.makeText(Main3Activity.this, "reached", Toast.LENGTH_LONG).show();
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);
                    // msg.arg1 = bytes from connect thread
                    //Log.d("msg","bhejege "+readMessage.charAt(readMessage.length()-1));
                    if ((readMessage.contains(";"))) {
                        found = true;
                        String finalMsg = recDataString.toString();
                        String[] values = finalMsg.split(",");
                        recDataString = new StringBuilder();
                        System.out.print(values[0]);
                        tempR.setText(values[0]);
                        humR.setText(values[1]);
                        solmR.setText(values[2]);
                        preR.setText(values[3]);
                        PhR.setText(values[4]);
                    }
                }
            }
        };
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException{

        return device.createRfcommSocketToServiceRecord(id);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent =getIntent();
        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        try{
            btSocket=createBluetoothSocket(device);
        } catch (IOException e) {
           // e.printStackTrace();
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        try{
            btSocket.connect();
        } catch (IOException e1) {
            //e.printStackTrace();
            try
            {
                btSocket.close();
            } catch (IOException e2) {
                e1.printStackTrace();
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }
    ////////////////////////
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
    ///////////////
    /*

        Button fav= (Button) findViewById(R.id.fav_crop);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main3Activity.this,Main4Activity.class);
                startActivity(intent);
            }
        });

        //Button Crop Ends

         */

       ////////
       //create new class for connect thread
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
               byte[] buffer = new byte[256];
               int bytes;

               // Keep looping to listen for received messages
               while (true) {
                   try {
                       bytes = mmInStream.read(buffer);            //read bytes from input buffer
                       String readMessage = new String(buffer, 0, bytes);
                       // Send the obtained bytes to the UI Activity via handler
                       bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                       //Log.d("yip",bluetoothIn.toString());

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
}








