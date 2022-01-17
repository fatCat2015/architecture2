package com.eju.demomodule.repository

import com.eju.architecture.core.BaseRepository
import com.eju.demomodule.entity.Tag
import javax.inject.Inject

class TagRepository @Inject constructor():BaseRepository() {

    fun getTags() = List(20){ index->
        Tag("id${index}","${if(index==0) "不限" else "name${index}"}").apply {
            setSelected(index==0)
        }
    }

}