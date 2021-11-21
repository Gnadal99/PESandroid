package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this;
    }

    public void login(View view) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            Handler handler = new Handler();
            public void run() {
                try {
                    //Obtain data
                    EditText mailIn = findViewById(R.id.editTextEmailAddress);
                    String mail = mailIn.getText().toString();
                    EditText passwordIn = findViewById(R.id.editTextPassword);
                    String password = passwordIn.getText().toString();

                    //Query
                    String query = String.format("http://10.0.2.2:9000/Android/login?mail=" + mail + "&password=" + password);
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
                    if(result.equals("0")) {
                        handler.post(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(activity, InboxActivity.class);
                                intent.putExtra("mail", mail);
                                intent.putExtra("password", password);
                                activity.startActivity(intent);
                            }
                        });
                    }
                    else if (result.equals("-1")) {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Mail address does not exist. Try again.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else if (result.equals("-2")) {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Wrong password. Try again.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Unexpected error.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void signUp(View view) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }
}