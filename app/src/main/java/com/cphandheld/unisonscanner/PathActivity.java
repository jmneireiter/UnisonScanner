package com.cphandheld.unisonscanner;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Button;
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

public class PathActivity extends HeaderActivity
{
    TextView textVIN;
    TextView textBin;
    ListView listPaths;
    ArrayList paths;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        setHeader(R.color.colorPathHeader, Utilities.currentUser.name, Utilities.currentContext.locationName, R.string.path_header);

        textVIN = (TextView) findViewById(R.id.textVIN);
        textVIN.setText(Utilities.currentContext.vehicle.vin);

        textBin = (TextView) findViewById(R.id.textBin);
        textBin.setText(Utilities.currentContext.binName.toUpperCase());

        listPaths = (ListView) findViewById(R.id.listPaths);
        listPaths.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView textView = (TextView) view.findViewById(R.id.rowTextView);
                textView.setTextColor(getResources().getColor(R.color.colorPathTextSelected));
                view.setBackgroundColor(getResources().getColor(R.color.colorPathBgSelected));

                Path path = (Path) paths.get(position);
                int pathId = path.pathId;
                String name = path.name;
                boolean startPath = path.startPath;

                Intent intent = new Intent(PathActivity.this, NotesActivity.class);
                Utilities.currentContext.pathId = pathId;
                Utilities.currentContext.pathName = name;
                Utilities.currentContext.startPath = startPath;
                startActivityForResult(intent, 1);
            }
        });

        new loadPaths().execute(Integer.toString(Utilities.currentContext.locationId));
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
        Intent i = new Intent(PathActivity.this, BinActivity.class);
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
                    listPaths.clearChoices();

                    for (int i=0; i<listPaths.getCount(); i++)
                    {
                        View view = listPaths.getChildAt(i);
                        TextView textView = (TextView) view.findViewById(R.id.rowTextView);
                        textView.setTextColor(getResources().getColor(R.color.colorListTextUnselected));
                        view.setBackgroundColor(getResources().getColor(R.color.colorListBgUnselected));
                    }
                }
            }
        }
    }

    private class loadPaths extends AsyncTask<String, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            //Toast.makeText(getApplicationContext(), "Loading bins...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            getPaths(Integer.parseInt(params[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (paths != null && paths.size() > 0) {
                ArrayAdapter<Path> adapter = new ArrayAdapter<Path>(PathActivity.this, R.layout.generic_list, paths);
                listPaths.setAdapter(adapter);
            }
        }

        private void getPaths(int locationId) {
            HttpURLConnection connection;
            InputStreamReader isr;
            URL url;
            String result;
            JSONArray responseData;

            try {
                String address = Utilities.AppURL + Utilities.PathURL + Integer.toString(locationId) + Utilities.AppURLSuffix;
                url = new URL(address);
                connection = (HttpURLConnection) url.openConnection();
                isr = new InputStreamReader(connection.getInputStream());

                if(connection.getResponseCode() == 200) {
                    result = Utilities.StreamToString(isr);
                    responseData = new JSONArray(result);

                    paths = new ArrayList(responseData.length());

                    for (int i = 0; i < responseData.length(); i++) {
                        Path path = new Path();
                        JSONObject temp = responseData.getJSONObject(i);
                        path.name = temp.getString("PathName");
                        path.pathId = temp.getInt("PathId");
                        path.startPath = true;
                        paths.add(path);
                    }

                    // add item for Custom Path (-1)
                    Path p = new Path();
                    p.name = getResources().getString(R.string.custom_path_item);
                    p.pathId = -1;
                    p.startPath = true;
                    paths.add(p);

                    // add item for No Path (-1)
                    p = new Path();
                    p.name = getResources().getString(R.string.no_path_item);
                    p.pathId = -1;
                    p.startPath = false;
                    paths.add(p);
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.i("getPaths()", "error...");
            }
        }
    }

    private class Path
    {
        private String name;
        private int pathId;
        private boolean startPath;

        public Path()
        {

        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
