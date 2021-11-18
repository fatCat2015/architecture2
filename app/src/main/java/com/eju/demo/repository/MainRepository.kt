package com.eju.demo.repository

import com.eju.architecture.core.BaseRepository
import com.eju.demo.service.HelpService
import javax.inject.Inject


class MainRepository @Inject constructor(private val helpService: HelpService):BaseRepository(){

    suspend fun queryHelpDetail(id:String) = helpService.getHelpDetail(id)

}