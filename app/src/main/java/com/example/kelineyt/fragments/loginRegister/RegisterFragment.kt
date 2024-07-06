package com.example.kelineyt.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kelineyt.R
import com.example.kelineyt.data.User
import com.example.kelineyt.databinding.FragmentRegisterBinding
import com.example.kelineyt.util.RegisterValidation
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val TAG ="RegisterFragment"
@AndroidEntryPoint
class RegisterFragment:Fragment(R.layout.fragment_register) {

    private lateinit var binding:FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDontHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.apply {
            buttonRegister.setOnClickListener {
            val user=User(
                edFirstName.text.toString().trim(),
                edLastName.text.toString().trim(),
                edEmail.text.toString().trim()
            )
                val password=edPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(user,password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading ->{
                       binding.buttonRegister.startAnimation()
                    }
                    is Resource.Success ->{
                       Log.d("test",it.message.toString())
                        binding.buttonRegister.revertAnimation()
                        Toast.makeText(requireContext(), "Üyelik başarıyla tamamlandı!", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error ->{
                       Log.e(TAG,it.message.toString())
                        binding.buttonRegister.revertAnimation()
                    }
                    else ->Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{validation->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edEmail.apply {
                            requestFocus()
                            error=validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edPassword.apply {
                            requestFocus()
                            error=validation.password.message
                        }
                    }
                }
            }
        }
    }
}