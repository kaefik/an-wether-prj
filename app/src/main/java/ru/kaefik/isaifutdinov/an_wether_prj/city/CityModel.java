package ru.kaefik.isaifutdinov.an_wether_prj.city;


public class CityModel {

    private long id;
    private String country; // страна
    private String name; // название города
    private float temp; // температура
    private float clouds; // облачность в %
    private float huminidity; // влажность
    private float pressure; // давление
    private float windspeed; // скорость ветра
    private float winddirection; // направление ветра

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
