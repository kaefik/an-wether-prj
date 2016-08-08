package ru.kaefik.isaifutdinov.an_wether_prj;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.kaefik.isaifutdinov.an_wether_prj.city.CityModel;

public class CityModelTest {

    @Before
    public void setUp() throws Exception {
        System.out.println("Before UtilsTest.class");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("After UtilsTest.class");
    }

    @Test
    // тест функции  translateWeatherDescription(String descWeather)
    public void testTranslateWeatherDescription() {

        CityModel cityModel = new CityModel();
        Assert.assertEquals("неизвестно", cityModel.translateWeatherDescription("привет!"));
        Assert.assertEquals("неизвестно", cityModel.translateWeatherDescription(null));
        Assert.assertEquals("ливень", cityModel.translateWeatherDescription("shower rain"));

    }

}
