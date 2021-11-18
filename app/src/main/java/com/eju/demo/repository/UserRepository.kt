package com.eju.demo.repository

import com.eju.architecture.base.BaseRepository
import com.eju.demo.entity.User
import com.eju.appbase.service.PagedList
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
                total_count = 60,
                list = mockList(page,pageSize)
            )
        }
    }

    private fun mockList(page:Int,pageSize:Int):List<User>{
        val startIndex = (page-1)*pageSize
        return List(pageSize){
            val id = "${startIndex+it}"
            User(id,"我是name ${id}")
        }
    }

}