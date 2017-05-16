package ro.stery.orar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.stery.orar.model.Weather;
import ro.stery.orar.model.WeatherFormat;
import ro.stery.orar.receivers.OverchargingReceiver;
import ro.stery.orar.services.OverchargingService;

public class MainActivity extends Activity {
    TextView weather_temp;
    TextView weather_city;
    //BroadcastReceiver mBatteryReceiver;
    boolean serviceRunning = false;
    boolean firstRun = true;

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

        /*mBatteryReceiver = new OverchargingReceiver();
        this.registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));*/

        if(firstRun) {
            firstRun = false;
            startService(new Intent(this, OverchargingService.class));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.main_services:
                if(item.isChecked()) {
                    serviceRunning = false;
                    item.setChecked(serviceRunning);
                    stopService(new Intent(MainActivity.this, OverchargingService.class));
                } else {
                    if(serviceRunning == true) {
                        //Some error occured, the box is unchecked but the service is still running
                        Toast.makeText(MainActivity.this, "Error occured: service is already running", Toast.LENGTH_SHORT).show();
                    } else {
                        serviceRunning = true;
                        item.setChecked(serviceRunning);
                        startService(new Intent(MainActivity.this, OverchargingService.class));
                    }
                }
        }
        return true;
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
                } else {
                    Toast.makeText(MainActivity.this, "[Orar] Some error occurred while fetching the weather", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherFormat> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "[ORAR] No Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showWeather(WeatherFormat weather) {

        String temperature = weather.getTemp() + "°C";
        weather_temp.setText(temperature);

    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBatteryReceiver);
    }*/
}
