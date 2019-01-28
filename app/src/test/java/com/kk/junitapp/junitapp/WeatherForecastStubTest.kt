package com.kk.junitapp.junitapp

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class WeatherForecastStubTest {

    lateinit var weatherForecast: WeatherForecast

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    // テストダブル：テスト対象が依存しているコンポーネントの影武者みたいなもの
    // テストの簡易化や影響範囲の最小化につながる
    // テストダブルの5つの役割
    // 1. スタブ： 事前に定義した値をテスト対象に与えるもの. テスト対象への入力をコントロールできる
    // 2. モック, 3. スパイ, 4. フェイク, 5. ダミー
    @Test
    fun shouldBridgeUmbrella_givenSunny_returnsFalse() {
        // スタブクラスによって天気を指定
        val satellite = StubSatellite(Weather.SUNNY)
        weatherForecast = WeatherForecast(satellite)

        val actual = weatherForecast.shouldBringUmbrella()
        assertThat(actual).isFalse()
    }

    // Satelliteクラスを継承したスタブ
    // スタブによってテストを簡易化することができる
    class StubSatellite(private val anyWeather: Weather): Satellite() {
        override fun getWeather(): Weather {
            return anyWeather
        }
    }
}