package io.github.kouwasi.namaviewer.helpers

import android.util.Rational

class AspectRatioHelper(val x: Int, val y: Int) {
    // 最大公約数を求める
    private fun gcd(x: Int, y: Int): Int = if(y == 0) x else gcd(y, x % y)

    fun aspectRatio(): Rational {
        val gcd = gcd(x, y)
        return Rational(x / gcd, y / gcd)
    }
}