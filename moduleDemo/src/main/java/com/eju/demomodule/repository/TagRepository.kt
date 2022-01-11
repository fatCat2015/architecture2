package com.eju.demomodule.repository

import com.eju.architecture.core.BaseRepository
import com.eju.demomodule.entity.Tag
import javax.inject.Inject

class TagRepository @Inject constructor():BaseRepository() {

    fun getTags() = List(20){
        Tag("id${it}","name${it}").apply {
            setSelected(it%2==0)
        }
    }

}