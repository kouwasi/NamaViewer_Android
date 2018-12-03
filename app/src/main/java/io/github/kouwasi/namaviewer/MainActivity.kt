package io.github.kouwasi.namaviewer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewButton.setOnClickListener {onViewButtonClick()}

        // FIXME:文字列書くとこに書く
        playPathLayout.hint = "livejupiter/000000のとこ"
        playPath.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                playPathLayout.hint = "playpath"
            } else {
                playPathLayout.hint = "livejupiter/000000のとこ"
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun onViewButtonClick() {
        if(playPath.text.isNotEmpty()) {
            val intent = Intent(this, ViewerActivity::class.java)
            intent.putExtra("PLAYPATH", playPath.text.toString())
            startActivity(intent)
        } else {
            Toast.makeText(this, "playpathが空です！！", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        inputMethodManager.hideSoftInputFromWindow(layout.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        layout.requestFocus()

        return super.onTouchEvent(event)
    }
}
