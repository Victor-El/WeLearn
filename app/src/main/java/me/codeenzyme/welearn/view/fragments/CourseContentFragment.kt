package me.codeenzyme.welearn.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import me.codeenzyme.welearn.databinding.FragmentCourseContentBinding
import me.codeenzyme.welearn.view.activities.MainActivity

@AndroidEntryPoint
class CourseContentFragment : Fragment() {

    private val PLAYBACK_POSITION_KEY = "playbackPosition"
    private val PLAY_WHEN_READY_KEY = "playWhenReady"

    private var playbackPosition = 0L
    private var playWhenReady = true
    private var currentWindow = 0

    private lateinit var _binding: FragmentCourseContentBinding
    private lateinit var binding: FragmentCourseContentBinding

    private var exoPlayer: ExoPlayer? = null

    private val topicArg by navArgs<CourseContentFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCourseContentBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCourseContentBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }

    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT < 24 || exoPlayer == null)) {
            initPlayer()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(PLAYBACK_POSITION_KEY, playbackPosition)
        outState.putBoolean(PLAY_WHEN_READY_KEY, playWhenReady)
    }

    private fun hideSystemUi() {
        binding.playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            playWhenReady = getBoolean(PLAY_WHEN_READY_KEY)
            playbackPosition = getLong(PLAYBACK_POSITION_KEY)
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
            (requireActivity() as MainActivity).binding.bottomAppBar.visibility = View.VISIBLE
        }

    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
            (requireActivity() as MainActivity).binding.bottomAppBar.visibility = View.VISIBLE
        }

    }

    private fun initPlayer() {

        val topic = topicArg.topic
        val mediaItem = MediaItem.fromUri(topic.videoUrl!!)

        exoPlayer = SimpleExoPlayer.Builder(requireContext())
            .build().also {
                it.setMediaItem(mediaItem)
                it.playWhenReady = playWhenReady
                it.seekTo(playbackPosition)
                it.prepare()
            }

        binding.playerView.player = exoPlayer
        binding.playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)

    }

    private fun releasePlayer() {
        exoPlayer?.let {
            playWhenReady = it.playWhenReady
            playbackPosition = it.currentPosition
            currentWindow = it.currentWindowIndex
        }
        exoPlayer?.release()
        exoPlayer = null

    }

}