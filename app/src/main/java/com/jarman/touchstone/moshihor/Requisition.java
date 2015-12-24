package com.jarman.touchstone.moshihor;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Requisition extends AppCompatActivity {
Spinner spinner;
    EditText amount;
    Button apply;
    String auth = "12345jhkdfgdfoem123";
    private static String applyURL = "http://www.sinha67.com/AppData/requisition.php";
    private int responseCode=0;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition);
        bundle=getIntent().getExtras();
        List<String> categories = new ArrayList<String>();
        categories.add("BEFTN");
        categories.add("CHEQUE");
        spinner=(Spinner)findViewById(R.id.which);
        amount=(EditText)findViewById(R.id.amount);
        apply=(Button)findViewById(R.id.apply);
        final ProgressDialog progressDialog = new ProgressDialog(
                Requisition.this);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    if(!amount.getText().toString().isEmpty()) {
                        if (Long.parseLong(amount.getText().toString()) >= 500) {

                            progressDialog.setMessage("Applying......");
                            progressDialog.show();

                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params = new RequestParams();
                            params.put("mobile_no", bundle.getString("mobile"));
                            params.put("reference", bundle.getString("reference"));
                            params.put("client_name", bundle.getString("name"));
                            params.put("auth", bundle.getString("auth"));
                            params.put("amount", amount.getText().toString());
                            params.put("via", spinner.getSelectedItem().toString());
                            Log.d("PARAMS>>",params.toString());
                            client.post(applyURL, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    String jsonString = new String(responseBody);
                                    Log.d("response>>",jsonString);
                                    try {
                                        JSONObject json = new JSONObject(jsonString);
                                        if (!json.isNull("status")) {
                                            if (json.getString("status").equalsIgnoreCase("Success")) {
                                                responseCode = 1;

                                            } else if (json.getString("status").equalsIgnoreCase("Repeat")) {
                                                responseCode = 4;

                                            } else {
                                                responseCode = 3;
                                            }
                                        } else {
                                            responseCode = 3;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        responseCode = 3;
                                    }
                                    if (responseCode == 1) {
                                        progressDialog.setMessage("Successfully applied");
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Successfully applied", Toast.LENGTH_LONG).show();

                                    }
                                    if (responseCode == 2) {
                                        Toast.makeText(getApplicationContext(), "Need an IPO name to apply", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();

                                    }
                                    if (responseCode == 4) {
                                        Toast.makeText(getApplicationContext(), "Can't apply again...... \n You already applied once!!", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                    if (responseCode == 3) {
                                        progressDialog.setMessage("Can't Apply");
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Failure ", Toast.LENGTH_LONG).show();

                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    progressDialog.dismiss();

                                }
                            });

//                        ApplyTask applyTask = new ApplyTask(Apply.this,
//                                progressDialog, mobile, reference, auth, boid, name, cash, ipo);
//                        applyTask.execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "SORRY!! \nAmount should be greater than 500", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "SORRY!! \nAmount is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
