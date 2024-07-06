package com.example.kelineyt.fragments.categories

import SharedViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelineyt.R
import com.example.kelineyt.adapters.BestDealsAdapter
import com.example.kelineyt.adapters.BestProductAdapter
import com.example.kelineyt.adapters.SpecialProductsAdapter
import com.example.kelineyt.databinding.FragmentMainCategoryBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.util.showBottomNavigationView
import com.example.kelineyt.viewmodel.MainCategoryViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {

    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductsAdapter: BestProductAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductsRv()
        setupBestDealsRv()
        setupBestProductsRv()

        specialProductsAdapter.onClick = {
            val bb = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, bb)
        }
        bestDealsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
        }
        bestProductsAdapter.onClick = {
            val ab = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, ab)
        }


        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        val productList = result.data
                        productList?.let { specialProductsAdapter.submitList(it) }
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, result.message.toString())
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                        Log.e(TAG, "RecyclerView'da hata: ${result.message}")
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestDealsProducts.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(result.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, result.message.toString())
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                        Log.e(TAG, "RecyclerView'da hata: ${result.message}")
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.bestProductsProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        result.data?.let { bestProductsAdapter.updateItems(it) }
                        binding.bestProductsProgressbar.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        Log.e(TAG, result.message.toString())
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                        Log.e(TAG, "RecyclerView'da hata: ${result.message}")
                        binding.bestProductsProgressbar.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

        sharedViewModel.query.observe(viewLifecycleOwner) { query ->
            bestProductsAdapter.filter(query)
        }


        binding.nestedScrollMainCategory.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                if (v.getChildAt(0).bottom <= v.height + scrollY) {
                    viewModel.fetchBestProducts()
                }
            }
        )
    }

    private fun setupBestProductsRv() {
        bestProductsAdapter = BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

    private fun showLoading() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.mainCategoryProgressbar.visibility = View.GONE
    }

    private fun setupSpecialProductsRv() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}
