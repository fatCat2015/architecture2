package com.eju.demo.repository

import com.eju.architecture.base.BaseRepository
import com.eju.demo.service.HelpService
import timber.log.Timber
import javax.inject.Inject


class MainRepository @Inject constructor(private val helpService: HelpService):BaseRepository(){

    suspend fun queryHelpDetail(id:String) = helpService.getHelpDetail(id)

}