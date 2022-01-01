package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateActivity extends AppCompatActivity {

    UpdateActivity activity;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This is executed when loading the activity, that is, when launching the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        this.activity = this;
        mail = getIntent().getStringExtra("sender");

        ProgressBar pBar = findViewById(R.id.updateAccountProgressBar);
        pBar.setVisibility(View.INVISIBLE);
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
                    TextView title = findViewById(R.id.updateAccountTitleBox);
                    TextView text1 = findViewById(R.id.updateAccountText1);
                    TextView text2 = findViewById(R.id.updateAccountText2);
                    EditText passwordIn = findViewById(R.id.updateAccountCurrentPassword);
                    TextView text3 = findViewById(R.id.updateAccountText3);
                    EditText nameIn = findViewById(R.id.updateAccountNewNameIn);
                    EditText passwordIn2 = findViewById(R.id.updateAccountNewPassword1);
                    EditText passwordIn3 = findViewById(R.id.updateAccountNewPassword2);
                    Button button = findViewById(R.id.updateAccountBtn);
                    Button backBtn = findViewById(R.id.updateAccountBackBtn);

                    ProgressBar pBar = findViewById(R.id.updateAccountProgressBar);

                    String password = passwordIn.getText().toString();
                    String newName = nameIn.getText().toString();
                    String newPassword1 = passwordIn2.getText().toString();
                    String newPassword2 = passwordIn3.getText().toString();

                    if(newPassword1.equals(newPassword2)) {

                        handler.post(() -> {
                            //Change visibilities. Only progress bar must be seen
                            title.setVisibility(View.INVISIBLE);
                            text1.setVisibility(View.INVISIBLE);
                            text2.setVisibility(View.INVISIBLE);
                            passwordIn.setVisibility(View.INVISIBLE);
                            text3.setVisibility(View.INVISIBLE);
                            nameIn.setVisibility(View.INVISIBLE);
                            passwordIn2.setVisibility(View.INVISIBLE);
                            passwordIn3.setVisibility(View.INVISIBLE);
                            button.setVisibility(View.INVISIBLE);
                            backBtn.setVisibility(View.INVISIBLE);

                            pBar.setVisibility(View.VISIBLE);
                        });

                        //Query
                        String query = "http://10.0.2.2:9000/Android/updateAccount?mail=" + mail + "&password=" + password + "&newname=" + newName + "&newpassword=" + newPassword1;
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
                        if (result.equals("0")) {
                            handler.post(() -> {
                                //Render toast with result message
                                Toast.makeText(activity.getApplicationContext(), "Your information has been updated correctly.", Toast.LENGTH_LONG).show();

                                //Return to inbox activity
                                finish();

                                //Change visibilities. Progress bar must not be seen
                                title.setVisibility(View.VISIBLE);
                                text1.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);
                                passwordIn.setVisibility(View.VISIBLE);
                                text3.setVisibility(View.VISIBLE);
                                nameIn.setVisibility(View.VISIBLE);
                                passwordIn2.setVisibility(View.VISIBLE);
                                passwordIn3.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                backBtn.setVisibility(View.VISIBLE);

                                pBar.setVisibility(View.INVISIBLE);
                            });
                        }
                        //If answer is -1, there was an error due to credentials
                        else if (result.equals("-1")) {
                            handler.post(() -> {
                                //Render toast with error message
                                Toast.makeText(activity.getApplicationContext(), "Wrong credentials. Make sure the data has been introduced properly.", Toast.LENGTH_LONG).show();

                                //Change visibilities. Progress bar must not be seen
                                title.setVisibility(View.VISIBLE);
                                text1.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);
                                passwordIn.setVisibility(View.VISIBLE);
                                text3.setVisibility(View.VISIBLE);
                                nameIn.setVisibility(View.VISIBLE);
                                passwordIn2.setVisibility(View.VISIBLE);
                                passwordIn3.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                backBtn.setVisibility(View.VISIBLE);

                                pBar.setVisibility(View.INVISIBLE);
                            });
                        } else {
                            handler.post(() -> {
                                //Render toast with error message
                                Toast.makeText(activity.getApplicationContext(), "Unexpected error, make sure your data has been introduced properly.", Toast.LENGTH_LONG).show();

                                //Change visibilities. Progress bar must not be seen
                                title.setVisibility(View.VISIBLE);
                                text1.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);
                                passwordIn.setVisibility(View.VISIBLE);
                                text3.setVisibility(View.VISIBLE);
                                nameIn.setVisibility(View.VISIBLE);
                                passwordIn2.setVisibility(View.VISIBLE);
                                passwordIn3.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                backBtn.setVisibility(View.VISIBLE);

                                pBar.setVisibility(View.INVISIBLE);
                            });
                        }
                    }
                    else {
                        handler.post(() -> {
                            //Render toast with error message
                            Toast.makeText(activity.getApplicationContext(), "New passwords are different, make sure they are equal.", Toast.LENGTH_LONG).show();
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