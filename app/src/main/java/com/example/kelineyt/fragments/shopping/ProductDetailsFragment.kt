package com.example.kelineyt.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelineyt.R
import com.example.kelineyt.activities.ShoppingActivity
import com.example.kelineyt.adapters.ColorsAdapter
import com.example.kelineyt.adapters.SizesAdapter
import com.example.kelineyt.adapters.ViewPager2Images
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.databinding.FragmentProductDetailsBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.util.hideBottomNavigationView
import com.example.kelineyt.viewmodel.DetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding:FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private var selectedColor:Int?=null
    private var selectedSize:String?=null
    private val viewModel by viewModels<DetailsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        // Inflate the layout for this fragment
        binding= FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product
        setupViewpager()
        setupColorsRv()
        setupSizesRv()

        binding.imgClose.setOnClickListener {
            findNavController().navigateUp()
        }

        sizesAdapter.onItemClick = {
            selectedSize=it
        }
        colorsAdapter.onItemClick = {
            selectedColor=it
        }

        binding.btnAddToCart.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(product,1,selectedColor,selectedSize))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.btnAddToCart.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.btnAddToCart.revertAnimation()
                        binding.btnAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                    }
                    is Resource.Error ->{
                        binding.btnAddToCart.stopAnimation()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }


        binding.apply {
            tvProductName.text=product.name
            tvProductPrice.text="$ ${product.price}"
            tvProductDescription.text=product.description

            if (product.colors.isNullOrEmpty())
                tvColor.visibility=View.INVISIBLE
            if (product.sizes.isNullOrEmpty())
                tvProductSize.visibility=View.INVISIBLE
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }

    }

    private fun  setupSizesRv() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }
    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }
    }

    private fun setupViewpager() {
        binding.apply {
            viewPagerProductImages.adapter=viewPagerAdapter
        }

    }

}