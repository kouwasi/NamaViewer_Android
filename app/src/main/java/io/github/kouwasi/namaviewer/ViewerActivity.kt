package io.github.kouwasi.namaviewer

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource

import kotlinx.android.synthetic.main.viewer_activity.*

class ViewerActivity : AppCompatActivity() {
    private lateinit var player:SimpleExoPlayer
    private lateinit var playpath:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewer_activity)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        playpath = intent.getStringExtra("PLAYPATH")
        play()
    }

    override fun onResume() {
        super.onResume()
        play()
    }

    override fun onPause() {
        super.onPause()
        stop()
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    private fun play() {
        player = ExoPlayerFactory.newSimpleInstance(this)
        playerView.player = player

        val rtmpDataSourceFactory = RtmpDataSourceFactory()
        val mediaSource = ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                .createMediaSource(Uri.parse("rtmp://live.open2ch.net/live live=1 playpath=$playpath"))

        player.prepare(mediaSource)
        player.playWhenReady = true
    }

    private fun stop() {
        playerView.player = null
        player.release()
    }
}