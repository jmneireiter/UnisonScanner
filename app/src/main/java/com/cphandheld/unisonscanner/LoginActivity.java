package com.cphandheld.unisonscanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class LoginActivity extends AppCompatActivity {

    ImageView imageButton1;
    ImageView imageButton2;
    ImageView imageButton3;
    ImageView imageButton4;
    ImageView imageButton5;
    ImageView imageButton6;
    ImageView imageButton7;
    ImageView imageButton8;
    ImageView imageButton9;
    ImageView imageButton0;
    ImageView imageEntry1;
    ImageView imageEntry2;
    ImageView imageEntry3;
    ImageView imageEntry4;
    ImageView imageBack;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setClickEvents();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

/*    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                Boolean authenticated = data.getBooleanExtra("authenticated", false);
                if (!authenticated)
                    Logout();
            }
        }
    }*/

    protected void setClickEvents()
    {
        imageButton1 = (ImageView) findViewById(R.id.button1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntry((String) view.getTag());
            }
        });

        imageButton2 = (ImageView) findViewById(R.id.button2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntry((String) view.getTag());
            }
        });

        imageButton3 = (ImageView) findViewById(R.id.button3);
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntry((String) view.getTag());
            }
        });

        imageButton4 = (ImageView) findViewById(R.id.button4);
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntry((String) view.getTag());
            }
        });

        imageButton5 = (ImageView) findViewById(R.id.button5);
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntry((String) view.getTag());
            }
        });

        imageButton6 = (ImageView) findViewById(R.id.button6);
        imageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntry((String) view.getTag());
            }
        });

        imageButton7 = (ImageView) findViewById(R.id.button7);
        imageButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntry((String) view.getTag());
            }
        });

        imageButton8 = (ImageView) findViewById(R.id.button8);
        imageButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntry((String) view.getTag());
            }
        });

        imageButton9 = (ImageView) findViewById(R.id.button9);
        imageButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntry((String) view.getTag());
            }
        });

        imageButton0 = (ImageView) findViewById(R.id.button0);
        imageButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntry((String) view.getTag());
            }
        });

        imageBack = (ImageView) findViewById(R.id.buttonBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEntry((String) view.getTag());
            }
        });
    }
    protected void setEntry(String tag) {
        imageEntry1 = (ImageView) findViewById(R.id.entry1);
        imageEntry2 = (ImageView) findViewById(R.id.entry2);
        imageEntry3 = (ImageView) findViewById(R.id.entry3);
        imageEntry4 = (ImageView) findViewById(R.id.entry4);

        if (imageEntry1.getTag() == null) {
            imageEntry1.setTag(tag);
            imageEntry1.setImageResource(R.drawable.yes_pin);
        } else if (imageEntry2.getTag() == null) {
            imageEntry2.setTag(tag);
            imageEntry2.setImageResource(R.drawable.yes_pin);
        } else if (imageEntry3.getTag() == null) {
            imageEntry3.setTag(tag);
            imageEntry3.setImageResource(R.drawable.yes_pin);
        } else if (imageEntry4.getTag() == null) {
            imageEntry4.setTag(tag);
            imageEntry4.setImageResource(R.drawable.yes_pin);
            String pin = (String)imageEntry1.getTag() + (String)imageEntry2.getTag() + (String)imageEntry3.getTag() + tag;
            new LoginTask().execute("3", pin);
        }
    }

    protected void deleteEntry(String tag) {
        imageEntry1 = (ImageView) findViewById(R.id.entry1);
        imageEntry2 = (ImageView) findViewById(R.id.entry2);
        imageEntry3 = (ImageView) findViewById(R.id.entry3);
        imageEntry4 = (ImageView) findViewById(R.id.entry4);

        if (imageEntry4.getTag() != null) {
            imageEntry4.setTag(null);
            imageEntry4.setImageResource(R.drawable.no_pin);
        } else if (imageEntry3.getTag() != null) {
            imageEntry3.setTag(null);
            imageEntry3.setImageResource(R.drawable.no_pin);
        } else if (imageEntry2.getTag() != null) {
            imageEntry2.setTag(null);
            imageEntry2.setImageResource(R.drawable.no_pin);
        } else if (imageEntry1.getTag() != null) {
            imageEntry1.setTag(null);
            imageEntry1.setImageResource(R.drawable.no_pin);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.cphandheld.unisonscanner/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.cphandheld.unisonscanner/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            //Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            Utilities.currentUser = null;
            if (LoginPost(Integer.parseInt(params[0]), params[1])) {
                Intent i = new Intent(LoginActivity.this, LocationActivity.class);
                startActivity(i);
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (Utilities.currentUser != null) {
                //Toast.makeText(getApplicationContext(), Utilities.currentUser.name + " logged in", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid PIN", Toast.LENGTH_SHORT).show();
                imageEntry1.setImageResource(R.drawable.pin_x);
                imageEntry2.setImageResource(R.drawable.pin_x);
                imageEntry3.setImageResource(R.drawable.pin_x);
                imageEntry4.setImageResource(R.drawable.pin_x);

                final Vibrator vibe = (Vibrator) LoginActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(200);


                YoYo.with(Techniques.Shake)
                        .duration(1000)
                        .playOn(imageEntry1);

                YoYo.with(Techniques.Shake)
                        .duration(1000)
                        .playOn(imageEntry2);

                YoYo.with(Techniques.Shake)
                        .duration(1000)
                        .playOn(imageEntry3);

                YoYo.with(Techniques.Shake)
                        .duration(1000)
                        .playOn(imageEntry4);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageEntry1.setImageResource(R.drawable.no_pin);
                        imageEntry2.setImageResource(R.drawable.no_pin);
                        imageEntry3.setImageResource(R.drawable.no_pin);
                        imageEntry4.setImageResource(R.drawable.no_pin);
                        imageEntry1.setTag(null);
                        imageEntry2.setTag(null);
                        imageEntry3.setTag(null);
                        imageEntry4.setTag(null);
                    }
                }, 1500);
            }
        }

        private boolean LoginPost(int organizationId, String pin) {
            URL url;
            HttpURLConnection connection;
            OutputStreamWriter request;
            JSONObject responseData;
            JSONObject postData;
            InputStreamReader isr;
            String result;

            try {
                postData = new JSONObject();
                postData.accumulate("OrganizationId", organizationId);
                postData.accumulate("Pin", pin);

                url = new URL(Utilities.AppURL + Utilities.LoginURL);

                connection = (HttpURLConnection) url.openConnection();
                connection.setFixedLengthStreamingMode(postData.toString().length());
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setRequestMethod("POST");

                request = new OutputStreamWriter(connection.getOutputStream());
                request.write(postData.toString());
                request.flush();
                request.close();

                if (connection.getResponseCode() == 200) {
                    isr = new InputStreamReader(connection.getInputStream());
                    result = Utilities.StreamToString(isr);
                    responseData = new JSONObject(result);

                    Utilities.currentUser = new User();
                    Utilities.currentUser.organizationId = organizationId;
                    Utilities.currentUser.userId = responseData.getInt("UserId");
                    Utilities.currentUser.name = responseData.getString("Name");

                    Utilities.currentContext = new CurrentContext();
                    Utilities.currentContext.organizationId = organizationId;

                    return true;
                } else
                    return false;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public void Logout()
    {
        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
        imageEntry1.setImageResource(R.drawable.no_pin);
        imageEntry2.setImageResource(R.drawable.no_pin);
        imageEntry3.setImageResource(R.drawable.no_pin);
        imageEntry4.setImageResource(R.drawable.no_pin);
        imageEntry1.setTag(null);
        imageEntry2.setTag(null);
        imageEntry3.setTag(null);
        imageEntry4.setTag(null);
    }
}