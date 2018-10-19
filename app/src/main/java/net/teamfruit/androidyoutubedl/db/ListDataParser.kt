package net.teamfruit.androidyoutubedl.db

import org.jetbrains.anko.db.MapRowParser

class ListDataParser: MapRowParser<ListData> {
    override fun parseRow(columns: Map<String, Any?>): ListData {
        return ListData(columns["title"] as String?, columns["url"] as String?)
    }
}