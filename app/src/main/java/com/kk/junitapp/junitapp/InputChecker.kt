package com.kk.junitapp.junitapp

// テスト対象クラス
class InputChecker {

    // 3文字以上かつ半角英数字のみであるか
    fun isValid(text: String?): Boolean {
        if (text == null) throw IllegalArgumentException()
        return text.length >= 3 && text.matches(Regex("[a-zA-Z0-9]+"))
    }
}