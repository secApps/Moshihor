package com.jarman.touchstone.moshihor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private static String loginURL = "http://www.sinha67.com/AppData/userloginNew.php";
    String auth="12345jhkdfgdfoem123";
    String boid;
    String ipo;
    String start,end;
    Context context;
    String mobileno;
    String name;
    private ProgressDialog progressDialog;
    String referenceno;
    private int responseCode=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      final   EditText ref = (EditText)findViewById(R.id.reference);
       final EditText edittext = (EditText)findViewById(R.id.mobile);

       final ProgressDialog progressDialog = new ProgressDialog(
                MainActivity.this);

        ((Button)findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                mobileno = edittext.getText().toString();
                referenceno = ref.getText().toString();
                if (isNetworkAvailable()) {

                    progressDialog.setMessage("Logging in...");
                    progressDialog.show();
//                    LoginTask loginTask = new LoginTask(MainActivity.this,
//                            progressDialog, MainActivity.this);
//                    loginTask.execute();
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("mobile", mobileno);
                    params.put("reference", referenceno);
                    params.put("auth", auth);
                    Log.d("PARAMS",params.toString());

                    client.post(loginURL, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String jsonString = new String(responseBody);
                            try {
                                JSONObject jsonobject = new JSONObject(jsonString);
                                Log.d("RESPOnse", jsonobject.toString());

                                if (!jsonobject.isNull("status")) {
                                    if (jsonobject.getString("status").equalsIgnoreCase("Success")) {

                                        responseCode = 1;
                                        JSONArray json = jsonobject.getJSONArray("UserData");
                                        boid = json.getJSONObject(0).getString("boid");
                                        name = json.getJSONObject(0).getString("name");
                                        ipo = json.getJSONObject(0).getString("ipo");
                                        start = json.getJSONObject(0).getString("start");
                                        end = json.getJSONObject(0).getString("end");
                                        Log.d("RESPONSE>>", (new StringBuilder()).append(boid).append(name).toString());
                                    }
                                }

                                if (responseCode == 1) {
                                    progressDialog.setMessage("Login successful");
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "LOGGED IN", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(getApplicationContext(), Apply.class);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("mobile", mobileno);
                                    bundle.putString("reference", referenceno);
                                    bundle.putString("auth", auth);
                                    bundle.putString("boid", boid);
                                    bundle.putString("name", name);
                                    bundle.putString("ipo", ipo);
                                    bundle.putString("start", start);
                                    bundle.putString("end", end);
                                    in.putExtras(bundle);
                                    startActivity(in);
                                    finish();
                                    // return;
                                } else {
                                    progressDialog.setMessage("Can't login");
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Failure ", Toast.LENGTH_SHORT).show();
                                    // return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    //return;
                }
            }


            {


            }
        });
    }
    public boolean isNetworkAvailable() {
        Log.d("ERROR>>","HERE");
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            Log.d("ERROR>>","HERE");
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }catch (Exception e){
         Log.d("ERROR>>",e.toString())   ;
            return true;
        }
    }
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

}
