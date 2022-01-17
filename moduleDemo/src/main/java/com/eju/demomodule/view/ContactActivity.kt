package com.eju.demomodule.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.architecture.core.baseViewModels
import com.eju.baseadapter.section.CeilingItemDecoration
import com.eju.demomodule.adapter.ContactAdapter
import com.eju.demomodule.databinding.ActivityContactBinding
import com.eju.demomodule.viewmodel.ContactViewModel
import com.eju.tools.widget.setUpWithRecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = PagePath.DemoModule.Contact)
class ContactActivity : AppBaseActivity<ActivityContactBinding>() {

    private val viewModel by baseViewModels<ContactViewModel>()

    private val adapter:ContactAdapter by lazy {
        ContactAdapter().apply {
            setOnSectionClickListener { viewHolder, item, position ->
                showToast("${item}")
            }

            setOnSectionItemClickListener { viewHolder, item, position ->
                showToast("${item.name}")
            }
        }
    }

    override fun observe() {
        viewModel.contactList.observe(this){
            adapter.notifyDataSetChanged(it){
                binding.lnView.setLetters(it)
            }
        }
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(CeilingItemDecoration())
        viewModel.getContactList()
    }


    override fun setListeners() {
        binding.lnView.setUpWithRecyclerView(binding.rv,{
            adapter.getSectionValueFromItemPosition(it)
        },{
            adapter.getPositionFromSection(it)
        })

    }

}