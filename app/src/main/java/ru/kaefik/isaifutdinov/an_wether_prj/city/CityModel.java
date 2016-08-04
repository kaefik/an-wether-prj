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

    private String mMYAPPID; // уникальный ключ для доступа к сервису OpenWeatherMap

    private long mId;
    private String mCountry; // страна
    private String mName; // название города
    private float mTemp; // температура
    private float mClouds; // облачность в %
    private float mHuminidity; // влажность
    private float mPressure; // давление
    private float mWindspeed; // скорость ветра
    private float mWinddirection; // направление ветра
    private String mTimeRefresh; // время обновления прогноза погоды
    private Map mWeather; // описание и иконка погоды

    public CityModel(long id, String country, String name, float temp, float clouds, float huminidity, float pressure, float windspeed, float winddirection, int i) {
        this.mId = id;
        this.mCountry = country;
        this.mName = name;
        this.mTemp = temp;
        this.mClouds = clouds;
        this.mHuminidity = huminidity;
        this.mPressure = pressure;
        this.mWindspeed = windspeed;
        this.mWinddirection = winddirection;
        this.mTimeRefresh = "";
        this.mWeather = new HashMap<String, String>();
        this.mWeather.put("id", "");
        this.mWeather.put("icon", "");
        this.mWeather.put("description", "");
        this.mWeather.put("main", "");
        setMYAPPID("");
    }

    public CityModel(String name) {
        this.mName = name;
        this.mId = 0;
        this.mCountry = "";
        this.mTemp = 0.00f;
        this.mClouds = 0.00f;
        this.mHuminidity = 0.00f;
        this.mPressure = 0.00f;
        this.mWindspeed = 0.00f;
        this.mWinddirection = 0.00f;
        this.mTimeRefresh = "";
        this.mWeather = new HashMap<String, String>();
        this.mWeather.put("id", "");
        this.mWeather.put("icon", "");
        this.mWeather.put("description", "");
        this.mWeather.put("main", "");
        setMYAPPID("");
    }

    public CityModel(String name, String appid) {
        this.mName = name;
        this.mId = 0;
        this.mCountry = "";
        this.mTemp = 0.00f;
        this.mClouds = 0.00f;
        this.mHuminidity = 0.00f;
        this.mPressure = 0.00f;
        this.mWindspeed = 0.00f;
        this.mWinddirection = 0.00f;
        this.mTimeRefresh = "";
        this.mWeather = new HashMap<String, String>();
        this.mWeather.put("id", "");
        this.mWeather.put("icon", "");
        this.mWeather.put("description", "");
        this.mWeather.put("main", "");
        setMYAPPID(appid);
    }

    public CityModel() {
        this.mName = "";
        this.mId = 0;
        this.mCountry = "";
        this.mTemp = 0.00f;
        this.mClouds = 0.00f;
        this.mHuminidity = 0.00f;
        this.mPressure = 0.00f;
        this.mWindspeed = 0.00f;
        this.mWinddirection = 0.00f;
        this.mTimeRefresh = "";
        this.mWeather = new HashMap<String, String>();
        this.mWeather.put("id", "");
        this.mWeather.put("icon", "");
        this.mWeather.put("description", "");
        this.mWeather.put("main", "");
        setMYAPPID("");
    }

    // копирование данных json jo в объект класса CityModel
    public CityModel(JSONObject jo) throws JSONException, ParseException {
        this.mName = jo.get("name").toString();
        this.mId = Long.parseLong(jo.get("id").toString());
        this.mCountry = jo.get("country").toString();
        this.mTemp = Float.parseFloat(jo.get("temp").toString());
        this.mClouds = Float.parseFloat(jo.get("clouds").toString());
        this.mHuminidity = Float.parseFloat(jo.get("huminidity").toString());
        this.mPressure = Float.parseFloat(jo.get("pressure").toString());
        this.mWindspeed = Float.parseFloat(jo.get("windspeed").toString());
        this.mWinddirection = Float.parseFloat(jo.get("winddirection").toString());
        this.mTimeRefresh = jo.get("timeRefresh").toString();
        this.mWeather = new HashMap<String, String>();
        this.mWeather.put("id", jo.get("weather-id").toString());
        this.mWeather.put("icon", jo.get("weather-icon").toString());
        this.mWeather.put("description", jo.get("weather-description").toString());
        this.mWeather.put("main", jo.get("weather-main").toString());
        setMYAPPID(jo.get("appid").toString());
    }

    // копирование объекта obj в текущий
    public void CityModel(CityModel obj) {
        this.mName = obj.getName();
        this.mId = obj.getId();
        this.mCountry = obj.getCountry();
        this.mTemp = obj.getTemp();
        this.mClouds = obj.getClouds();
        this.mHuminidity = obj.getHuminidity();
        this.mPressure = obj.getPressure();
        this.mWindspeed = obj.getWindspeed();
        this.mWinddirection = obj.getWinddirection();
        this.mTimeRefresh = obj.getTimeRefresh();
        this.mWeather = obj.getWeather();
        setMYAPPID(obj.getMYAPPID());
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
        jo.put("weather-main", getWeather("main"));
        jo.put("appid", getMYAPPID());
        return jo;
    }

    // обновить время обновления текущей датой
    public void setTimeRefresh() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.mTimeRefresh = df.format(new Date());
    }

    public void setTimeRefresh(String timeRefresh) {
        this.mTimeRefresh = timeRefresh;
    }

    public String getTimeRefresh() {
        return mTimeRefresh;
    }

    // получение данных с погоды
    public void getHttpWeather() throws ParseException {
        //api.openweathermap.org/data/2.5/mWeather?q=London&APPID=9a4be4eeb7de3b88211989559a0849f7
        //        {"coord":{"lon":49.12,"lat":55.79},
        //         "mWeather":[{"mId":800,"main":"Clear","description":"clear sky","icon":"01n"}],
        //         "base":"cmc stations","main":{"mTemp":293.9,"mPressure":1015,"humidity":52,"temp_min":293.71,"temp_max":294.15},
        //         "wind":{"speed":3,"deg":50},"mClouds":{"all":0},"dt":1469468475,
        //         "sys":{"type":1,"mId":7335,"message":0.0089,"mCountry":"RU","sunrise":1469407055,"sunset":1469466078},
        //         "mId":551487,"mName":"Kazan","cod":200}

        String res = getHttpRequestFromUrl("http://api.openweathermap.org/data/2.5/weather?q=" + getName() + "&units=metric&APPID=" + getMYAPPID());
        if (res == null) {
            // сгенерировать исключение чтобы на верхнем уровне обработать ее
            res="";
            System.out.println("Ошибка при обновлении данных");
        } else {
            if (getObjFromJson(res, "name", null).equals(this.mName)) {
                setTemp(Float.parseFloat(getObjFromJson(res, "main", "temp")));
                setPressure(Float.parseFloat(getObjFromJson(res, "main", "pressure")));
                setHuminidity(Float.parseFloat(getObjFromJson(res, "main", "humidity")));
                setWindspeed(Float.parseFloat(getObjFromJson(res, "wind", "speed")));
                setWinddirection(Float.parseFloat(getObjFromJson(res, "wind", "deg")));
                setCountry(getObjFromJson(res, "sys", "country"));
                setId(Long.parseLong(getObjFromJson(res, "id", null)));
                String ss = getObjFromJson(res, "weather", null);
            //  "mWeather":[{"mId":800,"main":"Clear","description":"clear sky","icon":"01n"}]
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
        return mWeather;
    }

    //  получение содержимого по ключу index
    public String getWeather(String index) {
        return mWeather.get(index).toString();
    }

    public void setWeather(String index, String value) {
        this.mWeather.put(index, value);
    }

    public void setMYAPPID(String MYAPPID) {
        this.mMYAPPID = MYAPPID;
    }

    public String getMYAPPID() {

        return this.mMYAPPID;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setTemp(float temp) {
        this.mTemp = temp;
    }

    public void setClouds(float clouds) {
        this.mClouds = clouds;
    }

    public void setHuminidity(float huminidity) {
        this.mHuminidity = huminidity;
    }

    public void setPressure(float pressure) {
        this.mPressure = pressure;
    }

    public void setWindspeed(float windspeed) {
        this.mWindspeed = windspeed;
    }

    public void setWinddirection(float winddirection) {
        this.mWinddirection = winddirection;
    }

    public long getId() {
        return mId;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getName() {
        return mName;
    }

    public float getTemp() {
        return mTemp;
    }

    public float getClouds() {
        return mClouds;
    }

    public float getHuminidity() {
        return mHuminidity;
    }

    public float getPressure() {
        return mPressure;
    }

    public float getWindspeed() {
        return mWindspeed;
    }

    public float getWinddirection() {
        return mWinddirection;
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
        intent.putExtra("appid", getMYAPPID());
        //  передача данных параметра mWeather
        intent.putExtra("weather-id", getWeather("id"));
        intent.putExtra("weather-icon", getWeather("icon"));
        intent.putExtra("weather-description", getWeather("description"));
        intent.putExtra("weather-main", getWeather("main"));
        return intent;
    }


    // реализация получение данных через intent
    public void getExtraIntent(Intent intent) throws ParseException {
        setName(intent.getStringExtra("name"));
        setId(intent.getLongExtra("id", 0));
        setCountry(intent.getStringExtra("country"));
        setTemp(intent.getFloatExtra("temp", 0.0f));
        setClouds(intent.getFloatExtra("clouds", 0.0f));
        setPressure(intent.getFloatExtra("pressure", 0.0f));
        setHuminidity(intent.getFloatExtra("huminidity", 0.0f));
        setWindspeed(intent.getFloatExtra("windspeed", 0.0f));
        setWinddirection(intent.getFloatExtra("winddirection", 0.0f));
        setTimeRefresh(intent.getStringExtra("timeRefresh"));
        //  получение данных параметра mWeather
        setWeather("mId", intent.getStringExtra("weather-id"));
        setWeather("icon", intent.getStringExtra("weather-icon"));
        setWeather("description", intent.getStringExtra("weather-description"));
        setWeather("main", intent.getStringExtra("weather-main"));
        setMYAPPID(intent.getStringExtra("appid"));
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
