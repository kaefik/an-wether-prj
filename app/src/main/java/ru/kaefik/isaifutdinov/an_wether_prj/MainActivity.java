package ru.kaefik.isaifutdinov.an_wether_prj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
        listdata.add(new CityModel(3,"ru","Samara",0,0,0,0,0,0,0));
        listdata.add(new CityModel(4,"tr","Istanbul",0,0,0,0,0,0,0));
        listdata.add(new CityModel(5,"gb","City of London",0,0,0,0,0,0,0));

        return  listdata;
    }

// ?????
    public void clickItemCity(View view,int position,long id){
        myText.setText( "itemClick: position = " + position + ", id = " + id );
    }

    // получить текущую погоду из OpenWeatherMap используя id города
    public CityModel  getCurrentWeather(long id){
        CityModel res = new CityModel(id,"","",0,0,0,0,0,0,0);
        String strid ="524901";
        String strurl=  "http://api.openweathermap.org/data/2.5/forecast/city?id="+strid+"&APPID=9a4be4eeb7de3b88211989559a0849f7";
//        myText.setText(getHTML(strurl));
        return  res;
    }

    public void getInfoWeatherCity(View view) throws Exception {

        getHttpWeather();

//        DefaultHttpClient hc = new DefaultHttpClient();
//        ResponseHandler response = new BasicResponseHandler();
//        HttpGet http = new HttpGet("http://api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=9a4be4eeb7de3b88211989559a0849f7");
//        //получаем ответ от сервера
//        String response = (String) hc.execute(http, response);
//        myText.setText(response);

//        CityModel currentWeather = getCurrentWeather(524901);

//        Intent intent = new Intent(this,cityInfoActivity.class);
//// передаем данные выбранного города в activity для отображения полученной информации
//        intent.putExtra("name","Казань");
//        intent.putExtra("temp","температура");
//        intent.putExtra("clouds","облачность");
//        intent.putExtra("huminidity","влажность");
//        intent.putExtra("pressure","давление");
//        intent.putExtra("windspeed","ск-ть ветра");
//        intent.putExtra("winddirection","напр-е ветра");

//        startActivity(intent);
    }

    public void getHttpWeather() throws MalformedURLException {

//        String url=  "http://api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=9a4be4eeb7de3b88211989559a0849f7";

        HttpURLConnection connection=null;
        BufferedReader reader=null;
        URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=9a4be4eeb7de3b88211989559a0849f7");

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(stream));

            String line = "";
            while (((line = reader.readLine()) != null)) {
                buffer.append(line);
            }

            myText.setText(line);
        } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        myText.setText("Response: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//

    }







}
