package com.example.kelineyt.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kelineyt.data.Address
import com.example.kelineyt.databinding.FragmentAddressBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel>()
    val args by navArgs<AddressFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.addNewAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val address = args.address
        if (address != null) {
            binding.apply {
                edAddressTitle.setText(address.addressTitle)
                edFullName.setText(address.fullName)
                edStreet.setText(address.street)
                edPhone.setText(address.phone)
                edCity.setText(address.city)
                edState.setText(address.state)
                buttonDelelte.visibility = View.VISIBLE
            }
        } else {
            binding.buttonDelelte.visibility = View.GONE
        }

        binding.apply {
            buttonSave.setOnClickListener {
                val addressTitle = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()
                val newAddress = Address(
                    id = address?.id ?: "",  // Yeni adres için boş string, mevcut adres için mevcut id
                    addressTitle = addressTitle,
                    fullName = fullName,
                    street = street,
                    phone = phone,
                    city = city,
                    state = state
                )

                viewModel.addOrUpdateAddress(newAddress)
            }
        }

        binding.imageAddressClose.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonDelelte.setOnClickListener {
            val addressId = args.address?.id
            if (addressId != null) {
                viewModel.deleteAddress(addressId)
            } else {
                // Eğer adres null ise (yani zaten silinmiş veya silinmesi gereken bir adres yoksa)
                Toast.makeText(requireContext(), "Silinecek bir adres bulunamadı.", Toast.LENGTH_SHORT).show()
            }
            findNavController().navigateUp()
        }

    }
}
