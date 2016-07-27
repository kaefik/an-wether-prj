package ru.kaefik.isaifutdinov.an_wether_prj;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    List<CityModel> listDataCity; // данные прогноза погоды

    private cityInfoAsyncTask task;

    // проработать этот AsyncTask применимо к списку городов
    class cityInfoAsyncTask extends AsyncTask<List<CityModel>, CityModel, List<CityModel>> {
        @Override
        protected List<CityModel> doInBackground(List<CityModel>... listcityModels) {
//            CityModel cityModel = listcityModels[0].get(0);

            for (int i = 0; i < listcityModels[0].size(); i++) {
                listcityModels[0].get(i).getHttpWeather();
                publishProgress(listcityModels[0].get(i));
            }


            return listcityModels[0];
        }

        @Override
        protected void onProgressUpdate(CityModel... values) {
            super.onProgressUpdate(values);
            nameCity.invalidateViews();
            MainActivity.this.setProgressBarIndeterminateVisibility(false);
//            refreshData2View(cityModel);

        }

        @Override
        protected void onPostExecute(List<CityModel> values) {
            super.onPostExecute(values);


        }
    }



    public void onClickRefresh(View v) throws JSONException {
        JSONObject jo = listDataCity.get(0).toJSON();
        System.out.println("JSONObject :: "+jo);
        CityModel tmpCityModel = new CityModel(jo);
        System.out.println("CityModel :: "+tmpCityModel);

    }


    public String getMY_APPID() {
        return MY_APPID;
    }

    public void setMY_APPID(String MY_APPID) {

        this.MY_APPID = MY_APPID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); // добавления прогресс бара на заголовок activity
        setContentView(R.layout.activity_main);

        nameCity = (ListView) findViewById(R.id.listView);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        editTextAddNewCity = (EditText) findViewById(R.id.editTextAddNewCity);
        setMY_APPID("9a4be4eeb7de3b88211989559a0849f7");
        if (listDataCity == null) {
            listDataCity = new ArrayList<CityModel>();
            listDataCity = initDataCity();
        }

        final CityModelAdapter adapter = new CityModelAdapter(this, listDataCity);
        nameCity.setAdapter(adapter);

        // Обработка события на клик по элементу списка
        nameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CityModel tmpCityModel = adapter.getCityModel(position);
                startActivityForResult(tmpCityModel.putExtraIntent(getApplicationContext(), cityInfoActivity.class), RequestCode.REQUEST_CODE_CITY_WEATHER);

            }
        });

   }

    @Override
    protected void onStart() {
        super.onStart();
        startcityInfoAsyncTask(listDataCity);
    }

    public void startcityInfoAsyncTask(List<CityModel> listCity){
        MainActivity.this.setProgressBarIndeterminateVisibility(true);
        task = new cityInfoAsyncTask();
        task.execute(listDataCity);
        System.out.println("");
    }



    // инициализация данных для списка городов
    private List<CityModel> initDataCity() {
        List<CityModel> listDataCity = new ArrayList<CityModel>();

        listDataCity.add(new CityModel("Kazan"));
        listDataCity.add(new CityModel("Moscow"));
        listDataCity.add(new CityModel("Samara"));
        listDataCity.add(new CityModel("Istanbul"));
        listDataCity.add(new CityModel("London"));
        listDataCity.add(new CityModel("L"));


        return listDataCity;
    }

     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.REQUEST_CODE_CITY_WEATHER:
                    CityModel tmpCityData = new CityModel();
                    tmpCityData.getExtraIntent(intent);

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
            Toast.makeText(this, "Error onActivityResult (закончился лимит или нет интернета) ", Toast.LENGTH_SHORT).show();
        }
        nameCity.invalidateViews();
    }

    // добавления нового города
    public void onClickAddCity(View v) {
        String newCity = Utils.firstUpCaseString(editTextAddNewCity.getText().toString().trim());
        // СЮДА ДОБАВИТЬ ПРОВЕРКИ ВВОДА НАЗВАНИЯ ГОРОДА
        if ((!newCity.equals("")) && (!isExistNameFromList(listDataCity,newCity))) {
            listDataCity.add(new CityModel(newCity));
            Toast.makeText(getApplicationContext(), getString(R.string.txtaddcityedit) + newCity, Toast.LENGTH_SHORT).show();
        }
        editTextAddNewCity.setText("");
        // прячем клавиатуру
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextAddNewCity.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
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
    public boolean isExistNameFromList(List<CityModel> listcity, String namecity){
        boolean flag = false;
        for(int i=0;i<listcity.size();i++){
            if (listcity.get(i).getName().equals(namecity)){
                flag = true;
                break;
            }
        }
        return flag;

    }

}
