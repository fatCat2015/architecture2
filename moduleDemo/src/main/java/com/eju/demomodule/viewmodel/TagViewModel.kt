package com.eju.demomodule.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.demomodule.entity.Tag
import com.eju.demomodule.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(private val tagRepository: TagRepository) :BaseViewModel() {

    val tags = MutableLiveData<List<Tag>>()

    fun getTags(){
        launch {
            tags.postValue(tagRepository.getTags())
        }
    }
}