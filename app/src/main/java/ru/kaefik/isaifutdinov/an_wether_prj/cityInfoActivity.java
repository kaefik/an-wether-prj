package ru.kaefik.isaifutdinov.an_wether_prj;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    ImageView imageWeatherConditions;
    TextView textViewDescriptionWeather;

    private CityModel cityDataWeather;
    private cityInfoAsyncTask task;

    class cityInfoAsyncTask extends AsyncTask<Void, Void, CityModel> {
        @Override
        protected CityModel doInBackground(Void... voids) {
            try {
                cityDataWeather.getHttpWeather();   //??? не нравится что использую в этом классе объект cityDataWeather
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return cityDataWeather;
        }

        @Override
        protected void onPostExecute(CityModel cityModel) {
            super.onPostExecute(cityModel);
            refreshData2View(cityModel);

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
        imageWeatherConditions = (ImageView) findViewById(R.id.imageWeatherConditions);
        textViewDescriptionWeather = (TextView) findViewById(R.id.textViewDescriptionWeather);

        cityDataWeather = new CityModel();
        try {
            cityDataWeather.getExtraIntent(getIntent());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        refreshData2View(cityDataWeather);

    }

    @Override
    protected void onStart() {
        super.onStart();
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
    public void goBackMainActivity() throws ParseException {
        if (task != null) {
            task.cancel(true);
        }

        setResult(RESULT_OK, cityDataWeather.putExtraIntent(this, MainActivity.class));
        finish();
    }

    public void onClickgoBackMainActivity(View view) throws ParseException {
        goBackMainActivity();
    }

    // обновление данных о погоде
    public void onClickRefreshDataWeather(View view) throws ExecutionException, InterruptedException {
        refreshDataWeather();
    }

    @Override
    //обработка нажатия клавиши Назад
    public void onBackPressed() {
        try {
            goBackMainActivity();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // обновление данных о погоде
    public void refreshDataWeather() throws ExecutionException, InterruptedException {

        if (task != null) {
            task.cancel(true);
        }
        task = new cityInfoAsyncTask();
        try {
            task.execute();
            cityDataWeather = task.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Toast.makeText(this,"Ошибка обновления данных погоды",Toast.LENGTH_SHORT);
        }

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
        imageWeatherConditions.setImageResource(getResourceImageFile("weather" + cityModel.getWeather("icon")));
        textViewDescriptionWeather.setText(cityModel.getWeather("description"));
        if(cityModel.getTimeRefresh()!=null) {
            textTimeRefresh.setText(cityModel.getTimeRefresh().toString());
        }else{
            textTimeRefresh.setText(R.string.unknown);
        }

    }

    //  из имени ресурса получить идентификатор на ресурс
    public int getResourceImageFile(String name) {
        return getResources().getIdentifier(name, "mipmap", getPackageName());
    }


}
