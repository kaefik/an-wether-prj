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
import java.util.HashMap;
import java.util.Map;

public class Utils {

    // получение страницы из урла strurl
    static public String getHttpRequestFromUrl(String strurl) {
        String resultStr = null;
        try {
            URL url = new URL(strurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            InputStream stream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            resultStr = "";
            while ((line = bufferedReader.readLine()) != null) {
                resultStr += line;
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
    static public String getObjFromJson(String sjosn, String nameParrent, String nameChild) {

        JSONObject parentObject = null;
        JSONObject childObject = null;
        String res = null;
        try {
            parentObject = new JSONObject(sjosn);
            if (nameParrent != null) {
                res = parentObject.get(nameParrent).toString();
                if (nameChild != null) {
                    if (res != null) {
                        childObject = new JSONObject(res);
                        res = childObject.get(nameChild).toString();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    //сделать первую букву заглавной
    public static String firstUpCaseString(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    // перевод градусов в румбы (С, В, Ю, З и т д)
    public String windGradus2Rumb(Float gradusWind) {
        String windRumb = "";

        Map<Float, String> map = new HashMap<Float, String>();
        map.put(22.0f, "ССВ");

        windRumb = map.get(gradusWind);

//        System.out.println(map.get("dog"));

//            case : windRumb = "ССВ";
//            case 45.0f: windRumb = "СВ";
//            case 68.0f: windRumb = "ВСВ";
//            case 90.0f: windRumb = "В";
//            case 112.0f: windRumb = "ВЮВ";
//            case 135.0f: windRumb = "ЮВ";
//            case 158.0f: windRumb = "ЮЮВ";
//            case 180.0f: windRumb = "Ю";
//            case 202.0f: windRumb = "ЮЮЗ";
//            case 225.0f: windRumb = "ЮЗ";
//            case 248.0f: windRumb = "ЗЮЗ";
//            case 270.0f: windRumb = "З";
//            case 292.0f: windRumb = "ЗСЗ";
//            case 295.0f: windRumb = "СЗ";
//            case 318.0f: windRumb = "ССЗ";


        return windRumb;

    }

    // возвращает ближайшее из двух чисел ch1 или ch2 число ch
//    public Float getNearNumber(Float ch,Float ch1,Float ch2){
//        Float d =  abs(ch2-ch1)/2;
//        if (ch<=ch1+d){
//            return ch1;
//        } else{
//            return ch2;
//        }
//
//    }



}
