package ro.stery.orar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.stery.orar.model.Weather;
import ro.stery.orar.model.WeatherFormat;

public class MainActivity extends Activity {
    TextView weather_temp;
    TextView weather_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weather_temp = (TextView) findViewById(R.id.weather_temp);
        weather_city = (TextView) findViewById(R.id.weather_city);

        TextView luni = (TextView) findViewById(R.id.luni);
        luni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLuni();
            }
        });

        TextView marti = (TextView) findViewById(R.id.marti);
        marti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMarti();
            }
        });

        TextView miercuri = (TextView) findViewById(R.id.miercuri);
        miercuri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMiercuri();
            }
        });

        TextView joi = (TextView) findViewById(R.id.joi);
        joi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startJoi();
            }
        });

        fetchWeather();
    }

    public void startLuni() {
        Intent intent = new Intent(this, LuniActivity.class);
        startActivity(intent);
    }

    public void startMarti() {
        Intent intent = new Intent(this, MartiActivity.class);
        startActivity(intent);
    }

    public void startMiercuri() {
        Intent intent = new Intent(this, MiercuriActivity.class);
        startActivity(intent);
    }

    public void startJoi() {
        Intent intent = new Intent(this, JoiActivity.class);
        startActivity(intent);
    }

    public void fetchWeather() {

        Call<WeatherFormat> weatherFormatCall = Weather.Service.get().getWeather(Weather.city, Weather.key);
        weatherFormatCall.enqueue(new Callback<WeatherFormat>() {
            @Override
            public void onResponse(Call<WeatherFormat> call, Response<WeatherFormat> response) {

                if(response.isSuccessful()) {
                    WeatherFormat weather = response.body();
                    showWeather(weather);
                    Toast.makeText(MainActivity.this, "blabla", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherFormat> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showWeather(WeatherFormat weather) {

        String temperature = weather.getTemp() + "°C";
        weather_temp.setText(temperature);

    }
}
