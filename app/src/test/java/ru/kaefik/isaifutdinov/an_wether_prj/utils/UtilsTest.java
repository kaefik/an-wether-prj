/*
  * Copyright (C) 2016 Ilnur Sayfutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/


package ru.kaefik.isaifutdinov.an_wether_prj.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UtilsTest {

    @Before
    public void setUp() throws Exception {
        System.out.println("Before UtilsTest.class");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("After UtilsTest.class");
    }

    @Test
    public void testWindGradus2Rumb() throws Exception {
        Assert.assertEquals("С",Utils.windGradus2Rumb(0f));
        Assert.assertEquals("С",Utils.windGradus2Rumb(360f));
        Assert.assertEquals("СВ",Utils.windGradus2Rumb(45f));
        Assert.assertEquals("",Utils.windGradus2Rumb(-1f));
        Assert.assertEquals("",Utils.windGradus2Rumb(360.5f));
        Assert.assertEquals("В",Utils.windGradus2Rumb(90f));
        Assert.assertEquals("ЮВ",Utils.windGradus2Rumb(135f));
        Assert.assertEquals("Ю",Utils.windGradus2Rumb(180f));
        Assert.assertEquals("ЮЗ",Utils.windGradus2Rumb(225f));
        Assert.assertEquals("З",Utils.windGradus2Rumb(270f));
        Assert.assertEquals("СЗ",Utils.windGradus2Rumb(295f));
        Assert.assertEquals("",Utils.windGradus2Rumb(-1f));
        Assert.assertEquals("",Utils.windGradus2Rumb(360.5f));
    }
    @Test
    // тестирование windGradus2Rumb в промежуточных точках
    public void testWindGradus2Rumb2() throws Exception {
        Assert.assertEquals("С",Utils.windGradus2Rumb(22f));

        Assert.assertEquals("СВ",Utils.windGradus2Rumb(30f));
        Assert.assertEquals("С",Utils.windGradus2Rumb(22.5f));

        Assert.assertEquals("СВ",Utils.windGradus2Rumb(67.5f));
        Assert.assertEquals("В",Utils.windGradus2Rumb(67.6f));

        Assert.assertEquals("В",Utils.windGradus2Rumb(112.5f));
        Assert.assertEquals("ЮВ",Utils.windGradus2Rumb(112.6f));

        Assert.assertEquals("ЮВ", Utils.windGradus2Rumb(157.5f));
        Assert.assertEquals("Ю", Utils.windGradus2Rumb(157.6f));

        Assert.assertEquals("Ю", Utils.windGradus2Rumb(202.5f));
        Assert.assertEquals("ЮЗ", Utils.windGradus2Rumb(202.6f));

        Assert.assertEquals("ЮЗ", Utils.windGradus2Rumb(247.5f));
        Assert.assertEquals("З", Utils.windGradus2Rumb(247.6f));

        Assert.assertEquals("З", Utils.windGradus2Rumb(292.5f));
        Assert.assertEquals("СЗ", Utils.windGradus2Rumb(292.6f));

        Assert.assertEquals("СЗ", Utils.windGradus2Rumb(337.5f));
        Assert.assertEquals("С", Utils.windGradus2Rumb(337.6f));
    }

    @Test
    public void testGetNearNumber(){
//        getNearNumber(Float ch, Float ch1, Float ch2)
        Assert.assertEquals(1f,Utils.getNearNumber(1f,0f,1f),0f);
        Assert.assertEquals(0f,Utils.getNearNumber(0f,0f,1f),0f);
        Assert.assertEquals(1f,Utils.getNearNumber(0.6f,0f,1f),0f);
        Assert.assertEquals(0f,Utils.getNearNumber(0.49f,0f,1f),0f);
        Assert.assertEquals(0f,Utils.getNearNumber(0.05f,0f,1f),0f);
        // проверка вхождения в интервал [0..45]
        Assert.assertEquals(0f,Utils.getNearNumber(22f,0f,45f),0f);
        Assert.assertEquals(45f,Utils.getNearNumber(30f,0f,45f),0f);
        Assert.assertEquals(0f,Utils.getNearNumber(22.5f,0f,45f),0f);
        // проверка вхождения в интервал [45..90]
        Assert.assertEquals(45f,Utils.getNearNumber(67.5f,45f,90f),0f);
        Assert.assertEquals(90f,Utils.getNearNumber(68f,45f,90f),0f);
        Assert.assertEquals(90f,Utils.getNearNumber(89f,45f,90f),0f);
        // проверка вхождения в интервал [90..135]
        Assert.assertEquals(90f,Utils.getNearNumber(112.5f,90f,135f),0f);
        Assert.assertEquals(135f,Utils.getNearNumber(130.5f,90f,135f),0f);
        Assert.assertEquals(135f,Utils.getNearNumber(113f,90f,135f),0f);
        // проверка вхождения в интервал [135..180]
        Assert.assertEquals(135f,Utils.getNearNumber(157.5f,135f,180f),0f);
        Assert.assertEquals(135f,Utils.getNearNumber(136f,135f,180f),0f);
        Assert.assertEquals(180f,Utils.getNearNumber(170f,135f,180f),0f);
        // проверка вхождения в интервал [180..225]
        Assert.assertEquals(180f,Utils.getNearNumber(202.5f,180f,225f),0f);
        Assert.assertEquals(225f,Utils.getNearNumber(203f,180f,225f),0f);
        Assert.assertEquals(225f,Utils.getNearNumber(220.5f,180f,225f),0f);
        // проверка вхождения в интервал [225..270]
        Assert.assertEquals(225f,Utils.getNearNumber(247.5f,225f,270f),0f);
        Assert.assertEquals(270f,Utils.getNearNumber(248f,225f,270f),0f);
        Assert.assertEquals(225f,Utils.getNearNumber(226f,225f,270f),0f);
        // проверка вхождения в интервал [270..315]
        Assert.assertEquals(270f,Utils.getNearNumber(292.5f,270f,315f),0f);
        Assert.assertEquals(270f,Utils.getNearNumber(272.5f,270f,315f),0f);
        Assert.assertEquals(315f,Utils.getNearNumber(292.6f,270f,315f),0f);
        // проверка вхождения в интервал [315..360]
        Assert.assertEquals(315f,Utils.getNearNumber(337.5f,315f,360f),0f);
        Assert.assertEquals(360f,Utils.getNearNumber(337.6f,315f,360f),0f);
    }
}