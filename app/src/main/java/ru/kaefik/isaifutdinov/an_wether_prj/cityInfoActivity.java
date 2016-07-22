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
        cityDataWeather.setTemp(getIntent().getFloatExtra("temp",0.0f));
        cityDataWeather.setClouds(getIntent().getFloatExtra("clouds",0.0f));
        cityDataWeather.setHuminidity(getIntent().getFloatExtra("huminidity",0.0f));
        cityDataWeather.setPressure(getIntent().getFloatExtra("pressure",0.0f));
        cityDataWeather.setWindspeed(getIntent().getFloatExtra("windspeed",0.0f));
        cityDataWeather.setWinddirection(getIntent().getFloatExtra("winddirection",0.0f));
        cityDataWeather.setTimeRefresh();


//        cityDataWeather.setTemp(currentTemp);
        nameCity.setText(getIntent().getStringExtra("name").toString());
        tempCity.setText("temp:          "+Float.toString(getIntent().getFloatExtra("temp",0.0f)));
        cloudsCity.setText("clouds:        "+Float.toString(getIntent().getFloatExtra("clouds",0.0f)));
        huminidityCity.setText("huminidity:    "+Float.toString(getIntent().getFloatExtra("huminidity",0.0f)));
        pressureCity.setText("pressure:      "+Float.toString(getIntent().getFloatExtra("pressure",0.0f)));
        windspeedCity.setText("windspeed:     "+Float.toString(getIntent().getFloatExtra("windspeed",0.0f)));
        winddirectionCity.setText("winddirection: "+Float.toString(getIntent().getFloatExtra("winddirection",0.0f)));
//        textTimeRefresh.setText(----);  // сделать передачу времени последнего обновления

//        task = new cityInfoAsyncTask();

//        // обновление погоды
//        try {
//            refreshDataWeather();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        cityDataWeather.setTimeRefresh();
        textTimeRefresh.setText(cityDataWeather.getTimeRefresh().toString());


    }

    public void goBackMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        intent.putExtra("name",cityDataWeather.getName());
        intent.putExtra("id",cityDataWeather.getId());
        intent.putExtra("country",cityDataWeather.getCountry());
        intent.putExtra("temp",cityDataWeather.getTemp());
        intent.putExtra("clouds",cityDataWeather.getClouds());
        intent.putExtra("huminidity",cityDataWeather.getHuminidity());
        intent.putExtra("pressure",cityDataWeather.getPressure());
        intent.putExtra("windspeed",cityDataWeather.getWindspeed());
        intent.putExtra("winddirection",cityDataWeather.getWinddirection());
        intent.putExtra("timeRefresh",cityDataWeather.getTimeRefresh());
        setResult(RESULT_OK,intent);
        finish();
    }

    // обновление данных о погоде
    public void onClickRefreshDataWeather(View view) throws ExecutionException, InterruptedException {
        refreshDataWeather();
    }

    public void refreshDataWeather() throws ExecutionException, InterruptedException {

        if (task!=null) {
            task.cancel(true);
        }
        task = new cityInfoAsyncTask();  // !!!!! заменить на отмену предыдущего обновления и запуск нового обновления данных о погоде
        task.execute();
        cityDataWeather = task.get();


    // получение отправленных данных и отображение данных
    nameCity.setText(cityDataWeather.getName());
    tempCity.setText("temp:          "+Float.toString(cityDataWeather.getTemp()));
    cloudsCity.setText("clouds:        "+Float.toString(cityDataWeather.getClouds()));
    huminidityCity.setText("huminidity:    "+Float.toString(cityDataWeather.getHuminidity()));
    pressureCity.setText("pressure:      "+Float.toString(cityDataWeather.getPressure()));
    windspeedCity.setText("windspeed:     "+Float.toString(cityDataWeather.getWindspeed()));
    winddirectionCity.setText("winddirection: "+Float.toString(cityDataWeather.getWinddirection()));
    cityDataWeather.setTimeRefresh();
    textTimeRefresh.setText(cityDataWeather.getTimeRefresh().toString());
}

}
