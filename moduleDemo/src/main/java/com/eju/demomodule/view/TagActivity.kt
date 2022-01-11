package com.eju.demomodule.view

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.architecture.baseViewModels
import com.eju.baseadapter.BaseViewHolder
import com.eju.baseadapter.tag.TagAdapter
import com.eju.demomodule.R
import com.eju.demomodule.databinding.ActivityTagBinding
import com.eju.demomodule.databinding.ItemTagBinding
import com.eju.demomodule.entity.Tag
import com.eju.demomodule.viewmodel.TagViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.properties.Delegates

@Route(path = PagePath.DemoModule.TAG)
@AndroidEntryPoint
class TagActivity:AppBaseActivity<ActivityTagBinding>() {

    private val viewModel by baseViewModels<TagViewModel>()

    private var tagAdapter:TagAdapter<Tag,ItemTagBinding> by Delegates.notNull()

    override fun observe() {
        viewModel.tags.observe(this){
            tagAdapter = object:TagAdapter<Tag,ItemTagBinding>(it,3,true,true){
                override fun getLayoutViewBinding(parent: ViewGroup): ItemTagBinding {
                    return ItemTagBinding.inflate(layoutInflater,parent,false)
                }
                override fun bindData(
                    viewHolder: BaseViewHolder<ItemTagBinding>,
                    item: Tag,
                    position: Int
                ) {
                    viewHolder.binding.let {
                        it.tvTag.text = item.name
                        it.tvTag.setTextColor(if(item.isSelected()) Color.RED else Color.parseColor("#333333"))
                        it.tvTag.setBackgroundResource(if(item.isSelected()) R.drawable.bg_tag_selected else R.drawable.bg_tag_unselected)
                    }
                }
            }
            tagAdapter.onSelectedChangedCallback = { selectedItems,selectedCount->
                Timber.i("selectedCount:${selectedCount}")
            }
            tagAdapter.actionWhenExceedMaxCount = {
                showToast("最多只能选择${it}个")
            }
            binding.rv.adapter=tagAdapter
        }
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        viewModel.getTags()
    }
}