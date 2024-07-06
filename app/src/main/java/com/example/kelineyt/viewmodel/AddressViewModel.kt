package com.example.kelineyt.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.kelineyt.R
import com.example.kelineyt.data.Address
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress = _addNewAddress.asStateFlow()
    private val _deleteAddressState = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val deleteAddressState: StateFlow<Resource<String>> = _deleteAddressState


    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun addOrUpdateAddress(address: Address) {
        val validateInputs = validateInputs(address)
        if (validateInputs) {
            viewModelScope.launch { _addNewAddress.emit(Resource.Loading()) }

            val userAddressRef = firestore.collection("user").document(auth.uid!!)
                .collection("address")

            if (address.id.isEmpty()) {
                // Yeni adres ekle
                userAddressRef.add(address)
                    .addOnSuccessListener { documentRef ->
                        val updatedAddress = address.copy(id = documentRef.id)
                        documentRef.set(updatedAddress)
                        viewModelScope.launch { _addNewAddress.emit(Resource.Success(updatedAddress)) }
                    }
                    .addOnFailureListener {
                        viewModelScope.launch { _addNewAddress.emit(Resource.Error(it.message.toString())) }
                    }
            } else {
                // Mevcut adresi güncelle
                userAddressRef.document(address.id).set(address)
                    .addOnSuccessListener {
                        viewModelScope.launch { _addNewAddress.emit(Resource.Success(address)) }

                    }
                    .addOnFailureListener {
                        viewModelScope.launch { _addNewAddress.emit(Resource.Error(it.message.toString())) }
                    }
            }
        } else {
            viewModelScope.launch {
                _error.emit("Tüm Alanları Doldurun La")
            }
        }
    }

    private fun validateInputs(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty() &&
                address.city.trim().isNotEmpty() &&
                address.fullName.trim().isNotEmpty() &&
                address.phone.trim().isNotEmpty() &&
                address.state.trim().isNotEmpty() &&
                address.street.trim().isNotEmpty()
    }
    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            _deleteAddressState.emit(Resource.Loading())
            val userAddressRef = firestore.collection("user").document(auth.uid!!)
                .collection("address")

            try {
                userAddressRef.document(addressId).delete().await()
                _deleteAddressState.emit(Resource.Success("Adres başarıyla silindi."))
            } catch (e: Exception) {
                _deleteAddressState.emit(Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu."))
            }
        }
    }


}
