package com.eju.demo.viewModel

import com.eju.appbase.base.BasePagePagingViewModel
import com.eju.appbase.base.PageLoadingParams
import com.eju.demo.entity.User
import com.eju.demo.repository.UserRepository
import com.eju.appbase.service.PagedList
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