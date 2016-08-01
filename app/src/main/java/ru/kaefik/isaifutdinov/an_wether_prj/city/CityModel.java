package ru.kaefik.isaifutdinov.an_wether_prj.city;


import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.kaefik.isaifutdinov.an_wether_prj.utils.Utils;

import static ru.kaefik.isaifutdinov.an_wether_prj.utils.Utils.getHttpRequestFromUrl;
import static ru.kaefik.isaifutdinov.an_wether_prj.utils.Utils.getObjFromJson;

public class CityModel {

    private String MY_APPID; // уникальный ключ для доступа к сервису OpenWeatherMap


    private long id;
    private String country; // страна
    private String name; // название города
    private float temp; // температура
    private float clouds; // облачность в %
    private float huminidity; // влажность
    private float pressure; // давление
    private float windspeed; // скорость ветра
    private float winddirection; // направление ветра
    private String timeRefresh; // время обновления прогноза погоды
    private Map weather; // описание и иконка погоды

    public CityModel(long id, String country, String name, float temp, float clouds, float huminidity, float pressure, float windspeed, float winddirection, int i) {
        this.id = id;
        this.country = country;
        this.name = name;
        this.temp = temp;
        this.clouds = clouds;
        this.huminidity = huminidity;
        this.pressure = pressure;
        this.windspeed = windspeed;
        this.winddirection = winddirection;
        this.timeRefresh = "";
        this.weather = new HashMap<String, String>();
        this.weather.put("id", "");
        this.weather.put("icon", "");
        this.weather.put("description", "");
        this.weather.put("main", "");

        setMY_APPID("9a4be4eeb7de3b88211989559a0849f7");
    }

    public CityModel(String name) {
        this.name = name;
        this.id = 0;
        this.country = "";
        this.temp = 0.00f;
        this.clouds = 0.00f;
        this.huminidity = 0.00f;
        this.pressure = 0.00f;
        this.windspeed = 0.00f;
        this.winddirection = 0.00f;
        this.timeRefresh = "";
        this.weather = new HashMap<String, String>();
        this.weather.put("id", "");
        this.weather.put("icon", "");
        this.weather.put("description", "");
        this.weather.put("main", "");

        setMY_APPID("9a4be4eeb7de3b88211989559a0849f7");
    }

    public CityModel() {
        this.name = "";
        this.id = 0;
        this.country = "";
        this.temp = 0.00f;
        this.clouds = 0.00f;
        this.huminidity = 0.00f;
        this.pressure = 0.00f;
        this.windspeed = 0.00f;
        this.winddirection = 0.00f;
        this.timeRefresh = "";
        this.weather = new HashMap<String, String>();
        this.weather.put("id", "");
        this.weather.put("icon", "");
        this.weather.put("description", "");
        this.weather.put("main", "");

        setMY_APPID("9a4be4eeb7de3b88211989559a0849f7");
    }

    public CityModel(JSONObject jo) throws JSONException, ParseException {
        this.name = jo.get("name").toString();
        this.id = Long.parseLong(jo.get("id").toString());
        this.country = jo.get("country").toString();
        this.temp = Float.parseFloat(jo.get("temp").toString());
        this.clouds = Float.parseFloat(jo.get("clouds").toString());
        this.huminidity = Float.parseFloat(jo.get("huminidity").toString());
        this.pressure = Float.parseFloat(jo.get("pressure").toString());
        this.windspeed = Float.parseFloat(jo.get("windspeed").toString());
        this.winddirection = Float.parseFloat(jo.get("winddirection").toString());
        this.timeRefresh = jo.get("timeRefresh").toString();

        this.weather = new HashMap<String, String>();
        this.weather.put("id",  jo.get("weather-id").toString());
        this.weather.put("icon", jo.get("weather-icon").toString());
        this.weather.put("description", jo.get("weather-description").toString()); /
        this.weather.put("main", jo.get("weather-main").toString());

        setMY_APPID("9a4be4eeb7de3b88211989559a0849f7");

    }

