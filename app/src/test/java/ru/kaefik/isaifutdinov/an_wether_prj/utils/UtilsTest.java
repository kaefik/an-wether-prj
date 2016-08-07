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
    }
}