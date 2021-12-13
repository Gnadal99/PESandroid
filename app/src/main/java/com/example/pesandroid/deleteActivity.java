package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class deleteActivity extends AppCompatActivity {

    deleteActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        this.activity = this;
    }

    public void delete(View view) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            Handler handler = new Handler();
            public void run() {
                try {
                    EditText mailIn = findViewById(R.id.deleteEditTextEmailAddress2);
                    String mail = mailIn.getText().toString();
                    EditText passwordIn = findViewById(R.id.deleteEditTextPassword4);
                    String password = passwordIn.getText().toString();

                    handler.post(new Runnable() {
                        public void run() {

                            EditText mailbox = findViewById(R.id.deleteEditTextEmailAddress2);
                            mailbox.setVisibility(View.INVISIBLE);
                            EditText pasbox = findViewById(R.id.deleteEditTextPassword4);
                            pasbox.setVisibility(View.INVISIBLE);
                            Button buto = findViewById(R.id.deleteBtn);
                            buto.setVisibility(View.INVISIBLE);

                            //TextView texto = findViewById(R.id.textview);
                            //texto.setText(result)
                        }
                    });

                    String query = String.format("http://10.0.2.2:9000/Android/deleteAccount?mail=" + mail + "&password=" + password);
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    stream = conn.getInputStream();

                    BufferedReader reader = null;

                    StringBuilder sb = new StringBuilder();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    if(result.equals("0")) {
                        //User removed correctly
                        handler.post(new Runnable() {
                            public void run() {

                                Toast.makeText(activity.getApplicationContext(), "Your account has been deleted.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(activity, MainActivity.class);
                                activity.startActivity(intent);

                                EditText mailbox = findViewById(R.id.deleteEditTextEmailAddress2);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.deleteEditTextPassword4);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.deleteBtn);
                                buto.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    else if (result.equals("-1")) {
                        //Retry
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Wrong credentials. Make sure the data has been introduced properly.", Toast.LENGTH_LONG).show();

                                EditText mailbox = findViewById(R.id.deleteEditTextEmailAddress2);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.deleteEditTextPassword4);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.deleteBtn);
                                buto.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    else {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Unexpected error.", Toast.LENGTH_LONG).show();

                                EditText mailbox = findViewById(R.id.deleteEditTextEmailAddress2);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.deleteEditTextPassword4);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.deleteBtn);
                                buto.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void goBack(View view) {
        finish();
    }
}
