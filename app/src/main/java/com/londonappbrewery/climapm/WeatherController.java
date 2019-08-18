package com.londonappbrewery.climapm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.londonappbrewery.climapm.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class WeatherController extends AppCompatActivity {

    // Constants:
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    final String APP_ID = "d08bf628ce34759b5eaea3ad0ec99920";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 123;

    // TODO: Set LOCATION_PROVIDER here:
    final String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;


    // Member Variables:
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;
    ImageButton changeCityButton;
    // TODO: Declare a LocationManager and a LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        // Linking the elements in the layout to Java code
        mCityLabel = findViewById(R.id.locationTV);
        mWeatherImage = findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = findViewById(R.id.tempTV);
        changeCityButton = findViewById(R.id.changeCityButton);



        // TODO: Add an OnClickListener to the changeCityButton here:
         changeCityButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(WeatherController.this, change_city.class );
                 startActivity(intent);
              }
         });
    }


    // TODO: Add onResume() here:
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("Clima", "onResume() called");
        Intent loadIntent = getIntent();
        String city = loadIntent.getStringExtra("newCity");

        if(city != null){
            Log.d("Clima", "onResume: getWeatherForNewCIty is Called");
            getweatherForNewCity(city);
        }else {
            Log.d("Clima", "onResume: getWeatherForCurrentLocation is Called");
            getWeatherForCurrentLocation();
        }
    }


    // TODO: Add getWeatherForNewCity(String city) here:
    private void getweatherForNewCity(String city){
        RequestParams param = new RequestParams();
        param.put("q", city);
        param.put("appid", APP_ID);
        letsDoSomeNetworking(param);
    }


    // TODO: Add getWeatherForCurrentLocation() here:
    public void getWeatherForCurrentLocation(){

    mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("Clima", "Location changed");
            String longitude = String.valueOf(location.getLongitude());
            String latitude = String.valueOf(location.getLatitude());
            Log.d("Clima","The new location latitude is " + latitude + "and the longitude is " + longitude);

            RequestParams params = new RequestParams();
            params.put("lat",latitude);
            params.put("lon", longitude);
            params.put("appid", APP_ID);
            letsDoSomeNetworking(params);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Clima", "The status is changed, new status is " + status);
            Log.d("Clima", "status = 2: available, status = 1: temporarily unavailable, status = 0: out of service");
        }

        @Override
        public void onProviderEnabled(String provider) {
        Log.d("Clima", "onProviderEnabled(): service provider = "+ provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Clima", "onProviderDisabled(): service provider =" + provider);
        }

    };

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }else{
            mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Clima", "onRequestPermissionsResult: Permission Granted");
                getWeatherForCurrentLocation();
            } else{
                Log.d("Clima", "onRequestPermissionsResult: Permission Denied");
            }
        }

    }

    // TODO: Add letsDoSomeNetworking(RequestParams params) here:
    private void letsDoSomeNetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("Clima", "onSuccess: status code = " + statusCode );
                Log.d("Clima", "onSuccess: JSON response = " + response.toString());
                Toast.makeText(getApplicationContext(), "The request was successful", Toast.LENGTH_SHORT).show();
                WeatherDataModel receivedData = WeatherDataModel.fromJson(response);
                updateUI(receivedData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("Clima", "on Failure: The request failed with status code = " + statusCode );
                Log.e("Clima", "on Failure: The error message = " + throwable.toString());
            }
        });
    }

    // TODO: Add updateUI() here:
    private void updateUI(WeatherDataModel receivedData){
        mCityLabel.setText(receivedData.getMcity());
        mTemperatureLabel.setText(receivedData.getTemperature());
        int resourceID  = getResources().getIdentifier(receivedData.getIconName(),"drawable",getPackageName());
        mWeatherImage.setImageResource(resourceID);
    }

    // TODO: Add onPause() here:

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager != null){
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
