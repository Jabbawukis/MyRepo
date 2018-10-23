package com.cortado.geolocator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity{
    LocationManager locationManager;
    LocationListener locationListener;
    double lat = 0;
    double lng = 0;
    String IMEI = "";
    int count = 0;

    private ArrayList<String> mEntries;
    public void run(String url, final String IMEI) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e("Rest Response", response);
        }
       }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
        @Override
        public void onErrorResponse(VolleyError error) {
            //This code is executed if there is an error.
        }
        }) {
        protected Map<String, String> getParams() {
            String Slng = new Double(lng).toString();
            String Slat = new Double(lat).toString();
            Map<String, String> params = new HashMap<String, String>();
            params.put("imei", IMEI);
            params.put("longetude", Slng);
            params.put("latetude", Slat);

            return params;
        }
      };
        MyRequestQueue.add(MyStringRequest);
    }
    public void showHistory(final ArrayList<String> list){
        final ArrayList<String> mEntries1 = new ArrayList<>();
        for(int i = 0; i <list.size(); i++ ){
            String temp = list.get(i);
            mEntries1.add(temp);
        }
        final ArrayList<String> mEntries2 = new ArrayList<>();
        for(int i = 0; i <mEntries1.size(); i++ ){
            String temp = mEntries1.get(i);
            String remove1= "Value ";
            temp = temp.replace(remove1, "" );
            String remove = " at " + String.valueOf(i) + " of type java.lang.String cannot be converted to JSONObject";
            temp = temp.replace(remove, "" );
            mEntries2.add (temp);
        }
       /* SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);*/
        Intent intent = new Intent(this, Activity2.class);
        intent.putExtra ("data", mEntries2);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                TextView locationText = (TextView) findViewById(R.id.textView2);
                locationText.setText("Longitude: " + location.getLongitude() + "\n" + "Latitude: " + location.getLatitude());
                lat = location.getLatitude();
                lng = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        configure_button();
    }
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode){
                case 10:
                    configure_button();
                    break;
                default:
                    break;
            }
        }
        void configure_button() {
            // first check for permissions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                return;
            }
            Button send = (Button) findViewById(R.id.send);
            send.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onClick(View view) {
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                    String urlString = "http://10.0.2.2:5000/v1/Locate";
                    //String IMEI = "123";
                    IMEI = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
                    run(urlString, IMEI);
                }
            });
            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            Button get = (Button) findViewById(R.id.get);
            get.setOnClickListener(new View.OnClickListener() {
                String url1 = "http://10.0.2.2:5000/v1/Locate";
                String IMEI = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
                String url = url1 + "/" + IMEI;
                @Override
                public void onClick(View view) {
                    JsonObjectRequest objectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    String Long = null;
                                    try {
                                        Long = response.getJSONObject(IMEI).getString("longetude");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String Lat = null;
                                    try {
                                        Lat = response.getJSONObject(IMEI).getString("latetude");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("Rest Response", response.toString());
                                    TextView textView = (TextView) findViewById(R.id.textView);
                                    textView.setText("Longitude: " + Long + "\n" + "Latitude: " + Lat);
                                    lat = Double.parseDouble(Lat);
                                    lng = Double.parseDouble(Long);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Rest Response", error.toString());
                                }
                            }
                    );
                    requestQueue.add(objectRequest);
                }
            });
            Button showOnMap = (Button) findViewById(R.id.showOnMap);
            showOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double myLatitude = lat;
                    Double myLongitude = lng;
                    String labelLocation = "Device= " + Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + myLatitude  + ">,<" + myLongitude + ">?q=<" + myLatitude  + ">,<" + myLongitude + ">(" + labelLocation + ")"));
                    if (lng != 0 && lat !=0 ){
                    startActivity(intent);}
                }
            });
            final RequestQueue requestQueue1 = Volley.newRequestQueue(this);
            final Button showHistory = (Button) findViewById(R.id.showHistory);
            showHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url1 = "http://10.0.2.2:5000/v1/Locate";
                    final String IMEI = Secure.getString (getApplicationContext ().getContentResolver (), Secure.ANDROID_ID);
                    String newUrl = url1 + "/" + IMEI + "/History";
                    JsonArrayRequest request = new JsonArrayRequest(newUrl,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray jsonArray) {
                                    count = jsonArray.length();
                                    for(int i = 0; i < jsonArray.length(); i++) {
                                        try {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            mEntries.add(jsonObject.toString());
                                        }
                                        catch(JSONException e) {
                                            mEntries.add(e.getLocalizedMessage());
                                        }
                                        if(mEntries.size ()== count){
                                            showHistory(mEntries);
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(MainActivity.this, "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    mEntries = new ArrayList<>();
                    requestQueue1.add(request);
                }
             });
        }
}






















