package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.pesandroid.models.Mail;
import com.example.pesandroid.models.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InboxActivity extends AppCompatActivity   {

    //Objects for this activity, the adapter for recyclerView and spinners are saved here
    InboxActivity activity;
    AdapterInbox adapter;
    Spinner inboxSelectSpinner;
    Spinner inboxOptionsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This is executed when loading the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        this.activity = this;

        //Get mail from previous activity
        String mail = getIntent().getStringExtra("mail");

        //Set the recyclerView
        RecyclerView recyclerView = findViewById(R.id.inboxRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(InboxActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        // Set the adapter for recyclerView
        adapter = new AdapterInbox();
        recyclerView.setAdapter(adapter);

        //By default, get main inbox
        getInbox(mail, "main");

        //Set spinner for choosing an inbox to see
        inboxSelectSpinner = findViewById(R.id.inboxSelectSpinner);
        inboxSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //This is executed when an option is clicked
                String text = inboxSelectSpinner.getSelectedItem().toString();
                getInbox(mail, text.toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        //Set spinner for other options
        inboxOptionsSpinner = findViewById(R.id.inboxOptionsSpinner);
        inboxOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //This is executed when an option is clicked
                String text = inboxOptionsSpinner.getSelectedItem().toString();

                //In function of the clicked option, do anything
                switch (text) {
                    case "New message":
                        Toast.makeText(activity.getApplicationContext(), "Oops... not implemented.", Toast.LENGTH_LONG).show();
                        break;
                    case "Log out":
                        //Delete credentials from shared preferences
                        SharedPreferences preferences = getSharedPreferences("MySharedPref", 0);
                        preferences.edit().remove("mail").apply();
                        preferences.edit().remove("password").apply();
                        finish();
                        break;
                    case "Update profile":
                        Toast.makeText(activity.getApplicationContext(), "Oops... not implemented", Toast.LENGTH_LONG).show();
                        break;
                    case "Delete account":
                        //Show activity to delete the account
                        Intent intent = new Intent(activity, deleteActivity.class);
                        activity.startActivity(intent);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //This is executed when user clicks any button of the phone system. If the button was "back", finish the app
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finishAffinity();
        }
        return super.onKeyDown(keyCode, event);
    }

    //Query for getting any inbox introduced as parameter
    public void getInbox(String mail, String inbox) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            final Handler handler = new Handler();
            public void run() {
                try {
                    //Query
                    String query = "http://10.0.2.2:9000/Android/getInbox?mail=" + mail + "&inboxCode=" + inbox;
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    //Response
                    stream = conn.getInputStream();
                    BufferedReader reader;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    //Result processing
                    //If answer is -1, there are no messages on that inbox
                    if (result.equals("-1")) {
                        handler.post(() -> {
                            //AÑADIR MENSAJE EN ACTIVITY SI NO HAY MAILS
                            Toast.makeText(activity.getApplicationContext(), "Empty inbox (not implemented)", Toast.LENGTH_LONG).show();
                        });
                    }
                    //In rest of cases, there are results
                    else {
                        handler.post(() -> {
                            try {
                                //Cast received JSON to a mails array
                                JSONArray jArray = new JSONArray(result);
                                List<Mail> mailList = new ArrayList<>();
                                for(int i = 0; i < jArray.length(); i++) {
                                    JSONArray json_array = jArray.getJSONArray(i);
                                    JSONObject json_data = json_array.getJSONObject(0);
                                    Message message = new Message(json_data.getString("title"), json_data.getString("body"));
                                    String mail1 = json_array.getString(1);
                                    Date date = null;
                                    try {
                                        date = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault()).parse(json_array.getString(2));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Mail email = new Mail(message, mail1, date);
                                    mailList.add(email);
                                }

                                //Send data to set to adapter
                                activity.adapter.setData(mailList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}