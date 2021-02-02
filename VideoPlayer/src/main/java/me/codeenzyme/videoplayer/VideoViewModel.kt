package me.codeenzyme.videoplayer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

class VideoViewModel @ViewModelInject constructor() : ViewModel() {
    var playbackPosition = 0L
    var playWhenReady = true
    var currentWindow = 0

    var fullScreen = false
    var toggleBtnMode = false
    var configJustChanged = true
}