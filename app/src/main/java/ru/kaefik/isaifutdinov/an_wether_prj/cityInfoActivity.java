/*
  * Copyright (C) 2016 Ilnur Saifutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/

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
import ru.kaefik.isaifutdinov.an_wether_prj.utils.Utils;

// второй экран activity который отображает подробную информацию о выбранном городе
public class cityInfoActivity extends AppCompatActivity {

    TextView mNameCity;
    TextView mTempCity;
    TextView mCloudsCity;
    TextView mHuminidityCity;
    TextView mPressureCity;
    TextView mWindCity;
    TextView mWinddirectionCity;
    TextView mTextTimeRefresh;
    ImageView mImageWeatherConditions;
    TextView mTextViewDescriptionWeather;

    private CityModel mCityDataWeather;
    private cityInfoAsyncTask mTask;

    class cityInfoAsyncTask extends AsyncTask<Void, Void, CityModel> {
        @Override
        protected CityModel doInBackground(Void... voids) {
            try {
                // TODO: не нравится что использую в этом классе объект mCityDataWeather
                mCityDataWeather.getHttpWeather();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return mCityDataWeather;
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

        mNameCity = (TextView) findViewById(R.id.nameCity);
        mTempCity = (TextView) findViewById(R.id.tempCity);
//        mCloudsCity = (TextView) findViewById(R.id.cloudsCity);
        mHuminidityCity = (TextView) findViewById(R.id.huminidityCity);
        mPressureCity = (TextView) findViewById(R.id.pressureCity);
        mWindCity = (TextView) findViewById(R.id.windCity);
//        mWinddirectionCity = (TextView) findViewById(R.id.winddirectionCity);
        mTextTimeRefresh = (TextView) findViewById(R.id.textTimeRefresh);
        mImageWeatherConditions = (ImageView) findViewById(R.id.imageWeatherConditions);
        mTextViewDescriptionWeather = (TextView) findViewById(R.id.textViewDescriptionWeather);

        mCityDataWeather = new CityModel();
        try {
            mCityDataWeather.getExtraIntent(getIntent());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        refreshData2View(mCityDataWeather);

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
        if (mTask != null) {
            mTask.cancel(true);
        }
        setResult(RESULT_OK, mCityDataWeather.putExtraIntent(this, MainActivity.class));
        finish();
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
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new cityInfoAsyncTask();
        try {
            mTask.execute();
            mCityDataWeather = mTask.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Toast.makeText(this, R.string.strErrorUpdateCityInfo, Toast.LENGTH_SHORT);
        }
        refreshData2View(mCityDataWeather);
    }

    // отображение данных о погоде выбранного города
    public void refreshData2View(CityModel cityModel) {
        mNameCity.setText(cityModel.getName());
        mTempCity.setText(Float.toString(cityModel.getTemp()) + getString(R.string.strCelcium));
//        mCloudsCity.setText(Float.toString(cityModel.getClouds()));
        mHuminidityCity.setText(Float.toString(cityModel.getHuminidity()) + getString(R.string.znak_procent));
        mPressureCity.setText(Float.toString(cityModel.getPressure() * 0.75f) + getString(R.string.mm_rt_st));
        mImageWeatherConditions.setImageResource(getResourceImageFile("weather" + cityModel.getWeather("icon")));
        mTextViewDescriptionWeather.setText(cityModel.getWeather("description"));
        if ((cityModel.getTimeRefresh() != null) & (!cityModel.getTimeRefresh().trim().equals(""))) {
            mTextTimeRefresh.setText(cityModel.getTimeRefresh().toString());
        } else {
            mTextTimeRefresh.setText(R.string.unknown);
        }

        mWindCity.setText(Utils.windGradus2Rumb(cityModel.getWinddirection())+" ( "+Float.toString(cityModel.getWindspeed())+ getString(R.string.metr_v_sec)+" )");
    }

    //  из имени ресурса получить идентификатор на ресурс
    public int getResourceImageFile(String name) {
        return getResources().getIdentifier(name, "mipmap", getPackageName());
    }

}
