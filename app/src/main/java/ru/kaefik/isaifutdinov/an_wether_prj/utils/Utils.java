/*
  * Copyright (C) 2016 Ilnur Sayfutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/

package ru.kaefik.isaifutdinov.an_wether_prj.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// Класс утилитных функций
public class Utils {

    // получение страницы из урла strurl
    static public String getHttpRequestFromUrl(String strurl) {
        String resultStr = "";
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
//            e.printStackTrace();
            resultStr = "";
        } catch (IOException e) {
//            e.printStackTrace();
            resultStr = "";
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
            res = "0";
        }
        return res;
    }

    //сделать первую букву заглавной
    public static String firstUpCaseString(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }


    //----- работа  с файлами

    // Метод для открытия файла
    public static String openFile(String filename, Context context) {
        String res = "";
        try {
            FileInputStream inputStream = context.openFileInput(filename);
            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                inputStream.close();
                res = builder.toString();
            }
        } catch (Throwable t) {
            // TODO: сделать передачу исключения к уровню повыше, чтобы вывести пользователю сообщение об ошибке
        }
        return res;
    }


    // Метод для сохранения файла
    public static void saveFile(String filename, String strText, Context context) {
        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(strText);
            osw.close();
        } catch (Throwable t) {
            //TODO: сделать передачу исключения к уровню повыше, чтобы вывести пользователю сообщение об ошибке
        }
    }


    //----- END работа  с файлами


    // TODO: доделать перевод градусов в румбы и отобразить на панели информации о погоде выбранного города
    // перевод градусов в румбы (С, В, Ю, З и т д)
    public static String windGradus2Rumb(Float gradusWind) {
        String windRumb = "";


        if ((gradusWind < 0) | (gradusWind > 360)) {
            windRumb = "";
        }

        if ((gradusWind >= 0) & (gradusWind <= 45)) {
            gradusWind = Utils.getNearNumber(gradusWind, 0f, 45f);
        }
        if ((gradusWind >= 45) & (gradusWind <= 90)) {
            gradusWind = Utils.getNearNumber(gradusWind, 45f, 90f);
        }
        if ((gradusWind >= 90) & (gradusWind <= 135)) {
            gradusWind = Utils.getNearNumber(gradusWind, 90f, 135f);
        }
        if ((gradusWind >= 135) & (gradusWind <= 180)) {
            gradusWind = Utils.getNearNumber(gradusWind, 135f, 180f);
        }
        if ((gradusWind >= 180) & (gradusWind <= 225)) {
            gradusWind = Utils.getNearNumber(gradusWind, 180f, 225f);
        }
        if ((gradusWind >= 225) & (gradusWind <= 270)) {
            gradusWind = Utils.getNearNumber(gradusWind, 225f, 270f);
        }
        if ((gradusWind >= 270) & (gradusWind <= 315)) {
            gradusWind = Utils.getNearNumber(gradusWind, 270f, 315f);
        }
        if ((gradusWind >= 315) & (gradusWind <= 360)) {
            gradusWind = Utils.getNearNumber(gradusWind, 315f, 360f);
        }


        if ((gradusWind == 0f) | (gradusWind == 360f)) {
            windRumb = "С";
        }
        if (gradusWind == 45f) {
            windRumb = "СВ";
        }
        if (gradusWind == 90f) {
            windRumb = "В";
        }
        if (gradusWind == 135f) {
            windRumb = "ЮВ";
        }
        if (gradusWind == 180f) {
            windRumb = "Ю";
        }
        if (gradusWind == 225f) {
            windRumb = "ЮЗ";
        }
        if (gradusWind == 270f) {
            windRumb = "З";
        }
        if (gradusWind == 315f) {
            windRumb = "СЗ";
        }
        return windRumb;

    }

    // возвращает ближайшее из двух чисел ch1 или ch2 число ch
    public static Float getNearNumber(Float ch, Float ch1, Float ch2) {
        Float d = Math.abs(ch2 - ch1) / 2;
        if (ch <= ch1 + d) {
            return ch1;
        } else {
            return ch2;
        }

    }


}
