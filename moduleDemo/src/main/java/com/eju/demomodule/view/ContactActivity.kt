package com.eju.demomodule.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.architecture.baseViewModels
import com.eju.demomodule.R
import com.eju.demomodule.adapter.ContactAdapter
import com.eju.demomodule.databinding.ActivityContactBinding
import com.eju.demomodule.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = PagePath.DemoModule.Contact)
class ContactActivity : AppBaseActivity<ActivityContactBinding>() {

    private val viewModel by baseViewModels<ContactViewModel>()

    private val adapter:ContactAdapter by lazy {
        ContactAdapter()
    }

    override fun observe() {
        viewModel.contactList.observe(this){
            adapter.notifyDataSetChanged(it)
        }
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        binding.rv.adapter = adapter
        viewModel.getContactList()
    }


}