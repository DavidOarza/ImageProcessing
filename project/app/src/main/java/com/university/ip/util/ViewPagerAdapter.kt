package com.university.ip.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(
    fm: FragmentManager, private var tabNames: List<String>,
    private val fragments: List<Fragment>
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return fragments[position]
    }

    override fun getCount(): Int {
        return tabNames.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabNames.getOrNull(position)
    }
}