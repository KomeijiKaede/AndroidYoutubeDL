package net.teamfruit.androidyoutubedl

import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.experimental.async
import java.net.URLDecoder

suspend fun getVideoInfo(inputURL: String):String {
    val getVideoInfo = async {
        val videoID = Regex("""v=[\d\p{Upper}\p{Lower}_\-]{11}""").find(inputURL)?.value?.substring(2..12)
        val connect = "http://www.youtube.com/get_video_info?video_id=$videoID".httpGet().response().toString()
        return@async URLDecoder.decode(URLDecoder.decode(connect)).replace("%2C",",").replace("%2F", "/").replace("+", " ")
    }.await()
    val opus = Regex("""codecs=\"opus\"""")
    val vorbis = Regex("""codecs=\"vorbis\"""")
    if(!opus.containsMatchIn(getVideoInfo) || !vorbis.containsMatchIn(getVideoInfo)) return "audioURL not found"
    return getVideoInfo
}

suspend fun getVideoTitle(inputURL: String):String {
    return async {
        val info = Regex(""",\"""").split(getVideoInfo(inputURL))
        for(list in info) {
            when {
                Regex("""title\":""").containsMatchIn(list) -> return@async list.substring(Regex("""title\":\"""").find(list)?.range?.last!!).substring(1 until list.length-8)
                else ->{}
            }
        }
        return@async "not found"
    }.await()
}

tailrec suspend fun getCodecsURL(inputURL:String):String {
    val videoInfo = getVideoInfo(inputURL)
    val codecsList:MutableList<String> = videoInfo.split(";").toMutableList()
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
    val co = async {
        val url = getCodecsURL(inputURL)
        when(url.httpGet().response().second.statusCode) {
            200 -> {return@async url}
            else -> {return@async null}
        }
    }.await()
    for(i in 0..5) if(co != null) return co
    return statusCheck(inputURL)
}