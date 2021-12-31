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

public class UpdateActivity extends AppCompatActivity {

    UpdateActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This is executed when loading the activity, that is, when launching the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        this.activity = this;

    }

    //Query for deleting an account with credentials introduced by user
    public void update(View view) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            final Handler handler = new Handler();
            public void run() {
                try {

                    //Obtain data
                    EditText mailIn = findViewById(R.id.editTextTextPersonName2);
                    String mail = mailIn.getText().toString();
                    EditText passwordIn = findViewById(R.id.editTextTextPassword);
                    String password = passwordIn.getText().toString();

                    EditText mailIn2 = findViewById(R.id.editTextTextPersonName);
                    String newname = mailIn2.getText().toString();
                    EditText passwordIn2 = findViewById(R.id.editTextTextPassword2);
                    String newpassword = passwordIn2.getText().toString();

                    Button button = findViewById(R.id.button3);
                    System.out.println(mail);
                    System.out.println(password);
                    System.out.println(newname);
                    System.out.println(newpassword);

                    handler.post(() -> {
                        //Change visibilities. Only progress bar must be seen (AÑADIR PROGRESS BAR)
                        mailIn.setVisibility(View.INVISIBLE);
                        passwordIn.setVisibility(View.INVISIBLE);
                        mailIn2.setVisibility(View.INVISIBLE);
                        passwordIn2.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                    });

                    //Query
                    String query = "http://10.0.2.2:9000/Android/updateAccount?mail=" + mail + "&password=" + password + "&newname=" + newname+ "&newpassword=" + newpassword;
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
                            Toast.makeText(activity.getApplicationContext(), "Your information has been updated correctly.", Toast.LENGTH_LONG).show();

                            //Show login activity
                            finish();

                            //Change visibilities. Progress bar must not be seen
                            mailIn.setVisibility(View.VISIBLE);
                            passwordIn.setVisibility(View.VISIBLE);
                            mailIn2.setVisibility(View.VISIBLE);
                            passwordIn2.setVisibility(View.VISIBLE);
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
                            mailIn2.setVisibility(View.VISIBLE);
                            passwordIn2.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                        });
                    }
                    else {
                        handler.post(() -> {
                            //Render toast with error message
                            Toast.makeText(activity.getApplicationContext(), "Unexpected error, make sure your data has been introduuced properly.", Toast.LENGTH_LONG).show();

                            //Change visibilities. Progress bar must not be seen
                            mailIn.setVisibility(View.VISIBLE);
                            passwordIn.setVisibility(View.VISIBLE);
                            mailIn2.setVisibility(View.VISIBLE);
                            passwordIn2.setVisibility(View.VISIBLE);
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