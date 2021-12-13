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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pesandroid.models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends AppCompatActivity   {

    InboxActivity activity;
    AdapterInbox adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        this.activity = this;
        String mail = getIntent().getStringExtra("mail");

        RecyclerView recyclerView = findViewById(R.id.inboxRecyclerView);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(InboxActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        // Set the adapter
        adapter = new AdapterInbox();
        recyclerView.setAdapter(adapter);

        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            Handler handler = new Handler();
            public void run() {
                try {
                    //Query
                    String query = String.format("http://10.0.2.2:9000/Android/getInbox?mail=" + mail + "&inboxCode=main");
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    //Response
                    stream = conn.getInputStream();
                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    //Result processing
                    if (result.equals("-1")) {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Mail address does not exist. Try again.", Toast.LENGTH_LONG).show();

                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.loginImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.logineditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.loginBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito = findViewById(R.id.loginsignUpText);
                                textito.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.loginsignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else if (result.equals("-2")) {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Wrong password. Try again.", Toast.LENGTH_LONG).show();

                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.loginImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.logineditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.loginBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito = findViewById(R.id.loginsignUpText);
                                textito.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.loginsignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else {
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    JSONArray jArray = new JSONArray(result);
                                    List<Message> messageList = new ArrayList<>();
                                    for(int i = 0; i < jArray.length(); i++) {
                                        JSONObject json_data = jArray.getJSONObject(i);
                                        Message message = new Message(json_data.getString("title"), json_data.getString("body"));
                                        messageList.add(message);
                                    }
                                    activity.adapter.setData(messageList);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void deleteAccount(View view) {
        Intent intent = new Intent(activity, deleteActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finishAffinity();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void back(View view) {
        SharedPreferences preferences = getSharedPreferences("MySharedPref", 0);
        preferences.edit().remove("mail").apply();
        preferences.edit().remove("password").apply();
        finish();
    }
}