    // копирование объекта obj в текущий
    public void CityModel(CityModel obj) {
        this.name = obj.getName();
        this.id = obj.getId();
        this.country = obj.getCountry();
        this.temp = obj.getTemp();
        this.clouds = obj.getClouds();
        this.huminidity = obj.getHuminidity();
        this.pressure = obj.getPressure();
        this.windspeed = obj.getWindspeed();
        this.winddirection = obj.getWinddirection();
        this.timeRefresh = obj.getTimeRefresh();
        this.weather = obj.getWeather();

        setMY_APPID("9a4be4eeb7de3b88211989559a0849f7");
    }


    // преобразование объекта CityModel в Josn
    public JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", getName());
        jo.put("id", getId());
        jo.put("country", getCountry());
        jo.put("temp", getTemp());
        jo.put("clouds", getClouds());
        jo.put("huminidity", getHuminidity());
        jo.put("pressure", getPressure());
        jo.put("windspeed", getWindspeed());
        jo.put("winddirection", getWinddirection());
        jo.put("timeRefresh", getTimeRefresh());
        jo.put("weather-id", getWeather("id"));
        jo.put("weather-icon", getWeather("icon"));
        jo.put("weather-description", getWeather("description"));
        jo.put("weather-main",getWeather("main"));

        return jo;
    }

    // обновить время обновления текущей датой
    public void setTimeRefresh() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.timeRefresh = df.format(new Date());
    }

    public void setTimeRefresh(String timeRefresh) {
        this.timeRefresh = timeRefresh;
    }

    public String getTimeRefresh() {
        return timeRefresh;
    }

    // получение данных с погоды
    public void getHttpWeather() throws ParseException {
        //api.openweathermap.org/data/2.5/weather?q=London&APPID=9a4be4eeb7de3b88211989559a0849f7

//        {"coord":{"lon":49.12,"lat":55.79},
//         "weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01n"}],
//         "base":"cmc stations","main":{"temp":293.9,"pressure":1015,"humidity":52,"temp_min":293.71,"temp_max":294.15},
//         "wind":{"speed":3,"deg":50},"clouds":{"all":0},"dt":1469468475,
//         "sys":{"type":1,"id":7335,"message":0.0089,"country":"RU","sunrise":1469407055,"sunset":1469466078},
//         "id":551487,"name":"Kazan","cod":200}


        String res = getHttpRequestFromUrl("http://api.openweathermap.org/data/2.5/weather?q=" + getName() + "&units=metric&APPID=" + getMY_APPID());
        if (res == null) {
            System.out.println("Ошибка при обновлении данных");
        } else {
            if (getObjFromJson(res, "name", null).equals(this.name)) { // сделать парсинг параметра name)
                setTemp(Float.parseFloat(getObjFromJson(res, "main", "temp")));
                setPressure(Float.parseFloat(getObjFromJson(res, "main", "pressure")));
                setHuminidity(Float.parseFloat(getObjFromJson(res, "main", "humidity")));
                setWindspeed(Float.parseFloat(getObjFromJson(res, "wind", "speed")));
                setWinddirection(Float.parseFloat(getObjFromJson(res, "wind", "deg")));
                setCountry(getObjFromJson(res, "sys", "country"));
                setId(Long.parseLong(getObjFromJson(res, "id", null))); // сделать парсинг параметра id

                String ss = getObjFromJson(res, "weather", null);
//            "weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01n"}]
                String tmp1 = ss.substring(1, ss.length() - 1);
                setWeather("id", (getObjFromJson(tmp1, "id", null)));
                setWeather("main", (getObjFromJson(tmp1, "main", null)));
                setWeather("description", (getObjFromJson(tmp1, "description", null)));
                setWeather("icon", (getObjFromJson(tmp1, "icon", null)));
                setTimeRefresh();
            }
        }
    }

    public Map getWeather() {
        return weather;
    }

    //  получение содержимого по ключу index
    public String getWeather(String index) {
        return weather.get(index).toString();
    }

    public void setWeather(String index, String value) {
        this.weather.put(index, value);
    }

    public void setMY_APPID(String MY_APPID) {
        this.MY_APPID = MY_APPID;
    }

    public String getMY_APPID() {

        return MY_APPID;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public void setClouds(float clouds) {
        this.clouds = clouds;
    }

    public void setHuminidity(float huminidity) {
        this.huminidity = huminidity;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setWindspeed(float windspeed) {
        this.windspeed = windspeed;
    }

    public void setWinddirection(float winddirection) {
        this.winddirection = winddirection;
    }

    public long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public float getTemp() {
        return temp;
    }

    public float getClouds() {
        return clouds;
    }

    public float getHuminidity() {
        return huminidity;
    }

    public float getPressure() {
        return pressure;
    }

    public float getWindspeed() {
        return windspeed;
    }

    public float getWinddirection() {
        return winddirection;
    }


    // реализация передачи данных через intent
    public Intent putExtraIntent(Context context, Class<?> klass) throws ParseException {

        Intent intent = new Intent(context, klass);
        intent.putExtra("name", getName());
        intent.putExtra("id", getId());
        intent.putExtra("country", getCountry());
        intent.putExtra("temp", getTemp());
        intent.putExtra("clouds", getClouds());
        intent.putExtra("huminidity", getHuminidity());
        intent.putExtra("pressure", getPressure());
        intent.putExtra("windspeed", getWindspeed());
        intent.putExtra("winddirection", getWinddirection());
        intent.putExtra("timeRefresh", getTimeRefresh());

        //  передача данных параметра weather
        intent.putExtra("weather-id", getWeather("id"));
        intent.putExtra("weather-icon", getWeather("icon"));
        intent.putExtra("weather-description", getWeather("description"));
        intent.putExtra("weather-main", getWeather("main"));

        return intent;
    }


    // реализация получение данных через intent
    public void getExtraIntent(Intent intent) throws ParseException {
        setName(intent.getStringExtra("name"));
        setId(intent.getLongExtra("id", 0));  ///dsfsdfdsfdsfdsfsdf
        setCountry(intent.getStringExtra("country"));
        setTemp(intent.getFloatExtra("temp", 0.0f));
        setClouds(intent.getFloatExtra("clouds", 0.0f));
        setPressure(intent.getFloatExtra("pressure", 0.0f));
        setHuminidity(intent.getFloatExtra("huminidity", 0.0f));
        setWindspeed(intent.getFloatExtra("windspeed", 0.0f));
        setWinddirection(intent.getFloatExtra("winddirection", 0.0f));
        setTimeRefresh(intent.getStringExtra("timeRefresh"));

        //  получение данных параметра weather
        setWeather("id",intent.getStringExtra("weather-id"));
        setWeather("icon",intent.getStringExtra("weather-icon"));
        setWeather("description",intent.getStringExtra("weather-description"));
        setWeather("main",intent.getStringExtra("weather-main"));


//        }
    }



    // сохранить объект в файл nameFile в виде Josn
    public void saveToFile(String nameFile, Context context) throws JSONException {
        String strJo = this.toJSON().toString();
        Utils.saveFile(nameFile, strJo, context);
    }

    // открыть файл nameFile сохраненный в виде Josn и сохранить данные в объект, возвращает false если произошла ошибка открытия, иначе true
    public boolean openFile(String nameFile, Context context) {
        boolean flagStatus = true;
        try {
            JSONObject jo = new JSONObject(Utils.openFile(nameFile, context));
            this.CityModel(new CityModel(jo));
        } catch (JSONException e) {
            flagStatus = false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flagStatus;
    }

}
