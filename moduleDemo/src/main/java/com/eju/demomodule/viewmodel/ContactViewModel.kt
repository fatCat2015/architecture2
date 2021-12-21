package com.eju.demomodule.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.demomodule.entity.Contact
import com.eju.demomodule.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val contactRepository: ContactRepository):BaseViewModel() {

    val contactList = MutableLiveData<List<Contact>>()

    fun getContactList(){
        launch {
            contactList.postValue(contactRepository.getContactList())
        }
    }
}