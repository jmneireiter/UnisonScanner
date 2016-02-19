package com.cphandheld.unisonscanner;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.StatusData;

public class ScanActivity extends ActionBarActivity
{

    TextView textUser;
    TextView textDealership;
    Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

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
    }

    public void onBackPressed() {
        Intent i = new Intent(ScanActivity.this, LocationActivity.class);
        i.putExtra("back", true);
        setResult(RESULT_OK, i);
        finish();
    }
}
