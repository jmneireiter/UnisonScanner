package com.cphandheld.unisonscanner;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class NotesActivity extends HeaderActivity
{
    TextView textVIN;
    TextView textBin;
    TextView textNotesHeader;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setHeader(R.color.colorNotesHeader, Utilities.currentUser.name, Utilities.currentContext.locationName, R.string.notes_header);

        textVIN = (TextView) findViewById(R.id.textVIN);
        textVIN.setText(Utilities.currentContext.vehicle.vin);

        textBin = (TextView) findViewById(R.id.textBin);
        textBin.setText(Utilities.currentContext.binName.toUpperCase());

        textNotesHeader = (TextView) findViewById(R.id.textNotesHeader);
        String notesHeader = getResources().getString(R.string.notes_text_header) + " " + Utilities.currentContext.pathName.toUpperCase();
        textNotesHeader.setText(notesHeader);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast toast = Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 75);
                toast.show();

                Intent i = new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed()
    {
        Intent i = new Intent(NotesActivity.this, PathActivity.class);
        i.putExtra("back", true);
        setResult(RESULT_OK, i);
        finish();
    }
}
