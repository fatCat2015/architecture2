package com.eju.demo.repository

import com.eju.architecture.core.BaseRepository
import com.eju.demo.entity.Moment
import com.eju.demo.entity.MomentNext
import com.eju.demo.entity.MomentPagingData
import com.eju.demo.viewModel.MomentLoadingParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MomentRepository @Inject constructor():BaseRepository(){

    private var page:Int =1

    suspend fun load(momentNext: MomentLoadingParams): MomentPagingData {
        Timber.i("${momentNext.momentNext}  ${momentNext.pageSize}")
        return withContext(Dispatchers.IO){
            delay(1000)
            MomentPagingData().apply {
                this.momentNextParams = if(page>=3) null else MomentNext("next","2112")
                this.momentList = mockList(momentNext.pageSize)
            }
        }
    }

    private fun mockList(pageSize:Int):List<Moment>{
        val startIndex = (page-1)*pageSize
        this.page++
        return List(pageSize){
            val id = "${startIndex+it}"
            Moment(id,"我是moment ${id}")
        }
    }

}