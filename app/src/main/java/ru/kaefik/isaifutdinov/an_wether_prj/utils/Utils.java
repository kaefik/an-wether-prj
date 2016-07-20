package ru.kaefik.isaifutdinov.an_wether_prj.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {

    // получение страницы из урла strurl
    static  public String getHttpRequestFromUrl(String strurl){
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

    // получение объектов из json
    static public String getObjFromJson(String sjosn,String nameParrent, String nameChild){

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

}
