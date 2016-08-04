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

    TextView mNameCity;
    TextView mTempCity;
    TextView mCloudsCity;
    TextView mHuminidityCity;
    TextView mPressureCity;
    TextView mWindspeedCity;
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
                mCityDataWeather.getHttpWeather();   //??? не нравится что использую в этом классе объект mCityDataWeather
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
        mCloudsCity = (TextView) findViewById(R.id.cloudsCity);
        mHuminidityCity = (TextView) findViewById(R.id.huminidityCity);
        mPressureCity = (TextView) findViewById(R.id.pressureCity);
        mWindspeedCity = (TextView) findViewById(R.id.windspeedCity);
        mWinddirectionCity = (TextView) findViewById(R.id.winddirectionCity);
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

    //обработка нажатия клавиши Назад
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
        mCloudsCity.setText(Float.toString(cityModel.getClouds()));
        mHuminidityCity.setText(Float.toString(cityModel.getHuminidity()) + getString(R.string.znak_procent));
        mPressureCity.setText(Float.toString(cityModel.getPressure() * 0.75f) + getString(R.string.mm_rt_st));
        mWindspeedCity.setText(Float.toString(cityModel.getWindspeed()) + getString(R.string.metr_v_sec));
        mWinddirectionCity.setText(Float.toString(cityModel.getWinddirection()) + getString(R.string.gradus));
        mImageWeatherConditions.setImageResource(getResourceImageFile("weather" + cityModel.getWeather("icon")));
        mTextViewDescriptionWeather.setText(cityModel.getWeather("description"));
        if ((cityModel.getTimeRefresh() != null) | (cityModel.getTimeRefresh().trim().equals(""))) {
            mTextTimeRefresh.setText(cityModel.getTimeRefresh().toString());
        } else {
            mTextTimeRefresh.setText(R.string.unknown);
        }
    }

    //  из имени ресурса получить идентификатор на ресурс
    public int getResourceImageFile(String name) {
        return getResources().getIdentifier(name, "mipmap", getPackageName());
    }

}
