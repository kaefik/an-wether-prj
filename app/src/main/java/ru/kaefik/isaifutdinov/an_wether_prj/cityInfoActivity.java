package ru.kaefik.isaifutdinov.an_wether_prj;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import ru.kaefik.isaifutdinov.an_wether_prj.city.CityModel;

public class cityInfoActivity extends AppCompatActivity {

    TextView nameCity;
    TextView tempCity;
    TextView cloudsCity;
    TextView huminidityCity;
    TextView pressureCity;
    TextView windspeedCity;
    TextView winddirectionCity;

    private CityModel cityDataWeather;
    private cityInfoAsyncTask task;

    class cityInfoAsyncTask extends AsyncTask<Void, Void, CityModel> {
        @Override
        protected CityModel doInBackground(Void... voids) {
            System.out.println(cityDataWeather.getName());
            cityDataWeather.getHttpWeather();   //??? не нравится что использую в этом классе объект cityDataWeather
            return cityDataWeather;
        }

        @Override
        protected void onPostExecute(CityModel cityModel) {
            super.onPostExecute(cityModel);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_info);

        nameCity = (TextView) findViewById(R.id.nameCity);
        tempCity = (TextView) findViewById(R.id.tempCity);
        cloudsCity = (TextView) findViewById(R.id.cloudsCity);
        huminidityCity = (TextView) findViewById(R.id.huminidityCity);
        pressureCity = (TextView) findViewById(R.id.pressureCity);
        windspeedCity = (TextView) findViewById(R.id.windspeedCity);
        winddirectionCity = (TextView) findViewById(R.id.winddirectionCity);

        String currentName = getIntent().getStringExtra("name").toString();

        cityDataWeather = new CityModel(currentName);
        nameCity.setText(currentName);
        System.out.println(""+currentName);

        task = new cityInfoAsyncTask();

        // обновление погоды
        try {
            refreshDataWeather();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void goBackMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // обновление данных о погоде
    public void onClickRefreshDataWeather(View view) throws ExecutionException, InterruptedException {
        refreshDataWeather();
    }

    public void  refreshDataWeather() throws ExecutionException, InterruptedException{
        task.execute();
        cityDataWeather = task.get();

        // получение отправленных данных и отображение данных
        nameCity.setText(cityDataWeather.getName());
        tempCity.setText("temp:          " + Float.toString(cityDataWeather.getTemp()));
        cloudsCity.setText("clouds:        " + Float.toString(cityDataWeather.getClouds()));
        huminidityCity.setText("huminidity:    " + Float.toString(cityDataWeather.getHuminidity()));
        pressureCity.setText("pressure:      " + Float.toString(cityDataWeather.getPressure()));
        windspeedCity.setText("windspeed:     " + Float.toString(cityDataWeather.getWindspeed()));
        winddirectionCity.setText("winddirection: " + Float.toString(cityDataWeather.getWinddirection()));
    }

}
