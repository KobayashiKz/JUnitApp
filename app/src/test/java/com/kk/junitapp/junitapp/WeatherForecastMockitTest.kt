package com.kk.junitapp.junitapp

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class WeatherForecastMockitTest {

    lateinit var satellite: Satellite
    lateinit var weatherForecast: WeatherForecast


    @Before
    fun setUp() {
        // Mockit-kotlinでのモック作成
        satellite = mock(name = "MockSatellite")
        // or
        val satelliteB = mock<Satellite>(name = "MockSatellite")

        // これで、getWeather()を呼び出した場合にWeather.SUNNYを返せるようになる
        // .thenReturn()
        whenever(satellite.getWeather()).thenReturn(Weather.SUNNY)

        val recorder = WeatherRecorder()
        val formatter = WeatherFormatter()
        weatherForecast = WeatherForecast(satellite, recorder, formatter)
    }

    @Test
    fun shouldBringAmbrella_givenSunny_returnsFalse() {
        // shouldBringUmbrella()のgetWeather()がSUNNYなのでfalseが返る
        val actual = weatherForecast.shouldBringUmbrella()
        assertThat(actual).isFalse()
    }
}