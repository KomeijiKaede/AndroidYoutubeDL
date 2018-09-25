package net.teamfruit.androidyoutubedl

import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.experimental.async
import java.net.URLDecoder

tailrec suspend fun getCodecsURL(inputURL:String):String {
    val getVideoInfo = async {
        val videoID = Regex("""v=[\d\p{Upper}\p{Lower}_\-]{11}""").find(inputURL)?.value?.substring(2..12)
        val connect = "http://www.youtube.com/get_video_info?video_id=$videoID".httpGet().response().toString()
        return@async URLDecoder.decode(URLDecoder.decode(connect)).replace("%2C",",").replace("%2F", "/").replace("+", " ")
    }.await()
    val opus = Regex("""codecs=\"opus\"""")
    val vorbis = Regex("""codecs=\"vorbis\"""")
    if(!opus.containsMatchIn(getVideoInfo) || !vorbis.containsMatchIn(getVideoInfo)) return "audioURL not found"
    val codecsList:MutableList<String> = getVideoInfo.split(";").toMutableList()
    var codecsUrl:String? = null
    loop@for(list in codecsList) {
        when {
            Regex("""codecs=\"vorbis\"""").containsMatchIn(list) -> {
                codecsUrl = list.substring(Regex("""url=h""").find(list)?.range?.last!!)
                break@loop
            }
            Regex("""codecs=\"opus\"""").containsMatchIn(list) -> {
                codecsUrl = list.substring(Regex("""url=h""").find(list)?.range?.last!!)
                break@loop
            }
            else -> {}
        }
    }
    return codecsUrl ?: getCodecsURL(inputURL)
}
tailrec suspend fun statusCheck(inputURL: String):String {
    val codecsURL = getCodecsURL(inputURL)
    if(codecsURL == "audioURL not found") return "audioURL not found"
    val status = codecsURL.httpGet().response().second.statusCode
    return when(status) {
        200 -> { codecsURL }
        403 -> { statusCheck(inputURL) }
        else -> { statusCheck(inputURL) }
    }
}