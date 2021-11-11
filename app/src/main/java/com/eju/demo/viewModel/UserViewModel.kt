package com.eju.demo.viewModel

import com.eju.architecture.base.paging.BasePagePagingViewModel
import com.eju.architecture.base.paging.LoadingParams
import com.eju.demo.entity.User
import com.eju.demo.repository.MainRepository
import com.eju.demo.repository.UserRepository
import com.eju.retrofit.PagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository):BasePagePagingViewModel<User>() {

    override suspend fun load(loadingParams: LoadingParams<Int>): PagedList<User> {
        return userRepository.load(loadingParams.key,loadingParams.loadSize)
    }
}