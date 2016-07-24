package ru.kaefik.isaifutdinov.an_wether_prj;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import ru.kaefik.isaifutdinov.an_wether_prj.city.CityModel;
import ru.kaefik.isaifutdinov.an_wether_prj.utils.Utils;

public class cityInfoActivity extends AppCompatActivity {

    TextView nameCity;
    TextView tempCity;
    TextView cloudsCity;
    TextView huminidityCity;
    TextView pressureCity;
    TextView windspeedCity;
    TextView winddirectionCity;
    TextView textTimeRefresh;

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
        textTimeRefresh = (TextView) findViewById(R.id.textTimeRefresh);

        cityDataWeather = new CityModel(getIntent().getStringExtra("name").toString());
        cityDataWeather.setTemp(getIntent().getFloatExtra("temp", 0.0f));
        cityDataWeather.setClouds(getIntent().getFloatExtra("clouds", 0.0f));
        cityDataWeather.setHuminidity(getIntent().getFloatExtra("huminidity", 0.0f));
        cityDataWeather.setPressure(getIntent().getFloatExtra("pressure", 0.0f));
        cityDataWeather.setWindspeed(getIntent().getFloatExtra("windspeed", 0.0f));
        cityDataWeather.setWinddirection(getIntent().getFloatExtra("winddirection", 0.0f));
        cityDataWeather.setTimeRefresh();  // ???? - разобраться с тем как получить дату из intent


        refreshData2View(cityDataWeather);
//        nameCity.setText(cityDataWeather.getName());
//        tempCity.setText(Float.toString(cityDataWeather.getTemp())+" C");
//        cloudsCity.setText(Float.toString(cityDataWeather.getClouds()));
//        huminidityCity.setText(cityDataWeather.getHuminidity()+" %");
//        pressureCity.setText(Float.toString(cityDataWeather.getPressure()*0.75f)+" мм рт.ст."); // 1hPa ~= 0.750064  мм рт. ст.
//        windspeedCity.setText(Float.toString(cityDataWeather.getPressure())+"м/с");
//        winddirectionCity.setText(Float.toString(cityDataWeather.getWinddirection()));
//        textTimeRefresh.setText(cityDataWeather.getTimeRefresh().toString());


        // обновление погоды
        try {
            refreshDataWeather();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // возврат к основной активити MainActivity
    public void goBackMainActivity() {
        if (task != null) {
            task.cancel(true);
        }

        setResult(RESULT_OK, Utils.intentPutExtra(this, MainActivity.class, cityDataWeather));
        finish();
    }

    public void onClickgoBackMainActivity(View view) {
        goBackMainActivity();
    }

    // обновление данных о погоде
    public void onClickRefreshDataWeather(View view) throws ExecutionException, InterruptedException {
        refreshDataWeather();
    }

    @Override
    //обработка нажатия клавиши Назад
    public void onBackPressed() {
        goBackMainActivity();

    }

    // обновление данных о погоде
    public void refreshDataWeather() throws ExecutionException, InterruptedException {

        if (task != null) {
            task.cancel(true);
        }
        task = new cityInfoAsyncTask();
        task.execute();
        cityDataWeather = task.get();

        cityDataWeather.setTimeRefresh();

        refreshData2View(cityDataWeather);
    }

    // отображение данных о погоде выбранного города
    public void refreshData2View(CityModel cityModel) {
        nameCity.setText(cityModel.getName());
        tempCity.setText(Float.toString(cityModel.getTemp()) + " C");
        cloudsCity.setText(Float.toString(cityModel.getClouds()));
        huminidityCity.setText(Float.toString(cityModel.getHuminidity()) + " %");
        pressureCity.setText(Float.toString(cityModel.getPressure() * 0.75f) + " мм рт.ст.");
        windspeedCity.setText(Float.toString(cityModel.getWindspeed()) + " м/с");
        winddirectionCity.setText(Float.toString(cityModel.getWinddirection()) + " град.");
        textTimeRefresh.setText(cityModel.getTimeRefresh().toString());
    }


}
