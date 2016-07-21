package ru.kaefik.isaifutdinov.an_wether_prj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.kaefik.isaifutdinov.an_wether_prj.adapter.CityModelAdapter;
import ru.kaefik.isaifutdinov.an_wether_prj.city.CityModel;
import ru.kaefik.isaifutdinov.an_wether_prj.utils.RequestCode;

public class MainActivity extends AppCompatActivity {

    private ListView nameCity;
    private TextView myText;
    private String MY_APPID; // уникальный ключ для доступа к сервису OpenWeatherMap
    List<CityModel> listDataCity; // данные прогноза погоды


    public String getMY_APPID() {
        return MY_APPID;
    }

    public void setMY_APPID(String MY_APPID) {

        this.MY_APPID = MY_APPID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameCity = (ListView) findViewById(R.id.listView);
        myText = (TextView) findViewById(R.id.textView);
        setMY_APPID("9a4be4eeb7de3b88211989559a0849f7");
        listDataCity = new ArrayList<CityModel>();
        listDataCity = initDataCity();

        final CityModelAdapter adapter = new CityModelAdapter(this, initDataCity());
        nameCity.setAdapter(adapter);

        // Обработка события на клик по элементу списка
        nameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myText.setText("itemClick: position = " + position + ", id = " + id);
                Intent intent = new Intent(getApplicationContext(), cityInfoActivity.class);
                // передаем данные выбранного города в activity для отображения полученной информации
                intent.putExtra("name", adapter.getCityModel(position).getName());

//                startActivity(intent);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_CITY_WEATHER);
            }
        });

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

    // ?????
    public void clickItemCity(View view, int position, long id) {
        myText.setText("itemClick: position = " + position + ", id = " + id);
    }

//    public void getInfoWeatherCity(View view) throws Exception {
//
//        Intent intent = new Intent(this, cityInfoActivity.class);
//        // передаем данные выбранного города в activity для отображения полученной информации
//        intent.putExtra("name", "Kazan");
//        startActivity(intent);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case RequestCode.REQUEST_CODE_CITY_WEATHER:
                    CityModel tmpCityData = new CityModel();

                    tmpCityData.setName(data.getStringExtra("name"));
                    tmpCityData.setId(data.getLongExtra("id",0));  ///dsfsdfdsfdsfdsfsdf
                    tmpCityData.setCountry(data.getStringExtra("country"));
                    tmpCityData.setTemp(data.getFloatExtra("temp",0.0f));
                    tmpCityData.setClouds(data.getFloatExtra("clouds",0.0f));
                    tmpCityData.setPressure(data.getFloatExtra("pressure",0.0f));
                    tmpCityData.setWindspeed(data.getFloatExtra("windspeed",0.0f));
                    tmpCityData.setWinddirection(data.getFloatExtra("winddirection",0.0f));
//                    tmpCityData.setTimeRefresh(Date.pa(data.getStringExtra("timeRefresh")));


                    // СЮДА ДОБАВИТЬ ОБНОВЛЕНИЕ ИНФОРМАЦИИ О ГОРОДЕ tmpCityData.getName() в listDataCity

                    System.out.println("Name City: "+tmpCityData.getName());
                    System.out.println("Temp City: "+tmpCityData.getTemp());
                    break;
            }
        }else {
            Toast.makeText(this,"Error onActivityResult ",Toast.LENGTH_SHORT).show();
        }
    }
}
