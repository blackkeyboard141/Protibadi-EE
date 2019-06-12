package com.example.rasik.protibadi_ee;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,LocationListener {

    Button btnDistress, btnAdd, btnExisting;
    LocationManager locationManager;
    String mprovider;
    double l1,l2;
    ImageView img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.img);

        //img.setImageResource(R.drawable.cropped);






        btnDistress = (Button) findViewById(R.id.btnDistress);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnExisting = (Button) findViewById(R.id.btnExistingNumbers);

        btnDistress.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnExisting.setOnClickListener(this);
       //



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 15000, 1, this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT).show();
        }





/// The Following are Added by Niaz 3/31/17
        textInfo = (TextView)findViewById(R.id.info);

        listViewPairedDevice = (ListView)findViewById(R.id.pairedlist);
        btnDis = (Button)findViewById(R.id.ds);
        inputPane = (LinearLayout)findViewById(R.id.inputpane);







        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent a=new Intent(MainActivity.this,MainActivity.class);
                startActivity(a);
            }
        });




        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            Toast.makeText(this,
                    "FEATURE_BLUETOOTH NOT support",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //using the well-known SPP UUID
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this,
                    "Bluetooth is not supported on this hardware platform",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        ///END





    }

    @Override
    public void onLocationChanged(Location location) {



        l1=location.getLongitude();
        l2=location.getLatitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }






    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.btnDistress){

int c=0;
            Log.e("outside dialog", "" + c);

            if (c==0) {


                String[]ph=  getphonenumber();
                for(int i=0;i<ph.length;i++) {
                    Log.e("Inside dialog", "Count " + c + "Number " + ph[i]);
                    count++;


                    SmsManager smsManager = SmsManager.getDefault();
                    if(l1==0.0)
                    {
                        smsManager.sendTextMessage(ph[i], null, "Please come and help me! I'm in this location 23.814861,90.425954", null, null);
                    }
                    else

                        smsManager.sendTextMessage(ph[i], null, "Please come and help me! I'm in this location " + l2+","+l1, null, null);
                }
                Toast.makeText(MainActivity.this, "Message sent !", Toast.LENGTH_SHORT).show();



            }
c++;


        }

        if(v.getId()== R.id.btnAdd){

           Intent intent = new Intent(this, Addnumber.class);
            startActivity(intent);

        }

        if(v.getId()== R.id.btnExistingNumbers){

            Intent intent = new Intent(this, Existing.class);
            startActivity(intent);

        }

    }

    /**
     * Created by Niaz on 31-Mar-17.
     * Connecting Bluetooth Module with Android
     * Sending Text
     */
    private static final int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter bluetoothAdapter;


    BluetoothDevice mdevice;
    int count=0;



    //String msg="Please come and help me! I'm in this location " + l1+","+l2;
    ///String num="01711520308";
    TextView textInfo  ;
    ListView listViewPairedDevice;
    LinearLayout inputPane;

    Button  btnDis;

    ArrayAdapter<BluetoothDevice> pairedDeviceAdapter;
    private UUID myUUID;
    private final String UUID_STRING_WELL_KNOWN_SPP =
            "00001101-0000-1000-8000-00805F9B34FB";

    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;




    //Turn ON BlueTooth if it is OFF
    @Override
    protected void onStart() {
        super.onStart();


        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
    }

    private void setup() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {

            for (BluetoothDevice device : pairedDevices) {

                mdevice=device;
            }

            myThreadConnectBTdevice = new ThreadConnectBTdevice(mdevice);
            myThreadConnectBTdevice.start();

        }




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(myThreadConnectBTdevice!=null){
            myThreadConnectBTdevice.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ENABLE_BT){
            if(resultCode == Activity.RESULT_OK){
                setup();
            }else{
                Toast.makeText(this,
                        "BlueTooth NOT enabled",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //Called in ThreadConnectBTdevice once connect successed
    //to start ThreadConnected
    private void startThreadConnected(BluetoothSocket socket){

        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }

    /*
    ThreadConnectBTdevice:
    Background Thread to handle BlueTooth connecting
    */
    private class ThreadConnectBTdevice extends Thread {

        private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;


        private ThreadConnectBTdevice(BluetoothDevice device) {
            bluetoothDevice = device;

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean success = false;
            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();

                final String eMessage = e.getMessage();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                    }
                });

                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            if(success){
                //connect successful
                final String msgconnected = "connect successful:\n"
                        + "BluetoothSocket: " + bluetoothSocket + "\n"
                        + "BluetoothDevice: " + bluetoothDevice;

                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {


                        listViewPairedDevice.setVisibility(View.GONE);
                        inputPane.setVisibility(View.VISIBLE);
                    }});

                startThreadConnected(bluetoothSocket);
            }else{
                //fail
            }
        }

        public void cancel() {

            Toast.makeText(getApplicationContext(),
                    "close bluetoothSocket",
                    Toast.LENGTH_LONG).show();

            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    /*
    ThreadConnected:
    Background Thread to handle Bluetooth data communication
    after connected
     */
    private class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = connectedInputStream.read(buffer);
                    String strReceived = new String(buffer, 0, bytes);
                    final String msgReceived = String.valueOf(bytes) +
                            " bytes received:\n"
                            + strReceived;



                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Log.e("outside dialog", "" + count);

                            if (count==0) {


                                String[]ph=  getphonenumber();
                                for(int i=0;i<ph.length;i++) {
                                    Log.e("Inside dialog", "Count " + count + "Number " + ph[i]);
                                    count++;


                                    SmsManager smsManager = SmsManager.getDefault();
                                    if(l1==0.0)
                                    {
                                        smsManager.sendTextMessage(ph[i], null, "Please come and help me! I'm in this location 23.814861,90.425954", null, null);
                                    }
                                    else

                                    smsManager.sendTextMessage(ph[i], null, "Please come and help me! I'm in this location " + l2+","+l1, null, null);
                                }
                                Toast.makeText(MainActivity.this, "Message sent !", Toast.LENGTH_SHORT).show();
                                myThreadConnected.cancel();






                            }

                        }});

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    final String msgConnectionLost = "Msg sent !:\n"
                            ;
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            // textStatus.setText(msgConnectionLost);
                        }});
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String[] getphonenumber(){
        Cursor c;
        final String SELECT_SQL = "SELECT * FROM persons";
        SQLiteDatabase db;
        db = openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);

        c = db.rawQuery(SELECT_SQL, null);

        c.moveToFirst();


        Toast.makeText(getApplicationContext()," "+c.getCount() , Toast.LENGTH_LONG).show();

         final String[] values= new String[c.getCount()];
        int i=0;

        values[0]= c.getString(1);
        i++;

        while(c.moveToNext()){
            values[i]= c.getString(1);
            i++;
        }

        return values;
    }


    // Other GPS function codes are here




}
