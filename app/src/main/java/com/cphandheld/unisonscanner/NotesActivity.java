package com.cphandheld.unisonscanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotesActivity extends HeaderActivity
{
    TextView textVIN;
    TextView textBin;
    TextView textNotesHeader;
    EditText textNotes;
    Button buttonCheckIn;

    String errorMessage;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setHeader(R.color.colorNotesHeader, Utilities.currentUser.name, Utilities.currentContext.locationName, -1, Utilities.currentContext.pathName.toUpperCase());

        textVIN = (TextView) findViewById(R.id.textVIN);
        textVIN.setText(Utilities.currentContext.vehicle.vin);

        textBin = (TextView) findViewById(R.id.textBin);
        textBin.setText(Utilities.currentContext.binName.toUpperCase());

        textNotesHeader = (TextView) findViewById(R.id.textNotesHeader);
        String notesHeader = getResources().getString(R.string.notes_text_header);
        textNotesHeader.setText(notesHeader);

        textNotes = (EditText) findViewById(R.id.textNotes);

        buttonCheckIn = (Button) findViewById(R.id.buttonCheckIn);
        buttonCheckIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Utilities.currentContext.notes = textNotes.getText().toString();
                new CheckIn().execute();
            }
        });
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

    private class CheckIn extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            //Toast.makeText(getApplicationContext(), "Checking vehicle in...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            errorMessage = "";

            if (CheckInPost())
            {
                    Intent i = new Intent(NotesActivity.this, ScanActivity.class);
                    startActivity(i);
                    return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (!errorMessage.equals(""))
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }

        private boolean CheckInPost() {
            URL url;
            HttpURLConnection connection;
            OutputStreamWriter request;
            InputStreamReader isr;
            JSONObject postData;
            JSONObject responseData;
            String result;

            try
            {
                CheckInPost cip = new CheckInPost();
                cip.locationId = Utilities.currentContext.locationId;
                cip.binId = Utilities.currentContext.binId;
                cip.pathId = Utilities.currentContext.pathId;
                cip.notes = Utilities.currentContext.notes;
                cip.userId = Utilities.currentUser.userId;
                cip.startPath = Utilities.currentContext.startPath;
                cip.vehicle = Utilities.currentContext.vehicle;

                Gson gson = new Gson();
                String json = gson.toJson(cip);

                url = new URL(Utilities.AppURL + Utilities.VehicleCheckInURL);

                connection = (HttpURLConnection) url.openConnection();
                connection.setFixedLengthStreamingMode(json.length());
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setRequestMethod("POST");

                request = new OutputStreamWriter(connection.getOutputStream());
                request.write(json);
                request.flush();
                request.close();

                if (connection.getResponseCode() == 200)
                    return true;
                else
                {
                    isr = new InputStreamReader(connection.getInputStream());
                    result = Utilities.StreamToString(isr);
                    responseData = new JSONObject(result);

                    errorMessage = responseData.getString("Message");
                    Log.i("vehicle check in error", errorMessage);
                    return false;
                }
            } catch (JSONException | IOException e)
            {
                e.printStackTrace();
            }

            return false;
        }
    }

    public class CheckInPost implements Serializable
    {
        int locationId;
        int binId;
        int pathId;
        String notes;
        int userId;
        boolean startPath;
        Vehicle vehicle;


        CheckInPost() {
        }


    }
}
