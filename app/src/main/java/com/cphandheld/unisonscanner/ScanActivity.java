package com.cphandheld.unisonscanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.ArrayList;

public class ScanActivity extends ActionBarActivity implements EMDKListener, StatusListener, DataListener
{
    TextView textUser;
    TextView textDealership;
    TextView textVIN;
    Button buttonLogout;

    private EMDKManager emdkManager = null;
    private BarcodeManager barcodeManager = null;
    private Scanner scanner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        textVIN = (TextView) findViewById(R.id.textVIN);

        textUser = (TextView) findViewById(R.id.textUser);
        textUser.setText("HELLO, " + Utilities.currentUser.name.toUpperCase());

        textDealership = (TextView) findViewById(R.id.textDealership);
        textDealership.setText(Utilities.currentContext.locationName.toUpperCase());

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ScanActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        EMDKResults results = EMDKManager.getEMDKManager(
                getApplicationContext(), this);
        // Check the return status of getEMDKManager and update the status Text
        // View accordingly
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            //statusTextView.setText("EMDKManager Request Failed");
        }
    }

    public void onBackPressed() {
        Intent i = new Intent(ScanActivity.this, LocationActivity.class);
        i.putExtra("back", true);
        setResult(RESULT_OK, i);
        finish();
    }

    private void initializeScanner() throws ScannerException
    {

        if (scanner == null) {

            // Get the Barcode Manager object
            barcodeManager = (BarcodeManager) this.emdkManager
                    .getInstance(EMDKManager.FEATURE_TYPE.BARCODE);

            // Get default scanner defined on the device
            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);

            // Add data and status listeners
            scanner.addDataListener(this);
            scanner.addStatusListener(this);

            // Hard trigger. When this mode is set, the user has to manually
            // press the trigger on the device after issuing the read call.
            scanner.triggerType = Scanner.TriggerType.HARD;

            // Enable the scanner
            scanner.enable();

            // Starts an asynchronous Scan. The method will not turn ON the
            // scanner. It will, however, put the scanner in a state in which
            // the scanner can be turned ON either by pressing a hardware
            // trigger or can be turned ON automatically.
            scanner.read();
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        this.emdkManager = emdkManager;

        try {
            // Call this method to enable Scanner and its listeners
            initializeScanner();
        } catch (ScannerException e) {
            e.printStackTrace();
        }

        // Toast to indicate that the user can now start scanning
        Toast.makeText(ScanActivity.this,
                "Press Hard Scan Button to start scanning...",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClosed() {
        if (this.emdkManager != null) {

            this.emdkManager.release();
            this.emdkManager = null;
        }
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
// Use the scanned data, process it on background thread using AsyncTask
        // and update the UI thread with the scanned results
        new AsyncDataUpdate().execute(scanDataCollection);
    }

    @Override
    public void onStatus(StatusData statusData) {
// process the scan status event on the background thread using
        // AsyncTask and update the UI thread with current scanner state
        new AsyncStatusUpdate().execute(statusData);
    }

    // Update the scan data on UI
    int dataLength = 0;

    // AsyncTask that configures the scanned data on background
    // thread and updated the result on UI thread with scanned data and type of
    // label
    private class AsyncDataUpdate extends
            AsyncTask<ScanDataCollection, Void, String>
    {

        @Override
        protected String doInBackground(ScanDataCollection... params) {

            // Status string that contains both barcode data and type of barcode
            // that is being scanned
            String statusStr = "";

            try {

                // Starts an asynchronous Scan. The method will not turn ON the
                // scanner. It will, however, put the scanner in a state in
                // which
                // the scanner can be turned ON either by pressing a hardware
                // trigger or can be turned ON automatically.
                scanner.read();

                ScanDataCollection scanDataCollection = params[0];

                // The ScanDataCollection object gives scanning result and the
                // collection of ScanData. So check the data and its status
                if (scanDataCollection != null
                        && scanDataCollection.getResult() == ScannerResults.SUCCESS) {

                    ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection
                            .getScanData();

                    // Iterate through scanned data and prepare the statusStr
                    for (ScanDataCollection.ScanData data : scanData) {
                        // Get the scanned data
                        String barcodeDate = data.getData();
                        // Get the type of label being scanned
                        ScanDataCollection.LabelType labelType = data.getLabelType();
                        // Concatenate barcode data and label type
                        statusStr = barcodeDate + " " + labelType;
                    }
                }

            } catch (ScannerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Return result to populate on UI thread
            return statusStr;
        }

        @Override
        protected void onPostExecute(String result) {
            // Update the dataView EditText on UI thread with barcode data and
            // its label type
            if (dataLength++ > 17) {
                // Clear the cache after 50 scans
                //dataView.getText().clear();
                Toast.makeText(ScanActivity.this, "Scanned VIN is too long", Toast.LENGTH_SHORT).show();
                dataLength = 0;
            }
            textVIN.setText(result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    // AsyncTask that configures the current state of scanner on background
    // thread and updates the result on UI thread
    private class AsyncStatusUpdate extends AsyncTask<StatusData, Void, String> {

        @Override
        protected String doInBackground(StatusData... params) {
            String statusStr = "";
            // Get the current state of scanner in background
            StatusData statusData = params[0];
            StatusData.ScannerStates state = statusData.getState();
            // Different states of Scanner
            switch (state) {
                // Scanner is IDLE
                case IDLE:
                    statusStr = "The scanner enabled and its idle";
                    break;
                // Scanner is SCANNING
                case SCANNING:
                    statusStr = "Scanning..";
                    break;
                // Scanner is waiting for trigger press
                case WAITING:
                    statusStr = "Waiting for trigger press..";
                    break;
                // Scanner is not enabled
                case DISABLED:
                    statusStr = "Scanner is not enabled";
                    break;
                default:
                    break;
            }

            // Return result to populate on UI thread
            return statusStr;
        }

        @Override
        protected void onPostExecute(String result) {
            // Update the status text view on UI thread with current scanner
            // state
            //statusTextView.setText(result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (emdkManager != null) {

            // Clean up the objects created by EMDK manager
            emdkManager.release();
            emdkManager = null;
        }
    }

    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try {
            if (scanner != null) {
                // releases the scanner hardware resources for other application
                // to use. You must call this as soon as you're done with the
                // scanning.
                scanner.disable();
                scanner = null;
            }
        } catch (ScannerException e) {
            e.printStackTrace();
        }
    }
}
