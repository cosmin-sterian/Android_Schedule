package ro.stery.orar.model;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Stery on 20.04.2017.
 */

public interface Weather {

    String city = "Bucharest";
    String key = "1654bf81ae0be8793998b640e9bce324";

    @GET("/weather")
    Call<WeatherFormat> getWeather(@Query("q") String city, @Query("appid") String appid);

    class Service {

        private static Weather sInstance;

        public synchronized static Weather get() {
            if(sInstance == null) {
                sInstance = new Retrofit.Builder().baseUrl("http://api.openweathermap.org/data/2.5/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(Weather.class);
            }
            return sInstance;
        }

    }

}