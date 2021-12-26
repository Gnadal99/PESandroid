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

    //Object for this activity is stored here
    deleteActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This is executed when loading the activity, that is, when launching the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        this.activity = this;
    }

    //Query for deleting an account with credentials introduced by user
    public void delete(View view) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            final Handler handler = new Handler();
            public void run() {
                try {

                    //Obtain data
                    EditText mailIn = findViewById(R.id.deleteEditTextEmailAddress2);
                    String mail = mailIn.getText().toString();
                    EditText passwordIn = findViewById(R.id.deleteEditTextPassword4);
                    String password = passwordIn.getText().toString();
                    Button button = findViewById(R.id.deleteBtn);

                    handler.post(() -> {
                        //Change visibilities. Only progress bar must be seen (AÑADIR PROGRESS BAR)
                        mailIn.setVisibility(View.INVISIBLE);
                        passwordIn.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                    });

                    //Query
                    String query = "http://10.0.2.2:9000/Android/deleteAccount?mail=" + mail + "&password=" + password;
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    stream = conn.getInputStream();

                    //Response
                    BufferedReader reader;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    //Result processing
                    //If answer is 0, user has been removed correctly
                    if(result.equals("0")) {
                        handler.post(() -> {
                            //Render toast with result message
                            Toast.makeText(activity.getApplicationContext(), "Your account has been deleted.", Toast.LENGTH_LONG).show();

                            //Show login activity
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);

                            //Change visibilities. Progress bar must not be seen
                            mailIn.setVisibility(View.VISIBLE);
                            passwordIn.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                        });
                    }
                    //If answer is -1, there was an error due to credentials
                    else if (result.equals("-1")) {
                        handler.post(() -> {
                            //Render toast with error message
                            Toast.makeText(activity.getApplicationContext(), "Wrong credentials. Make sure the data has been introduced properly.", Toast.LENGTH_LONG).show();

                            //Change visibilities. Progress bar must not be seen
                            mailIn.setVisibility(View.VISIBLE);
                            passwordIn.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                        });
                    }
                    else {
                        handler.post(() -> {
                            //Render toast with error message
                            Toast.makeText(activity.getApplicationContext(), "Unexpected error.", Toast.LENGTH_LONG).show();

                            //Change visibilities. Progress bar must not be seen
                            mailIn.setVisibility(View.VISIBLE);
                            passwordIn.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //This is executed when return button is clicked
    public void back(View view) {
        finish();
    }
}
