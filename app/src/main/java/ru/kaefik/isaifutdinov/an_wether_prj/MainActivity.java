package ru.kaefik.isaifutdinov.an_wether_prj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.kaefik.isaifutdinov.an_wether_prj.adapter.CityModelAdapter;
import ru.kaefik.isaifutdinov.an_wether_prj.city.CityModel;

public class MainActivity extends AppCompatActivity {

    private ListView nameCity;
    private TextView myText;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameCity =  (ListView) findViewById(R.id.listView);
        myText =  (TextView) findViewById(R.id.textView);

        CityModelAdapter adapter = new CityModelAdapter(this,initDataCity());

        nameCity.setAdapter(adapter);

//        // Обработка события на клик по элементу списка
//        nameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                myText.setText( "itemClick: position = " + position + ", id = " + id );
//            }
//        });

    }

// инициализация данных для списка городов
    private List<CityModel> initDataCity(){
        List<CityModel> listdata = new ArrayList<CityModel>();

        listdata.add(new CityModel(1,"ru","Kazan",0,0,0,0,0,0,0));
        listdata.add(new CityModel(2,"ru","Moscow",0,0,0,0,0,0,0));


        return  listdata;
    }

}
