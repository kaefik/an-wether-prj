package ru.kaefik.isaifutdinov.an_wether_prj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ru.kaefik.isaifutdinov.an_wether_prj.city.CityModel;

public class cityInfoActivity extends AppCompatActivity {

    TextView nameCity;
    TextView tempCity;
    TextView cloudsCity;
    TextView huminidityCity;
    TextView pressureCity;
    TextView windspeedCity;
    TextView winddirectionCity;


//    intent.putExtra("name","Казань");
//    intent.putExtra("temp","Казань");
//    intent.putExtra("clouds","Казань");
//    intent.putExtra("huminidity","Казань");
//    intent.putExtra("pressure","Казань");
//    intent.putExtra("windspeed","Казань");
//    intent.putExtra("winddirection","Казань");


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

        // получение отправленных данных и отображение данных
        nameCity.setText(getIntent().getStringExtra("name").toString());
        tempCity.setText(getIntent().getStringExtra("temp").toString());
        cloudsCity.setText(getIntent().getStringExtra("clouds").toString());
        huminidityCity.setText(getIntent().getStringExtra("huminidity").toString());
        pressureCity.setText(getIntent().getStringExtra("pressure").toString());
        windspeedCity.setText(getIntent().getStringExtra("windspeed").toString());
        winddirectionCity.setText(getIntent().getStringExtra("winddirection").toString());
    }

    public void goBackMainActivity(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void refreshDataWeather(View view){
        CityModel cc=new CityModel("London");
        cc.getHttpWeather();
    }

}
