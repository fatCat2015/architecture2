package com.eju.tools.widget

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class SimpleFragmentAdapter2(private val fragmentList: List<Fragment>, fragmentManager: FragmentManager,lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager,lifecycle) {

    constructor(fragmentList: List<Fragment>,fragment: Fragment):this(fragmentList,fragment.childFragmentManager,fragment.lifecycle ){

    }

    constructor(fragmentList: List<Fragment>,fragmentActivity: FragmentActivity):this(fragmentList,fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle ){

    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


}
