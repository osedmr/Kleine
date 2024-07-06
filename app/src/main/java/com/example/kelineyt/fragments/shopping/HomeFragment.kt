package com.example.kelineyt.fragments.shopping

import SharedViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import com.example.kelineyt.R
import com.example.kelineyt.adapters.HomeViewpagerAdapter
import com.example.kelineyt.databinding.FragmentHomeBinding
import com.example.kelineyt.fragments.categories.AccessoryFragment
import com.example.kelineyt.fragments.categories.ChairFragment
import com.example.kelineyt.fragments.categories.CupboardFragment
import com.example.kelineyt.fragments.categories.FurnitureFragment
import com.example.kelineyt.fragments.categories.MainCategoryFragment
import com.example.kelineyt.fragments.categories.TableFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )
        binding.viewpagerHome.isUserInputEnabled = false

        val viewPager2Adapter =
            HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){tab,position->

            when(position){
                0 -> tab.text = resources.getText(R.string.g_home)
                1-> tab.text = resources.getText(R.string.g_chair)
                2-> tab.text = resources.getText(R.string.g_cupboard)
                3-> tab.text = resources.getText(R.string.g_table)
                4-> tab.text = resources.getText(R.string.g_accessory)
                5-> tab.text = resources.getText(R.string.g_furniture)
                6-> tab.text = resources.getText(R.string.g_enlightening)
            }

        }.attach()


        binding.tvSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sharedViewModel.setQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sharedViewModel.setQuery(newText ?: "")
                return true
            }
        })

    }

}