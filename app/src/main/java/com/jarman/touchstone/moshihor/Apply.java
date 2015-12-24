// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.jarman.touchstone.moshihor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

// Referenced classes of package com.appsynopsis.jarman.dse:
//            ApplyTask, MainActivity

public class Apply extends Activity
{

    String auth = "12345jhkdfgdfoem123";
    private static String applyURL = "http://www.sinha67.com/AppData/application.php";
    String boid;
    TextView boids;
    String cash;
    TextView cashs;
    String mobile;
    String name,ipo;
    TextView names;
    String reference,start,end;
    TextView refs,ipos,starting,ending;
    private int responseCode=0;


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_apply);
        bundle = getIntent().getExtras();
        mobile = bundle.getString("mobile");
        reference = bundle.getString("reference");
        auth = bundle.getString("auth");
        boid = bundle.getString("boid");
        name = bundle.getString("name");
        ipo = bundle.getString("ipo");
        start=bundle.getString("start");
       end= bundle.getString("end");
        boids = (TextView)findViewById(R.id.boid);
        names = (TextView)findViewById(R.id.name);
        cashs = (TextView)findViewById(R.id.cash);
        refs = (TextView)findViewById(R.id.ref);
        ipos=(TextView)findViewById(R.id.ipo);
        starting=(TextView)findViewById(R.id.starting);
        ending=(TextView)findViewById(R.id.ending);
        starting.setText("Starting date : "+start);
        ending.setText("Ending date : " + end);
        ipos.setText(ipo);
        boids.setText((new StringBuilder()).append("BOID: ").append(boid).toString());
        names.setText(name);
        cashs.setText(mobile);
        refs.setText((new StringBuilder()).append("Ref no.:").append(reference).toString());
       final ProgressDialog progressDialog = new ProgressDialog(
                Apply.this);

        ((Button)findViewById(R.id.requi)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Apply.this,Requisition.class).putExtras(getIntent().getExtras()));
            }
        });
        ((Button)findViewById(R.id.apply)).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    if (!ipo.isEmpty()) {

                        progressDialog.setMessage("Applying......");
                        progressDialog.show();
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.put("mobile_no", mobile);
                        params.put("reference", reference);
                        params.put("ipo_name", ipo);
                        params.put("client_name", name);
                        params.put("auth", auth);
                        params.put("boid", boid);
                        client.post(applyURL, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String jsonString = new String(responseBody);
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
                        Toast.makeText(getApplicationContext(), "SORRY!! \nThere is no IPO to apply currently", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


        });
        ((Button)findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {



            public void onClick(View view)
            {
                Intent intent = new Intent(Apply.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            

        });
    }
}
