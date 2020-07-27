package com.task.codegamma.viewmodel


import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.task.codegamma.database.RecentPlace
import com.task.codegamma.respository.Repository
import java.util.*
import kotlin.collections.ArrayList

class RecentPlaceViewModel(application: Application) : AndroidViewModel(application) {

    var recentPlaceLiveData: LiveData<List<RecentPlace>>? = null

    var mRepository: Repository? = null

    init {

        mRepository = Repository(application)

        recentPlaceLiveData = mRepository?.getAllPlaces()

    }

    internal fun getAllRecentPlaceList(): LiveData<List<RecentPlace>>? {
        return recentPlaceLiveData
    }


    fun insertRecentPlace(recentPlace: RecentPlace){
        mRepository?.insertPlace(recentPlace)
    }

    fun checkValidity(name: String, description : String) : Boolean{

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description)){
            return true
        }

        return false

    }

}