package com.heptre.sololeveling.data.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isReady = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            tts?.setPitch(0.8f)       // Lower pitch for Shadow Monarch feel
            tts?.setSpeechRate(0.9f)  // Slightly slower, commanding tone
            isReady = true
        }
    }

    fun speak(text: String, flush: Boolean = true) {
        if (!isReady) return
        val mode = if (flush) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
        tts?.speak(text, mode, null, "sl_${System.currentTimeMillis()}")
    }

    fun speakAdd(text: String) = speak(text, flush = false)

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isReady = false
    }
}
