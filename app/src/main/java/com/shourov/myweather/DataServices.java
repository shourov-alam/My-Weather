package com.shourov.myweather;

import android.view.View;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataServices {


    //data/2.5/forecast?lat=23.9067212&lon=90.3572588&APPID=423f12022aff378f6033b8cacac98f05&cnt=7


    @GET("data/2.5/forecast")

    Call<DailyForecast> getDailyForecast (@Query("lat") String lat,@Query("lon") String lon, @Query("appid") String apiKey, @Query("cnt") int cnt);
    //Call<DailyForecast> getDailyForecast (@Query("q") String city, @Query("appid") String apiKey, @Query("cnt") int cnt);
}
