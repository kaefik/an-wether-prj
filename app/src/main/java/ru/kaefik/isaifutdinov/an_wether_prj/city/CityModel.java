package ru.kaefik.isaifutdinov.an_wether_prj.city;


import java.util.Date;

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
    private Date timeRefresh; // время обновления прогноза погоды

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
        setMY_APPID("9a4be4eeb7de3b88211989559a0849f7");
    }

    // обновить время обновления текущей датой
    public void setTimeRefresh() {
//        DateFormat dateFormat = new DateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(date);
        this.timeRefresh = date;
    }

    public void setTimeRefresh(Date timeRefresh) {
        this.timeRefresh = timeRefresh;
    }

    public Date getTimeRefresh() {
        return timeRefresh;
    }

    // получение данных с погоды
    public void getHttpWeather() {
        //api.openweathermap.org/data/2.5/weather?q=London&APPID=9a4be4eeb7de3b88211989559a0849f7


        String res = getHttpRequestFromUrl("http://api.openweathermap.org/data/2.5/weather?q=" + getName() + "&APPID=" + getMY_APPID());
        if (res == null) {
            System.out.println("Ошибка загрузки");
        } else {
//                    System.out.println(res);
            setTemp(Float.parseFloat(getObjFromJson(res, "main", "temp")));
            setPressure(Float.parseFloat(getObjFromJson(res, "main", "pressure")));
            setHuminidity(Float.parseFloat(getObjFromJson(res, "main", "humidity")));
            setWindspeed(Float.parseFloat(getObjFromJson(res, "wind", "speed")));
            setWinddirection(Float.parseFloat(getObjFromJson(res, "wind", "deg")));
//                    System.out.println(getObjFromJson(res,"weather","description")); // clear sky
            setCountry(getObjFromJson(res, "sys", "country"));
            setName(getObjFromJson(res, "name", null)); // сделать парсинг параметра name
            setId(Long.parseLong(getObjFromJson(res, "id",null))); // сделать парсинг параметра id

            System.out.println(this.name);

        }
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

}
