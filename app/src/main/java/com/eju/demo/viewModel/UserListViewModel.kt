package com.eju.demo.viewModel

import com.eju.appbase.base.paging.BasePagePagingViewModel
import com.eju.appbase.base.paging.PageLoadingParams
import com.eju.demo.entity.User
import com.eju.demo.repository.UserRepository
import com.eju.appbase.service.PagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(private val userRepository: UserRepository):BasePagePagingViewModel<User>() {

    override val pageSize: Int
        get() = 20


    override suspend fun load(loadingParams: PageLoadingParams<User>): PagedList<User> {
        return userRepository.load(loadingParams.page,loadingParams.pageSize)
    }

}