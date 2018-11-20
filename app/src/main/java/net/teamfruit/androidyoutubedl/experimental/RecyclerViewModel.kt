package net.teamfruit.androidyoutubedl.experimental

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.AsyncTask
import net.teamfruit.androidyoutubedl.experimental.database.Base
import net.teamfruit.androidyoutubedl.experimental.database.DBEntity
import net.teamfruit.androidyoutubedl.experimental.database.DataAccessObjects

class RecyclerViewModel(application: Application): AndroidViewModel(application) {
    var listinfo: LiveData<List<DBEntity>>
    private val db: Base

    init {
        db = Base.getDataBase(this.getApplication())
        listinfo = db.dbdao().livedataAll()
    }

    fun getList(): LiveData<List<DBEntity>> = listinfo

    fun addList(entity: DBEntity) {
        AddAsyncTask(db).execute(entity)
    }
}

class AddAsyncTask(private val base: Base): AsyncTask<DBEntity, Void, Void>() {
    override fun doInBackground(vararg params: DBEntity): Void? {
        base.dbdao().insert(params[0])
        return null
    }
}