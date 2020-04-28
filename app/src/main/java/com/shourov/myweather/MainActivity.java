package com.shourov.myweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;

import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;



public class MainActivity extends AppCompatActivity  {

    RecyclerView recyclerView;
   // int PERMISSION_ID = 44;

   // ImageView im;

    double Lat,Lon;
    TextView tv;
    private Geocoder geocoder;
    private List<Address> addresses;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    SwipeRefreshLayout swipeRefreshLayout;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.rv);
        tv=findViewById(R.id.txt);
        swipeRefreshLayout=findViewById(R.id.swipe);
       // im=findViewById(R.id.img);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        call();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                call();

                //swipeRefreshLayout.setRefreshing(false);

             /*  new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       swipeRefreshLayout.setRefreshing(false);
                   }
               },1000);  */


            }
        });

    }



    private void call() {

        geocoder=new Geocoder(this, Locale.ENGLISH);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    Lat=location.getLatitude();
                    Lon=location.getLongitude();


                    try {
                        addresses=geocoder.getFromLocation(Lat,Lon,1);
                        Address address = addresses.get(0);
                        tv.setText(address.getLocality()+","+address.getSubAdminArea()+","+address.getCountryName()+".");


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //  retrofit();
                }
                retrofit();
            }

        };

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

    }

    private void createLocationRequest(){

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},101);


            return;
        }

        // fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null);

    }


   @Override
    public void onResume() {
       super.onResume();

   }


    private void retrofit() {

        String lat = String.valueOf(Lat);
        String lon = String.valueOf(Lon);

        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        DataServices dataServices = retrofit.create(DataServices.class);
        Call<DailyForecast> call = dataServices.getDailyForecast(lat, lon, "423f12022aff378f6033b8cacac98f05", 40);

        call.enqueue(new Callback<DailyForecast>() {
            @Override
            public void onResponse(Call<DailyForecast> call, Response<DailyForecast> response) {

                //   int statusCode = response.code();
                // Toast.makeText(getApplicationContext(),statusCode,Toast.LENGTH_LONG).show();

                if (response.isSuccessful()) {

                    DailyForecast dailyForecast = response.body();
                    List<Day> days = dailyForecast.getList();

                    //  double Lat=   dailyForecast.getCity().getCoord().getLat();
                    // double Lon=   dailyForecast.getCity().getCoord().getLon();

                    //Toast.makeText(getApplicationContext(),dailyForecast.getCity().getName(),Toast.LENGTH_LONG).show();
                    WeatherAdapter weatherAdapter = new WeatherAdapter(MainActivity.this, days);
                    recyclerView.setAdapter(weatherAdapter);
                    swipeRefreshLayout.setRefreshing(false);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);


                } else {

                    Toast.makeText(getApplicationContext(), "Error! Try Again", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<DailyForecast> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Check Internet Connection!!",Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
