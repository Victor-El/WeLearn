package me.codeenzyme.videoplayer

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import me.codeenzyme.videoplayer.databinding.ActivityVideoBinding

@AndroidEntryPoint
class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    private lateinit var playerControllerFullscreenBtn: ImageView

    private lateinit var videoTitle: String
    private lateinit var videoUrl: String
    private lateinit var materialUrl: String

    private var exoPlayer: ExoPlayer? = null

    private val videoViewModel by viewModels<VideoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerControllerFullscreenBtn = binding.playerView.findViewById(R.id.exo_fullscreen_icon)

        playerControllerFullscreenBtn.setOnClickListener {
            toggleFullscreen()
        }

        intiIntentExtras()

        initScreenOnLogic()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }

    }

    override fun onResume() {
        super.onResume()
        // hideSystemUi()
        if ((Util.SDK_INT < 24 || exoPlayer == null)) {
            initPlayer()
        }

    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }

    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (videoViewModel.configJustChanged) {
            videoViewModel.configJustChanged = false
            return
        }

        val orientation = newConfig.orientation
        if (!videoViewModel.toggleBtnMode) {
            when (orientation) {

                Configuration.ORIENTATION_PORTRAIT -> {
                    screenPortrait()
                }

                Configuration.ORIENTATION_LANDSCAPE -> {
                    screenLandscape()
                }
                Configuration.ORIENTATION_SQUARE -> {

                }
                Configuration.ORIENTATION_UNDEFINED -> {

                }
            }
        }
    }

    private fun initPlayer() {
        val mediaItem = MediaItem.fromUri(videoUrl)

        exoPlayer = SimpleExoPlayer.Builder(this)
            .build().also {
                it.setMediaItem(mediaItem)
                it.playWhenReady = videoViewModel.playWhenReady
                it.seekTo(videoViewModel.playbackPosition)
                it.prepare()
            }

        binding.playerView.player = exoPlayer
        binding.playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
        initScreenOnLogic()

    }

    private fun releasePlayer() {
        exoPlayer?.let {
            videoViewModel.playWhenReady = it.playWhenReady
            videoViewModel.playbackPosition = it.currentPosition
            videoViewModel.currentWindow = it.currentWindowIndex
        }
        exoPlayer?.release()
        exoPlayer = null

    }

    private fun hideSystemUi() {
        binding.playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

    }

    private fun showSystemUi() {
        binding.playerView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    private fun intiIntentExtras() {
       intent.let {
           videoTitle = it.getStringExtra(VIDEO_TITLE_EXTRA)!!
           videoUrl = it.getStringExtra(VIDEO_URL_EXTRA)!!
           materialUrl = it.getStringExtra(VIDEO_MATERIAL_EXTRA)!!
       }
    }

    private fun toggleFullscreen() {
        if (videoViewModel.fullScreen) {
            screenPortrait()
        } else {
            screenLandscapeHalfSensor()
        }
    }

    private fun screenLandscape() {
        playerControllerFullscreenBtn.setImageResource(R.drawable.exo_controls_fullscreen_exit)
        hideSystemUi()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        videoViewModel.fullScreen = true
        videoViewModel.toggleBtnMode = false
        videoViewModel.configJustChanged = true
    }

    private fun screenPortrait() {
        playerControllerFullscreenBtn.setImageResource(R.drawable.exo_controls_fullscreen_enter)
        showSystemUi()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        videoViewModel.fullScreen = false
        videoViewModel.toggleBtnMode = false
        videoViewModel.configJustChanged = true
    }

    private fun screenLandscapeHalfSensor() {
        playerControllerFullscreenBtn.setImageResource(R.drawable.exo_controls_fullscreen_exit)
        hideSystemUi()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        videoViewModel.fullScreen = true
        videoViewModel.toggleBtnMode = true
        videoViewModel.configJustChanged = false
    }

    private fun initScreenOnLogic() {
        exoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)

                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        binding.playerView.keepScreenOn = true
                    }

                    Player.STATE_ENDED -> {
                        binding.playerView.keepScreenOn = false
                    }

                    Player.STATE_IDLE -> {
                        binding.playerView.keepScreenOn = false
                    }

                    Player.STATE_READY -> {
                        binding.playerView.keepScreenOn = true
                    }
                }

                if (exoPlayer?.playWhenReady!!) {
                    binding.playerView.keepScreenOn = true
                } else if (!exoPlayer?.playWhenReady!!) {
                    binding.playerView.keepScreenOn = false
                }
            }
        })
    }

    companion object {
        const val VIDEO_TITLE_EXTRA = "videoTitleExtra"
        const val VIDEO_URL_EXTRA = "videoUrlExtra"
        const val VIDEO_MATERIAL_EXTRA = "videoMaterialExtra"
    }
}