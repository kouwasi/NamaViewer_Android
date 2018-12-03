package io.github.kouwasi.namaviewer

import android.app.PictureInPictureParams
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Rational
import android.view.View
import android.view.WindowManager
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.video.VideoListener
import io.github.kouwasi.namaviewer.helpers.AspectRatioHelper

import kotlinx.android.synthetic.main.viewer_activity.*

class ViewerActivity : AppCompatActivity(), VideoListener {
    private lateinit var player:SimpleExoPlayer
    private lateinit var playpath:String
    private lateinit var aspectRatio: Rational

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewer_activity)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        playpath = intent.getStringExtra("PLAYPATH")
        play()
    }

    override fun onPause() {
        super.onPause()

        val pictureInPictureParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
        enterPictureInPictureMode(pictureInPictureParams)
    }

    override fun onRestart() {
        super.onRestart()

        play()
    }

    override fun onStop() {
        super.onStop()

        if(isInPictureInPictureMode)
            moveTaskToBack(true)

        stop()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    private fun play() {
        player = ExoPlayerFactory.newSimpleInstance(this)
        player.addVideoListener(this)
        playerView.player = player

        val rtmpDataSourceFactory = RtmpDataSourceFactory()
        val mediaSource = ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                .createMediaSource(Uri.parse("rtmp://live.open2ch.net/live live=1 playpath=$playpath"))

        player.prepare(mediaSource)
        player.playWhenReady = true
    }

    override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
        super.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio)
        val aspectRatio = AspectRatioHelper(width, height)
        this.aspectRatio = aspectRatio.aspectRatio()
    }

    private fun stop() {
        playerView.player = null
        player.release()
    }
}