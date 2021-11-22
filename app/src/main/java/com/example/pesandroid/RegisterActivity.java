package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    RegisterActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.activity = this;

        ProgressBar pgbar = findViewById(R.id.registerProgressBar);
        pgbar.setVisibility(View.INVISIBLE);
    }

    public void register(View view) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            Handler handler = new Handler();
            public void run() {
                try {
                    EditText mailIn = findViewById(R.id.registerEditTextEmailAddress);
                    String mail = mailIn.getText().toString();
                    EditText passwordIn = findViewById(R.id.registerEditTextPassword);
                    String password = passwordIn.getText().toString();
                    EditText nameIn = findViewById(R.id.registerEditTextFullName);
                    String fullName = nameIn.getText().toString();

                    handler.post(new Runnable() {
                        public void run() {
                            TextView title = findViewById(R.id.registerTitleText);
                            title.setVisibility(View.INVISIBLE);
                            ImageView imagen = findViewById(R.id.registerImageView);
                            imagen.setVisibility(View.INVISIBLE);
                            EditText mailbox = findViewById(R.id.registerEditTextEmailAddress);
                            mailbox.setVisibility(View.INVISIBLE);
                            EditText pasbox = findViewById(R.id.registerEditTextPassword);
                            pasbox.setVisibility(View.INVISIBLE);
                            Button buto = findViewById(R.id.registerBtn);
                            buto.setVisibility(View.INVISIBLE);
                            TextView textito2 = findViewById(R.id.registerSignUpLink);
                            textito2.setVisibility(View.INVISIBLE);

                            ProgressBar pgbar = findViewById(R.id.registerProgressBar);
                            pgbar.setVisibility(View.VISIBLE);
                        }
                    });

                    String query = String.format("http://10.0.2.2:9000/Android/register?mail=" + mail + "&password=" + password + "&fullName=" + fullName);
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
                        handler.post(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(activity, InboxActivity.class);
                                intent.putExtra("mail", mail);
                                intent.putExtra("password", password);
                                activity.startActivity(intent);

                                TextView title = findViewById(R.id.registerTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.registerImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.registerEditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.registerEditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.registerBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.registerSignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.registerProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else if (result.equals("-1")) {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Mail address does already exist. Try again.", Toast.LENGTH_LONG).show();

                                TextView title = findViewById(R.id.registerTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.registerImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.registerEditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.registerEditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.registerBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.registerSignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.registerProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Unexpected error.", Toast.LENGTH_LONG).show();

                                TextView title = findViewById(R.id.registerTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.registerImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.registerEditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.registerEditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.registerBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.registerSignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.registerProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
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