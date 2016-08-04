package ru.kaefik.isaifutdinov.an_wether_prj;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ru.kaefik.isaifutdinov.an_wether_prj.adapter.CityModelAdapter;
import ru.kaefik.isaifutdinov.an_wether_prj.city.CityModel;
import ru.kaefik.isaifutdinov.an_wether_prj.utils.RequestCode;
import ru.kaefik.isaifutdinov.an_wether_prj.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private ListView mNameCity;
    private EditText mEditTextAddNewCity;
    private String mMY_APPID; // уникальный ключ для доступа к сервису OpenWeatherMap
    private SharedPreferences mSPref;
    List<CityModel> mListDataCity; // данные прогноза погоды списка городов

    private cityInfoAsyncTask mTask;


    // task для обновления у списка городов прогноза погоды
    class cityInfoAsyncTask extends AsyncTask<List<CityModel>, CityModel, List<CityModel>> {
        @Override
        protected List<CityModel> doInBackground(List<CityModel>... listcityModels) {
            for (int i = 0; i < listcityModels[0].size(); i++) {
                try {
                    listcityModels[0].get(i).getHttpWeather();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                publishProgress(listcityModels[0].get(i));
            }
            return listcityModels[0];
        }

        @Override
        // вывод промежуточных результатов
        protected void onProgressUpdate(CityModel... values) {
            super.onProgressUpdate(values);
            mNameCity.invalidateViews();
            MainActivity.this.setProgressBarIndeterminateVisibility(false);
        }

        @Override
        // после обновления всех прогнозов, сохранение данных в файлах
        protected void onPostExecute(List<CityModel> values) {
            super.onPostExecute(values);
            try {
                saveCityInfoToFile();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); // добавления прогресс бара на заголовок activity
        setContentView(R.layout.activity_main);

        mNameCity = (ListView) findViewById(R.id.listView);
        mEditTextAddNewCity = (EditText) findViewById(R.id.editTextAddNewCity);

        setMY_APPID("9a4be4eeb7de3b88211989559a0849f7"); // со временем можно сделать настрйоку чтобы можно было в программе менять APPID

        if (mListDataCity == null) {
            mListDataCity = new ArrayList<CityModel>();
            loadListCity();
        }

        final CityModelAdapter adapter = new CityModelAdapter(this, mListDataCity);
        mNameCity.setAdapter(adapter);

        // Обработка события на клик по элементу списка
        mNameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityModel tmpCityModel = adapter.getCityModel(position);
                try {
                    startActivityForResult(tmpCityModel.putExtraIntent(getApplicationContext(), cityInfoActivity.class), RequestCode.REQUEST_CODE_CITY_WEATHER);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // Обработка долгого нажатия на элемент
        mNameCity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final CityModel selectedItem = (CityModel) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.strDeleteIte)
                        .setMessage(getString(R.string.strDeleteCityQuestion) + selectedItem.getName() + "?")
                        .setCancelable(false)
                        .setPositiveButton(R.string.strDel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mListDataCity.remove(position);
                                        mNameCity.invalidateViews();
                                        saveListCity();
                                        Toast.makeText(getApplicationContext(), getString(R.string.strgorod) + "  " + selectedItem.getName() + " удалён.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton(R.string.strOstatsya,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });


        try {
            restoreCityInfoFromFile();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startcityInfoAsyncTask(mListDataCity);
    }


    // ручное обновление погоды в списке
    public void onClickRefreshCityInfo(View v) throws JSONException {
        try {
            startcityInfoAsyncTask(mListDataCity);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.strErrorUpdateCityInfo, Toast.LENGTH_SHORT);
        }
    }

    // ----- задействовать данный параметр
    public String getMY_APPID() {
        return mMY_APPID;
    }

    public void setMY_APPID(String MY_APPID) {

        this.mMY_APPID = MY_APPID;
    }
    // ----- END задействовать данный параметр


    // запуск задание cityInfoAsyncTask на обновления информации списка городов
    public void startcityInfoAsyncTask(List<CityModel> listCity) {
        MainActivity.this.setProgressBarIndeterminateVisibility(true);
        mTask = new cityInfoAsyncTask();
        mTask.execute(mListDataCity);

        try {
            mTask.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Toast.makeText(getApplicationContext(), R.string.strErrorUpdateCityInfo, Toast.LENGTH_SHORT);
        } catch (ExecutionException e) {
            Toast.makeText(getApplicationContext(), R.string.strErrorUpdateCityInfo, Toast.LENGTH_SHORT);
        } catch (TimeoutException e) {
            Toast.makeText(getApplicationContext(), R.string.strErrorUpdateCityInfo, Toast.LENGTH_SHORT);
        }
        saveListCity();

    }


    @Override
    // прием данных CityModel выбраного города из другое активити
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.REQUEST_CODE_CITY_WEATHER:
                    CityModel tmpCityData = new CityModel();
                    try {
                        tmpCityData.getExtraIntent(intent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // СЮДА ДОБАВИТЬ ОБНОВЛЕНИЕ ИНФОРМАЦИИ О ГОРОДЕ tmpCityData.getName() в mListDataCity
                    for (int i = 0; i < mListDataCity.size(); i++) {
                        if (mListDataCity.get(i).getName().equals(tmpCityData.getName())) {
                            mListDataCity.get(i).setTemp(tmpCityData.getTemp());
                            mListDataCity.get(i).setId(tmpCityData.getId());
                            mListDataCity.get(i).setCountry(tmpCityData.getCountry());
                            mListDataCity.get(i).setClouds(tmpCityData.getClouds());
                            mListDataCity.get(i).setPressure(tmpCityData.getPressure());
                            mListDataCity.get(i).setHuminidity(tmpCityData.getHuminidity());
                            mListDataCity.get(i).setWinddirection(tmpCityData.getWinddirection());
                            mListDataCity.get(i).setWindspeed(tmpCityData.getWindspeed());
                            mListDataCity.get(i).setTimeRefresh(tmpCityData.getTimeRefresh());
                        }
                    }
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.strErrorUpdateCityInfo, Toast.LENGTH_SHORT);
        }
        mNameCity.invalidateViews();
    }

    // добавления нового города
    public void onClickAddCity(View v) {
        String newCity = Utils.firstUpCaseString(mEditTextAddNewCity.getText().toString().trim());
        // СЮДА ДОБАВИТЬ ПРОВЕРКИ ВВОДА НАЗВАНИЯ ГОРОДА
        if ((!newCity.equals("")) && (!isExistNameFromList(mListDataCity, newCity))) {
            mListDataCity.add(new CityModel(newCity, getMY_APPID()));
            Toast.makeText(getApplicationContext(), getString(R.string.txtaddcityedit) + newCity, Toast.LENGTH_SHORT).show();
        }
        mEditTextAddNewCity.setText("");
        // прячем клавиатуру
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditTextAddNewCity.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        startcityInfoAsyncTask(mListDataCity);
        saveListCity();
    }

    @Override
    // обработка нажатия кнопки Назад
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.strImportantMessage)
                .setMessage(R.string.strQuitQuestion)
//                .setIcon(R.drawable.ic_android_cat)
                .setCancelable(false)
                .setPositiveButton(R.string.strostatsya,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(), R.string.strthankyou, Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton(R.string.strQuit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.super.onBackPressed();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // проверка на то что namecity есть в списке имен городов, true - есть, false - нет
    public boolean isExistNameFromList(List<CityModel> listcity, String namecity) {
        boolean flag = false;
        for (int i = 0; i < listcity.size(); i++) {
            if (listcity.get(i).getName().equals(namecity)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    // восстановление сохраненых данных о погоде (каждый город - отдельный файл с Josn)
    public void restoreCityInfoFromFile() throws JSONException {
        Boolean flagExistFile = true;
        for (int i = 0; i < mListDataCity.size(); i++) {
            String nameFile = mListDataCity.get(i).getName();
            mListDataCity.get(i).openFile(nameFile + ".txt", getApplicationContext());
        }
        mNameCity.invalidateViews();
    }

    //сохранение данных о погоде каждый город в отдельный файл
    public void saveCityInfoToFile() throws JSONException {
        for (int i = 0; i < mListDataCity.size(); i++) {
            String nameFile = mListDataCity.get(i).getName();
            mListDataCity.get(i).saveToFile(nameFile + ".txt", getApplicationContext());
        }
    }

    // сохранение списка названий городов
    public void saveListCity() {
        String stringCityName = "";
        for (int i = 0; i < mListDataCity.size(); i++) {
            stringCityName += mListDataCity.get(i).getName() + ",";
        }
        saveCityParameters("city", stringCityName);
    }

    // загрузка списка названий городов
    public void loadListCity() {
        String stringCityName = "";
        stringCityName = loadCityParameters("city");
        String stringListCityNames[] = stringCityName.split(",");
        if (!stringCityName.trim().equals("")) {
            mListDataCity.clear();
            for (int i = 0; i < stringListCityNames.length; i++) {
                mListDataCity.add(i, new CityModel(stringListCityNames[i]));
            }
        }
        if (mListDataCity.size() == 0) {
            mListDataCity.add(new CityModel("Kazan", getMY_APPID()));
            mListDataCity.add(new CityModel("Moscow", getMY_APPID()));
            mListDataCity.add(new CityModel("Samara", getMY_APPID()));
            mListDataCity.add(new CityModel("Istanbul", getMY_APPID()));
            mListDataCity.add(new CityModel("London", getMY_APPID()));
            Toast.makeText(getApplicationContext(), R.string.strDwnloadCityDefault, Toast.LENGTH_SHORT);
        }
        mNameCity.invalidateViews();
    }

    //сохранение параметра  в файл параметров
    public void saveCityParameters(String parameters, String values) {
        mSPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = mSPref.edit();
        ed.putString(parameters, values);
        ed.apply();
    }

    // загрузка списка городов из файл параметров
    public String loadCityParameters(String parameters) {
        String resSet = "";
        mSPref = getPreferences(MODE_PRIVATE);
        resSet = mSPref.getString(parameters, "");
        return resSet;
    }


}
