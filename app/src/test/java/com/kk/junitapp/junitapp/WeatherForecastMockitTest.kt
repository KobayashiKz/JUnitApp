package com.kk.junitapp.junitapp

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class WeatherForecastMockitTest {

    lateinit var satellite: Satellite
    lateinit var weatherForecast: WeatherForecast

    // Mockit: モック用ライブラリ
    // モック,スタブ,スパイが主な機能.
    // 本来Java向けのライブラリなのでKotlinとの相性はいまいち(Mockit-Kotlin)

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
    fun shouldBringUmbrella_givenSunny_returnsFalse() {
        // shouldBringUmbrella()のgetWeather()がSUNNYなのでfalseが返る
        val actual = weatherForecast.shouldBringUmbrella()
        assertThat(actual).isFalse()
    }

    // 引数マッチャー: 引数の値によってスタブの戻り値を変えることができる仕組み
    @Test
    fun shouldBringUmbrella_argument_matcher() {
        val satellite: Satellite = mock(name = "MockSatellite")
        // any()で引数をワイルドカードでマッチさせて戻り値を検証する
        whenever(satellite.getWeatherLocation(any(), any())).thenReturn(Weather.SUNNY)
        // 引数を直接指定して、戻り値を検証する
        whenever(satellite.getWeatherLocation(eq(37.580006), eq(-122.345106))).thenReturn(Weather.CLOUDY)
        whenever(satellite.getWeatherLocation(eq(37.792872), eq(-122.396915))).thenReturn(Weather.RAINY)
    }

    // Answer: スタブメソッドの呼び出しの引数を取り出したり、その値に応じた戻り値を変更できる
    // 使用するには、whenever().thenReturn()と引数マッチャーを組み合わせる
    // 実際にメソッドが呼ばれた場合の引数に応じた戻り値までの処理を記載する
    @Test
    fun shouldBringUmbrella_givenInJapan_returnsFalse() {
        // getWeather()の関数の処理の制御を行う
        // 引数によって戻り値を制限する
        val satellite: Satellite = mock(name = "MockSatellite")
        // any()で両方の引数をマッチさせる
        whenever(satellite.getWeatherLocation(any(), any())).thenAnswer{ invocation ->
            // invocationに様々な情報が入っている
            // 第一引数
            val latitude = invocation.arguments[0] as Double
            // 第二引数
            val longitude = invocation.arguments[1] as Double

            if (latitude in 20.424086..45.550999
                && longitude in 122.933872..153.980789) {
                // thenAnswer()はラムダ式のためreturnできない
                // return@thenAnswerでラムダ式を抜ける
                return@thenAnswer Weather.SUNNY
            } else {
                return@thenAnswer Weather.RAINY
            }
        }

        // 実際に確認
        val actual = weatherForecast.shouldBringUmbrellaLocation(35.669784, 139.817728)
        assertThat(actual).isFalse()
    }
}