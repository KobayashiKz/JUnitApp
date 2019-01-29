package com.kk.junitapp.junitapp

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class WeatherForecastMockTest {
    lateinit var weatherForecast: WeatherForecast
    lateinit var recorder: MockWeatherRecorder
    lateinit var formatter: SpyWeatherFormatter

    @Before
    fun setUp() {
        val satellite = Satellite()
        // 1. WeatherRecorderクラスの代わりにモックを渡す
        recorder = MockWeatherRecorder()
        weatherForecast = WeatherForecast(satellite, recorder, formatter)
    }

    @After
    fun tearDown() {
    }

    // モック： テスト対象が依存コンポーネントに与える値の検証に使用される
    // モック用クラスの中に、テスト対象のメソッドをオーバーライドして検証する
    class MockWeatherRecorder: WeatherRecorder() {
        var weather: Weather? = null
        // record()が呼ばれた判定フラグ
        var isCall = false

        override fun record(weather: Weather) {
            // 引数weatherの確認と呼び出されていることの確認
            // モッククラスなのでこの中身の処理をテストしやすいように処理する
            this.weather = weather
            isCall = true
        }
    }

    @Test
    fun recordCurrentWeather_assertCalled() {
        // テスト対象のメソッドを呼び出す
        weatherForecast.recordCurrentWeather()
        // フラグが更新されているかチェックし、無事にメソッドが呼び出されていること
        val isCalled = recorder.isCall
        assertThat(isCalled).isTrue()

        // 引数を検証する
        val weather = recorder.weather
        assertThat(weather)
            .isIn(Weather.SUNNY, Weather.CLOUDY, Weather.RAINY)
    }


    // スパイ: スタブの上位互換のようなものでテスト対象に与える入力をテスト
    // スパイ用のクラス
    class SpyWeatherFormatter: WeatherFormatter() {
        var weather: Weather? = null
        var isCalled = false

        override fun format(weather: Weather): String {
            // スパイなので入力を設定して親メソッドを呼び出す
            this.weather = weather
            isCalled = true
            return super.format(weather)
        }
    }

    @Test
    fun recordCurrentWeather_assertFormatterCalled() {
        weatherForecast.recordCurrentWeather()

        // スパイクラスのformatter()が呼ばれること
        val isCalled = formatter.isCalled
        assertThat(isCalled).isTrue()

        // その際の引数をチェックする
        val weather = formatter.weather
        assertThat(weather)
            .isIn(Weather.SUNNY, Weather.CLOUDY, Weather.RAINY)
    }

    // フェイク: 実際のコンポーネントに極めて近い機能をもったオブジェクト
    // 実際よりもシンプルで軽量に実装するとよい

    // ダミー: テスト対象メソッドとの依存関係はないが、テスト実行するために用意しなくてはいけないオブジェクト

}