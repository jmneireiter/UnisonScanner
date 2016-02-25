package com.cphandheld.unisonscanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.support.annotation.BinderThread;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BinActivity extends HeaderActivity
{
    TextView textVIN;
    ListView listBins;
    ArrayList bins;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin);
        setHeader(R.color.colorBinHeader, Utilities.currentUser.name, Utilities.currentContext.locationName, R.string.bin_header);

        textVIN = (TextView) findViewById(R.id.textVIN);
        textVIN.setText(Utilities.currentContext.vehicle.vin);

        listBins = (ListView) findViewById(R.id.listBins);
        listBins.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView textView = (TextView) view.findViewById(R.id.rowTextView);
                textView.setTextColor(getResources().getColor(R.color.colorBinTextSelected));
                view.setBackgroundColor(getResources().getColor(R.color.colorBinBgSelected));

                Bins bin = (Bins)bins.get(position);
                int binId = bin.binId;
                String name = bin.name;

                Intent intent = new Intent(BinActivity.this, PathActivity.class);
                Utilities.currentContext.binId = binId;
                Utilities.currentContext.binName = name;
                startActivityForResult(intent, 1);
            }
        });

        new loadBins().execute(Integer.toString(Utilities.currentContext.locationId));
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
        Intent i = new Intent(BinActivity.this, ScanActivity.class);
        i.putExtra("back", true);
        setResult(RESULT_OK, i);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                Boolean backPress = data.getBooleanExtra("back", false);

                if(backPress)
                {
                    listBins.clearChoices();

                    for (int i=0; i<listBins.getCount(); i++)
                    {
                        View view = listBins.getChildAt(i);
                        TextView textView = (TextView) view.findViewById(R.id.rowTextView);
                        textView.setTextColor(getResources().getColor(R.color.colorListTextUnselected));
                        view.setBackgroundColor(getResources().getColor(R.color.colorListBgUnselected));
                    }
                }
            }
        }
    }

    private class loadBins extends AsyncTask<String, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            //Toast.makeText(getApplicationContext(), "Loading bins...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            getBins(Integer.parseInt(params[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (bins != null && bins.size() > 0) {
                ArrayAdapter<Bins> adapter = new ArrayAdapter<Bins>(BinActivity.this, R.layout.generic_list, bins);
                listBins.setAdapter(adapter);
            }
        }

        private void getBins(int locationId) {
            HttpURLConnection connection;
            InputStreamReader isr;
            URL url;
            String result;
            JSONArray responseData;

            try {
                String address = Utilities.AppURL + Utilities.BinsURL + Integer.toString(locationId) + Utilities.AppURLSuffix;
                url = new URL(address);
                connection = (HttpURLConnection) url.openConnection();
                isr = new InputStreamReader(connection.getInputStream());

                if(connection.getResponseCode() == 200) {
                    result = Utilities.StreamToString(isr);
                    responseData = new JSONArray(result);

                    bins = new ArrayList(responseData.length());

                    for (int i = 0; i < responseData.length(); i++) {
                        Bins bin = new Bins();
                        JSONObject temp = responseData.getJSONObject(i);
                        bin.name = temp.getString("BinName");
                        bin.binId = temp.getInt("BinId");
                        bins.add(bin);
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.i("getBins()","error...");
            }
        }
    }

    private class Bins
    {
        private String name;
        private int binId;

        public Bins()
        {

        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
