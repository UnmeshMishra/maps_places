package com.task.codegamma.respository

import android.app.Application
import android.provider.ContactsContract
import com.task.codegamma.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class Repository(private var application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    var mDao: RecentPlaceDao

    init {
        mDao = DatabaseBuilder.getInstance(application).recentPlaceDao()
    }

    fun getAllPlaces() = mDao.getAll()

    fun insertPlace(place: RecentPlace) {
        launch { insertPlaceSuspend(place) }
    }

    private suspend fun insertPlaceSuspend(place: RecentPlace) {
        withContext(Dispatchers.IO) {
            mDao.insert(place)
        }
    }



}