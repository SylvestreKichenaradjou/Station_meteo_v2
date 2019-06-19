package com.example.skichenaradjou.station_meteo_interface;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Main2Activity extends AppCompatActivity {

    TextView celsius;
    TextView humide;
    TextView direction;
    TextView vent;

    String address = null;

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ReadSignal rsignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent newint = getIntent();

        rsignal = new ReadSignal();
        new ConnectBT().execute();

        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS);


        celsius = findViewById(R.id.celsius);
        humide = findViewById(R.id.humidite);
        direction = findViewById(R.id.direction);
        vent = findViewById(R.id.vitesse_vent);

        final Intent monIntent3 = new Intent().setClass(this, Main3Activity.class);
        final Intent monIntent4 = new Intent().setClass(this, Main4Activity.class);
        final Intent monIntent5 = new Intent().setClass(this, Main5Activity.class);
        final Intent monIntent6 = new Intent().setClass(this, Main6Activity.class);

        celsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(monIntent3);
            }
        });

        humide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(monIntent4);
            }
        });

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(monIntent5);
            }
        });

        vent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(monIntent6);
            }
        });
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Main2Activity.this, "Connexion en cours", "Patientez s'il vous plait...");
        }


        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("La connexion a échoué");
                finish();
            } else {
                msg("Connecté !");
                isBtConnected = true;
                rsignal.execute();
            }

            progress.dismiss();
        }
    }
    private class ReadSignal extends AsyncTask<Void, Integer, Void> {

        String reconstitution;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reconstitution = new String();
        }

        @Override
        protected Void doInBackground(Void... devices) {

            int test;

            try {


                while (btSocket != null) {
                    try {
                        test = btSocket.getInputStream().read();
                        publishProgress(test);

                    } catch (IOException e) {
                        msg("Error");
                    }
                }

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... valeur) {
            String a = Character.toString((char) (int) valeur[0]);
            reconstitution = reconstitution.concat(a);
            Log.i("Main2Ativity", "Valeur : "+ reconstitution);

            if (reconstitution.endsWith("\n")) {
                System.out.println(reconstitution);
               // celsius.setText(reconstitution);
                Log.i("Main2Ativity", "Valeur : "+ reconstitution);
                //System.out.print("OKOK");
                reconstitution = ""; //Reset de reconst.
                if (reconstitution.startsWith("temp"));
            }
        }
    }
}
