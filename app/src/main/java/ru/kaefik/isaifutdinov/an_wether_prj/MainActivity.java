package ru.kaefik.isaifutdinov.an_wether_prj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.kaefik.isaifutdinov.an_wether_prj.adapter.CityModelAdapter;
import ru.kaefik.isaifutdinov.an_wether_prj.city.CityModel;

public class MainActivity extends AppCompatActivity {

    private ListView nameCity;
    private TextView myText;
    private String MY_APPID;


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



        CityModelAdapter adapter = new CityModelAdapter(this, initDataCity());

        nameCity.setAdapter(adapter);

//        // Обработка события на клик по элементу списка
//        nameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                myText.setText( "itemClick: position = " + position + ", id = " + id );
//            }
//        });

    }

    // инициализация данных для списка городов
    private List<CityModel> initDataCity() {
        List<CityModel> listdata = new ArrayList<CityModel>();

        listdata.add(new CityModel(1, "ru", "Kazan", 0, 0, 0, 0, 0, 0, 0));
        listdata.add(new CityModel(2, "ru", "Moscow", 0, 0, 0, 0, 0, 0, 0));
        listdata.add(new CityModel(3, "ru", "Samara", 0, 0, 0, 0, 0, 0, 0));
        listdata.add(new CityModel(4, "tr", "Istanbul", 0, 0, 0, 0, 0, 0, 0));
        listdata.add(new CityModel(5, "gb", "City of London", 0, 0, 0, 0, 0, 0, 0));

        return listdata;
    }

    // ?????
    public void clickItemCity(View view, int position, long id) {
        myText.setText("itemClick: position = " + position + ", id = " + id);
    }

//    // получить текущую погоду из OpenWeatherMap используя id города
//    public CityModel getCurrentWeather(long id) {
//        CityModel res = new CityModel(id, "", "", 0, 0, 0, 0, 0, 0, 0);
//        String strid = "524901";
//        String strurl = "http://api.openweathermap.org/data/2.5/forecast/city?id=" + strid + "&APPID="+getMY_APPID();
////        myText.setText(getHTML(strurl));
//        return res;
//    }

    public void getInfoWeatherCity(View view) throws Exception {

        getHttpWeather();
    }

    public void getHttpWeather()  {

        //api.openweathermap.org/data/2.5/weather?q=London&APPID=9a4be4eeb7de3b88211989559a0849f7

        new Thread(new Runnable() {
            @Override
            public void run() {

                String res = getHttpRequestFromUrl("http://api.openweathermap.org/data/2.5/weather?q=Kazan&APPID="+getMY_APPID());
                if (res == null){
                    myText.setText("Ошибка загрузки");
                } else {
                    System.out.println(res);
                    System.out.println(getObjFromJson(res,"main","temp"));
                    System.out.println(getObjFromJson(res,"main","pressure"));
                    System.out.println(getObjFromJson(res,"main","humidity"));
                    System.out.println(getObjFromJson(res,"wind","speed"));
                    System.out.println(getObjFromJson(res,"wind","deg"));
//                    System.out.println(getObjFromJson(res,"weather","description")); // clear sky
                    System.out.println(getObjFromJson(res,"sys","country"));
                    System.out.println(getObjFromJson(res,"name",null));
                    System.out.println(getObjFromJson(res,"id",null));


                }

            }
        }).start();
    }

    // получение объектов из json
    public String getObjFromJson(String sjosn,String nameParrent, String nameChild){

        JSONObject parentObject = null;
        JSONObject childObject = null;
        String res=null;
        try {
            parentObject = new JSONObject(sjosn);
            if (nameParrent != null) {
                res=parentObject.get(nameParrent).toString();
                if (nameChild != null){
                    if (res!=null) {
                        childObject = new JSONObject(res);
                        res = childObject.get(nameChild).toString();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  res;
    }

    // получение страницы из урла strurl
    public String getHttpRequestFromUrl(String strurl){
        String resultStr=null;
        try {
            URL url = new URL(strurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            InputStream stream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            resultStr="";
            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
                resultStr+=line;
            }
            bufferedReader.close();
            return resultStr;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

}
