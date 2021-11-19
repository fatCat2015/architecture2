package com.eju.demomodule.viewmodel

import com.eju.appbase.base.BasePagePagingViewModel
import com.eju.appbase.base.PageLoadingParams
import com.eju.appbase.service.PagedList
import com.eju.demomodule.entity.User
import com.eju.demomodule.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(private val userRepository: UserRepository):
    BasePagePagingViewModel<User>() {

    override val pageSize: Int
        get() = 20


    override suspend fun load(loadingParams: PageLoadingParams<User>): PagedList<User> {
        return userRepository.load(loadingParams.page,loadingParams.pageSize)
    }

}