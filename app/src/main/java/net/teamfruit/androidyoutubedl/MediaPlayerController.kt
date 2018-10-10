package net.teamfruit.androidyoutubedl

import android.media.MediaPlayer

object MediaPlayerController {
    var mp = MediaPlayer()

    fun mToS(ms: Int): String {
        val toSec = ms / 1000
        val min = toSec / 60
        val sec = toSec % 60
        val stringSec:String
        val reg = Regex("""\d\d""")
        if(!reg.containsMatchIn(sec.toString())) {
            stringSec = "0$sec"
            return "$min:$stringSec"
        } else {}
        return "$min:$sec"
    }
}