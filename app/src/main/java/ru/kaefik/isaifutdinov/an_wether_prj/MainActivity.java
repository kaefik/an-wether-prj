package ru.kaefik.isaifutdinov.an_wether_prj;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                Intent intent = new Intent(getApplicationContext(), cityInfoActivity.class);
                // передаем данные выбранного города в activity для отображения полученной информации
                intent.putExtra("name", adapter.getCityModel(position).getName());
                intent.putExtra("id", adapter.getCityModel(position).getId());
                intent.putExtra("country", adapter.getCityModel(position).getCountry());
                intent.putExtra("temp", adapter.getCityModel(position).getTemp());
                intent.putExtra("clouds", adapter.getCityModel(position).getClouds());
                intent.putExtra("pressure", adapter.getCityModel(position).getPressure());
                intent.putExtra("windspeed", adapter.getCityModel(position).getWindspeed());
                intent.putExtra("winddirection", adapter.getCityModel(position).getWinddirection());
                intent.putExtra("huminidity", adapter.getCityModel(position).getHuminidity());
                intent.putExtra("timeRefresh", adapter.getCityModel(position).getTimeRefresh());


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

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("dfdsfsd");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.REQUEST_CODE_CITY_WEATHER:
                    CityModel tmpCityData = new CityModel();

                    tmpCityData.setName(data.getStringExtra("name"));
                    tmpCityData.setId(data.getLongExtra("id", 0));  ///dsfsdfdsfdsfdsfsdf
                    tmpCityData.setCountry(data.getStringExtra("country"));
                    tmpCityData.setTemp(data.getFloatExtra("temp", 0.0f));
                    tmpCityData.setClouds(data.getFloatExtra("clouds", 0.0f));
                    tmpCityData.setPressure(data.getFloatExtra("pressure", 0.0f));
                    tmpCityData.setHuminidity(data.getFloatExtra("huminidity", 0.0f));
                    tmpCityData.setWindspeed(data.getFloatExtra("windspeed", 0.0f));
                    tmpCityData.setWinddirection(data.getFloatExtra("winddirection", 0.0f));
//                    tmpCityData.setTimeRefresh(Date.pa(data.getStringExtra("timeRefresh")));

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
    }

// добавления нового города
    public  void onClickAddCity(View v){

    }

    @Override
    public void onBackPressed() {
        openQuitDialog();

/        }
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
                                Toast.makeText(getApplicationContext(),"Спасибо что решили с нами остаться",Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("Выйти ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.super.onBackPressed();                        }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }



//
//        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
//                this);
//        quitDialog.setTitle("Выход: Вы уверены?");
//
//        quitDialog.setPositiveButton("Таки да!", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//                finish();
//            }
//        });
//
//        quitDialog.setNegativeButton("Нет", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        quitDialog.show();
//    }

}
