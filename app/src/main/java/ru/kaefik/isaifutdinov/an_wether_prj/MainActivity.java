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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

    private ListView nameCity;
    private TextView myText;
    private EditText editTextAddNewCity;
    private Button btnRefresh;
    private String MY_APPID; // уникальный ключ для доступа к сервису OpenWeatherMap
    private SharedPreferences sPref;
    List<CityModel> listDataCity; // данные прогноза погоды

    private cityInfoAsyncTask task;


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
        protected void onProgressUpdate(CityModel... values) {
            super.onProgressUpdate(values);
            nameCity.invalidateViews();
            MainActivity.this.setProgressBarIndeterminateVisibility(false);
        }

        @Override
        protected void onPostExecute(List<CityModel> values) {
            super.onPostExecute(values);
            try {
                saveCityInfoToFile();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            saveListCity();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); // добавления прогресс бара на заголовок activity
        setContentView(R.layout.activity_main);

        nameCity = (ListView) findViewById(R.id.listView);
        editTextAddNewCity = (EditText) findViewById(R.id.editTextAddNewCity);
        setMY_APPID("9a4be4eeb7de3b88211989559a0849f7");
        if (listDataCity == null) {
            listDataCity = new ArrayList<CityModel>();
            loadListCity();
        }

        final CityModelAdapter adapter = new CityModelAdapter(this, listDataCity);
        nameCity.setAdapter(adapter);


        nameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Обработка события на клик по элементу списка
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityModel tmpCityModel = adapter.getCityModel(position);
                try {
                    startActivityForResult(tmpCityModel.putExtraIntent(getApplicationContext(), cityInfoActivity.class), RequestCode.REQUEST_CODE_CITY_WEATHER);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        nameCity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // Обработка долгого нажатия на элемент
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //removeCityDialog(parent, position);
                final CityModel selectedItem = (CityModel) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Удаление элемента")
                        .setMessage("Точно хотите удалить город " + selectedItem.getName() + "?")
                        .setCancelable(false)
                        .setPositiveButton("Удалить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        listDataCity.remove(position);
                                        nameCity.invalidateViews();
                                        saveListCity();
                                        Toast.makeText(getApplicationContext(), getString(R.string.strgorod) + "  " + selectedItem.getName() + " удалён.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("Оставить",
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

        startcityInfoAsyncTask(listDataCity);
    }


    // ручное обновление погоды в списке
    public void onClickRefreshCityInfo(View v) throws JSONException {
        try {
            startcityInfoAsyncTask(listDataCity);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Ошибка при обновлении данных", Toast.LENGTH_SHORT);
        }

    }


    // ----- задействовать данный параметр
    public String getMY_APPID() {
        return MY_APPID;
    }

    public void setMY_APPID(String MY_APPID) {

        this.MY_APPID = MY_APPID;
    }
    // ----- END задействовать данный параметр


    // запуск задание cityInfoAsyncTask на обновления информации списка городов
    public void startcityInfoAsyncTask(List<CityModel> listCity) {
        MainActivity.this.setProgressBarIndeterminateVisibility(true);
        task = new cityInfoAsyncTask();
        task.execute(listDataCity);

        try {
            task.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Toast.makeText(this,"Ошибка обновления данных погоды",Toast.LENGTH_SHORT);
        } catch (ExecutionException e) {
            Toast.makeText(this,"Ошибка обновления данных погоды",Toast.LENGTH_SHORT);
        } catch (TimeoutException e) {
            Toast.makeText(this,"Ошибка обновления данных погоды",Toast.LENGTH_SHORT);
        }
        saveListCity();

    }


    @Override
    // прием данных CityModel выбраного города из другое активити
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.REQUEST_CODE_CITY_WEATHER:
                    CityModel tmpCityData = new CityModel();
                    try {
                        tmpCityData.getExtraIntent(intent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // СЮДА ДОБАВИТЬ ОБНОВЛЕНИЕ ИНФОРМАЦИИ О ГОРОДЕ tmpCityData.getName() в listDataCity
                    for (int i = 0; i < listDataCity.size(); i++) {
                        if (listDataCity.get(i).getName().equals(tmpCityData.getName())) {
                            listDataCity.get(i).setTemp(tmpCityData.getTemp());
                            listDataCity.get(i).setId(tmpCityData.getId());
                            listDataCity.get(i).setCountry(tmpCityData.getCountry());
                            listDataCity.get(i).setClouds(tmpCityData.getClouds());
                            listDataCity.get(i).setPressure(tmpCityData.getPressure());
                            listDataCity.get(i).setHuminidity(tmpCityData.getHuminidity());
                            listDataCity.get(i).setWinddirection(tmpCityData.getWinddirection());
                            listDataCity.get(i).setWindspeed(tmpCityData.getWindspeed());
                            listDataCity.get(i).setTimeRefresh(tmpCityData.getTimeRefresh());

                        }
                    }
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка при обновлении данных", Toast.LENGTH_SHORT);
        }
        nameCity.invalidateViews();
    }

    // добавления нового города
    public void onClickAddCity(View v) {
        String newCity = Utils.firstUpCaseString(editTextAddNewCity.getText().toString().trim());
        // СЮДА ДОБАВИТЬ ПРОВЕРКИ ВВОДА НАЗВАНИЯ ГОРОДА
        if ((!newCity.equals("")) && (!isExistNameFromList(listDataCity, newCity))) {
            listDataCity.add(new CityModel(newCity));
            Toast.makeText(getApplicationContext(), getString(R.string.txtaddcityedit) + newCity, Toast.LENGTH_SHORT).show();
        }
        editTextAddNewCity.setText("");
        // прячем клавиатуру
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextAddNewCity.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        startcityInfoAsyncTask(listDataCity);
        saveListCity();
    }

    @Override
    public void onBackPressed() {  // обработка нажатия кнопки Назад
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Ого! Важное сообщение!")
                .setMessage("Точно хотите выйти?!")
//                .setIcon(R.drawable.ic_android_cat)
                .setCancelable(false)
                .setPositiveButton("Остаться",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(), "Спасибо что решили с нами остаться", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("Выйти ",
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

    // восстановление сохраненых данных о погоде (каждый город - отдельный файл сJosn)
    public void restoreCityInfoFromFile() throws JSONException {
//        List<CityModel> tmplistDataCity = new ArrayList<CityModel>();
        Boolean flagExistFile = true;
        for (int i = 0; i < listDataCity.size(); i++) {
            String nameFile = listDataCity.get(i).getName();
//            tmplistDataCity.add(new CityModel(nameFile));
            flagExistFile = listDataCity.get(i).openFile(nameFile + ".txt", getApplicationContext());
//            if (flagExistFile) {
//                listDataCity.set(i, tmplistDataCity.get(i));
//            }
        }
        nameCity.invalidateViews();
    }

    //сохранение данных о погоде каждый город в отдельный файл
    public void saveCityInfoToFile() throws JSONException {
        for (int i = 0; i < listDataCity.size(); i++) {
            String nameFile = listDataCity.get(i).getName();
            listDataCity.get(i).saveToFile(nameFile + ".txt", getApplicationContext());
        }
    }

    // сохранение списка названий городов
    public void saveListCity() {
        String stringCityName = "";
        for (int i = 0; i < listDataCity.size(); i++) {
            stringCityName += listDataCity.get(i).getName() + ",";
        }
        saveCityParameters("city", stringCityName);
    }

    // загрузка списка названий городов
    public void loadListCity() {
        String stringCityName = "";
        stringCityName = loadCityParameters("city");
        String stringListCityNames[] = stringCityName.split(",");
        if (!stringCityName.trim().equals("")) {
            listDataCity.clear();
            for (int i = 0; i < stringListCityNames.length; i++) {
                listDataCity.add(i, new CityModel(stringListCityNames[i]));
            }
        }

        if (listDataCity.size() == 0) {
            listDataCity.add(new CityModel("Kazan"));
            listDataCity.add(new CityModel("Moscow"));
            listDataCity.add(new CityModel("Samara"));
            listDataCity.add(new CityModel("Istanbul"));
            listDataCity.add(new CityModel("London"));
            Toast.makeText(getApplicationContext(), "Загрузка городов по умолчанию.", Toast.LENGTH_SHORT);
        }
        nameCity.invalidateViews();
    }

    //сохранение параметра  в файл параметров
    public void saveCityParameters(String parameters, String values) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(parameters, values);
        ed.apply();
    }

    // загрузка списка городов из файл параметров
    public String loadCityParameters(String parameters) {
        String resSet = "";
        sPref = getPreferences(MODE_PRIVATE);
        resSet = sPref.getString(parameters, "");
        return resSet;
    }


    // тестовая кнопка для отработки различных сценариев
    public void onClickSave(View v) {
        saveListCity();
    }
}
