package com.eju.demo.viewModel

import com.eju.architecture.core.BasePagingViewModel
import com.eju.architecture.core.LoadingParams
import com.eju.demo.entity.Moment
import com.eju.demo.entity.MomentNext
import com.eju.demo.entity.MomentPagingData
import com.eju.demo.repository.MomentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MomentListViewModel @Inject constructor(private val momentRepository: MomentRepository):
    BasePagingViewModel<Moment, MomentPagingData, MomentLoadingParams>(){

    override val loadingParamsFactory: () -> MomentLoadingParams
        get() = {
            MomentLoadingParams(null,20)
        }

    override fun verifyShowEmptyView(
        list: List<Moment>,
        loadingParams: MomentLoadingParams,
        pagedData: MomentPagingData
    ): Boolean {
        return list.isEmpty()
    }

    override fun verifyEnableLoadMore(
        list: List<Moment>,
        loadingParams: MomentLoadingParams,
        pagedData: MomentPagingData
    ): Boolean {
        return pagedData.momentNextParams!=null
    }

    override suspend fun load(loadingParams: MomentLoadingParams): MomentPagingData {
        return momentRepository.load(loadingParams)
    }
}

class MomentLoadingParams(
    private val initialAnchor:MomentNext?,
    val pageSize:Int
): LoadingParams<Moment, MomentPagingData> {

    var momentNext:MomentNext? = initialAnchor

    override fun reset() {
        this.momentNext = initialAnchor
    }
    override fun setNext(list: List<Moment>, data: MomentPagingData) {
        this.momentNext = data.momentNextParams
    }

}