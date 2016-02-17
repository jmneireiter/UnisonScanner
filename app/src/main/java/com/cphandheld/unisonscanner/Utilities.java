package com.cphandheld.unisonscanner;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Supernova on 2/16/2016.
 */
public class Utilities {

    // static values
    public static final String AppURL = "http://unison-alt.cphandheld.com/";
    public static final String LoginURL = "api/authorize/";

    public static User currentUser = new User();

    public static String StreamToString(InputStreamReader isr) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( isr);
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        bufferedReader.close();
        return result;

    }

}