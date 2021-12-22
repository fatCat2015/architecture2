package com.eju.demomodule.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.architecture.baseViewModels
import com.eju.baseadapter.section.CeilingItemDecoration
import com.eju.demomodule.adapter.ContactAdapter
import com.eju.demomodule.databinding.ActivityContactBinding
import com.eju.demomodule.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        binding.rv.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = (recyclerView.layoutManager as? LinearLayoutManager) ?: return
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                if(firstVisiblePosition != RecyclerView.NO_POSITION){
                    val section = adapter.getSectionValueFromItemPosition(firstVisiblePosition)
                    Timber.i("${section}")
                    binding.lnView.select(section)
                }
            }
        })

        binding.lnView.setOnSelectedLetterChangedListener { newSelectedLetter, letterPosition ->
            (binding.rv.layoutManager as? LinearLayoutManager)?.let { layoutManager->
                adapter.getPositionFromSection(newSelectedLetter)?.let { position->
                    layoutManager.scrollToPositionWithOffset(position,0)
                }

            }
        }


    }

}