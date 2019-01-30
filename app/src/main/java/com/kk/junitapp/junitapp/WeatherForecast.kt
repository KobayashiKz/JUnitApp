package com.kk.junitapp.junitapp

class WeatherForecast(private val satellite: Satellite,
                      private val recorder: WeatherRecorder? = null,
                      private val formatter: WeatherFormatter? = null) {

    // 傘が必要かどうか. スタブ用メソッド
    // このメソッドはSatelliteクラスに依存している（satellite.getWeather()）
    // Satellite：依存コンポーネントと呼ぶ
    fun shouldBringUmbrella(): Boolean {
        val weather = satellite.getWeather()
        return when (weather) {
            Weather.SUNNY, Weather.CLOUDY -> false
            Weather.RAINY -> true
        }
    }

    // 引数によって戻り値が変わるメソッド
    // 引数マッチャーによってテストする
    fun shouldBringUmbrellaLocation(latitude: Double, longitude: Double): Boolean {
        val weather = satellite.getWeatherLocation(latitude, longitude)
        return when (weather) {
            Weather.SUNNY, Weather.CLOUDY -> false
            Weather.RAINY -> true
        }
    }

    // 現在天気の保存. モック用メソッド
    // 戻り値がないためrecord()が内部で呼ばれたことでOKとする
    fun recordCurrentWeather() {
        val weather = satellite.getWeather()
        recorder?.record(weather)
        val formatted = formatter?.format(weather)
    }

    // Mockitを使用したメソッド呼び出し検証用メソッド
    fun recordCurrentWeatherLocation(latitude: Double, longitude: Double) {
        val weather = satellite.getWeatherLocation(latitude, longitude)
        recorder?.record(weather)
        val formatted = formatter?.format(weather)
    }
}

enum class Weather {
    SUNNY, CLOUDY, RAINY
}

// openでオーバーライド可能に
open class Satellite {
    open fun getWeather(): Weather {
        return Weather.SUNNY
    }

    open fun getWeatherLocation(latitude: Double, longitude: Double): Weather {
        return Weather.SUNNY
    }
}

open class WeatherRecorder {
    open fun record(weather: Weather) {

    }

    open fun recordStr(string: String) {

    }
}

open class WeatherFormatter {
    // 天気の文字列を返す
    // この関数はテストでも実インスタンスを使用して利用できる. スパイが適切
    open fun format(weather: Weather): String = "Weather is $weather"
}

