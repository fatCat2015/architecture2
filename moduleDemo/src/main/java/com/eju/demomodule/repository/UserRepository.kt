package com.eju.demomodule.repository

import com.eju.architecture.core.BaseRepository
import com.eju.appbase.service.PagedList
import com.eju.demomodule.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor():BaseRepository(){

    suspend fun load(page:Int,pageSize:Int): PagedList<User> {
        Timber.i("${page}  ${pageSize}")
        return withContext(Dispatchers.IO){
            delay(1000)
            PagedList(
                page = null,
                total_page = null,
                total_count = 40,
                list = mockList(page,pageSize)
//                total_count = 0,
//                list = emptyList()
            )
        }
    }

    private fun mockList(page:Int,pageSize:Int):List<User>{
        val startIndex = (page-1)*pageSize
        return List(pageSize){
            val id = startIndex+it
            User(id,"我是name ${id}")
        }
    }

}