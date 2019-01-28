package com.kk.junitapp.junitapp

import org.assertj.core.api.Assertions.*
import org.assertj.core.api.SoftAssertions
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// JUnit4とHamcrest：古くからあるライブラリ
// 書きやすい. ただ、書き方が冗長,エラーの出力がわかりにくいデメリットも
// AssertJ: 読みやすくて運用しやすい, ユニットテストが効率的になる
class InputCheckerTest {
    // テストケースを実行する範囲
    // 考えうるすべてのパターンを実施するのは得策でない
    // 1. 同値分割：テスト対象が同じ挙動をする値
    // 例. 3文字以上/3文字未満, 半角英数字/半角英数字以外
    // 2. 境界値：わけた境界にある値
    // 例. 3文字以上->3, 3文字未満->2
    // 3. 単項目チェック：同値分割や境界値で分割してもなお条件が増える場合に1つの条件のみチェック
    // テスト対象が最後まで実行される状態で複数試験する
    // 例. 3文字未満かつ英語, 3文字未満かつ数字, 3文字未満かつ英数字

    lateinit var target: InputChecker

    @Before
    // @Beforeでテストケースが走る直前に必ず走るようにしている
    // 前提となるテストのデータを用意するのに向いている
    fun setUp() {
        target = InputChecker()
    }

    @After
    // @Afterでテストケース終了後に必ず走る
    // テストケースの後処理を記述できる
    fun tearDown() {
    }

    /**
     * JUnit4
     */
    @Test
    // 3文字未満でfalseが返ることのチェック
    fun isValid_givenLessThan3_returnsFalse() {
        val actual = target.isValid("ab")
        assertThat(actual, `is`(false))
    }

    @Test
    // 3文字以上の英字
    fun isValid_givenAlphabetric_returnsTrue() {
        val actual = target.isValid("abc")
        assertThat(actual, `is`(true))
    }

    @Test
    // 3文字以上の数字
    fun isValid_givenNumeric_returnsTrue() {
        val actual = target.isValid("123")
        assertThat(actual, `is`(true))
    }

    @Test
    // 3文字以上の英数字
    fun isValid_givenAlphaNumeric_returnsTrue() {
        val actual = target.isValid("123ab")
        assertThat(actual, `is`(true))
    }

    @Test
    // 3文字以上の英数字以外
    fun isValid_givenInvalidCharacter_returnsFalse() {
        val actual = target.isValid("abc@123")
        assertThat(actual, `is`(false))
    }

    @Test(expected = IllegalArgumentException::class)
    // nullを引数にしてIllegalArgumentException()が発生することを確認するテスト
    // expected = ....  を使用する
    // 例外メッセージなども検証したい場合にはAssertJを使用する必要あり
    fun isValid_givenNull_throwsIllegalArgumentException() {
        target.isValid(null)
    }

    @Ignore("テスト対象が仮実装のため一時的にスキップ")
    @Test
    // @Ignoreによりテストがスキップされること
    // @Ignoreがついたテストを長期間いるのはあまり好ましいことではない
    fun temporarilySkipThisTest() {
        // do nothing.
    }

    @RunWith(JUnit4::class)
    // JUnit4標準のテストランナー
    // テストランナー：使用することでテストケースの収集・実行し結果をユーザーに知らせる
    // @RunWithを省略した場合にはJUnit4::classが使用される
    // MockitやRobolectricなど後々使用する
    class InputCheckerTest {}

    /**
     * AssertJ
     */
    @Test
    // 3文字未満でfalseが返ることのチェック（AssertJ）
    fun isValid_givenLessThan3_returnsFalse_assertJ() {
        val actual = target.isValid("ab")
        assertThat(actual).isFalse()
    }

    @Test
    // 文字列のアサーション
    fun assertJ_string_practice() {
        assertThat("TOKYO")
            // テストが失敗したときに表示されるラベル
            .`as`("TEXT CHECK TOKYO")
            // 期待値とイコールであるか
            .isEqualTo("TOKYO")
            // 大文字小文字を無視した場合
            .isEqualToIgnoringCase("tokyo")
            .isNotEqualTo("KYOTO")
            .isNotBlank()
            .startsWith("TO")
            .endsWith("YO")
            .contains("OKY")
            .matches("[A-Z]{5}")
            .isInstanceOf(String::class.java)

        // SoftAssertionsを使用することで、いずれかで失敗した場合でも最後まで実行して情報あつめる
        SoftAssertions().apply {
            // assertThat()...
        }.assertAll()
    }

    @Test
    // 数値のアサーション
    fun assertJ_numeric_practice() {
        assertThat(3.14159)
            .isNotZero()
            // マイナスでない
            .isNotNegative()
            // 3より大きい
            .isGreaterThan(3.0)
            // 4以下
            .isLessThanOrEqualTo(4.0)
            .isBetween(3.0, 3.2)
            // 対象の数値からの誤差
            // Mathで定義された円周率の0.001の誤差以内
            .isCloseTo(Math.PI, within(0.001))
    }

    @Test
    // リストのアサーション
    fun assertJ_list_practice() {
        val target: List<String> = listOf("Giants", "Dodgers", "Athletics")
        assertThat(target)
            // 要素数
            .hasSize(3)
            .contains("Dodgers")
            // 順不同で同じ要素のみが含まれているか
            .containsOnly("Athletics", "Dodgers", "Giants")
            // 同じ要素が同じ順番で含まれているか
            .containsExactly("Giants", "Dodgers", "Athletics")
            // 含まれていないか
            .doesNotContain("Padres")
    }

    @Test
    fun assertJ_filtering_practice() {
        // 検証用データクラス
        data class BallTeam(val name: String, val city: String, val stadium: String)

        val target = listOf(
            BallTeam("Giants", "San Francisco", "AT&T Park"),
            BallTeam("Dodgers", "Los Angels", "Dodger Stadium"),
            BallTeam("Angels", "Los Angels", "Angel Stadium"),
            BallTeam("Athletics", "Oakland", "Oakland Coliseum"),
            BallTeam("Padres", "San Diego", "Petco Park")
        )

        assertThat(target)
            // 開始と終わりの文字列でフィルタリング
            .filteredOn{team -> team.city.startsWith("San")}
            .filteredOn{team -> team.city.endsWith("Francisco")}
            // nameプロパティのみを取り出している(Giantsのみが残る)
            .extracting("name", String::class.java)
            .containsExactly("Giants")

        assertThat(target)
            .filteredOn{team -> team.city == "Los Angels"}
            .extracting("name", "stadium")
            .containsExactly(
                tuple("Dodgers", "Dodger Stadium"),
                tuple("Angels", "Angel Stadium")
            )
    }

    @Test
    // assertJの例外チェック
    fun assertJ_exception_practice() {
        assertThatExceptionOfType(RuntimeException::class.java)
            // 例外を出す可能性のあるメソッドを指定
            .isThrownBy { target.isValid(null) }
            // Exceptionの詳細メッセージ
            .withMessage("Aborted!")
            // この例外が他の例外経由で送出されていないことのチェック
            .withNoCause()
    }
    // Truth: AssertJに似たインターフェースを持っている
    // Kotlinとすごく相性がいい
    // AssertK: AssertJにインスパイアされたKotlin向けアサーションライブラリ
}