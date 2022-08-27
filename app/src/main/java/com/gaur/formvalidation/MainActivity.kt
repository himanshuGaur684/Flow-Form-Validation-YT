package com.gaur.formvalidation

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.gaur.formvalidation.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!


    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")
    private val confirmPassword = MutableStateFlow("")


    private var errorMessage :String?=null

    private val isFormValid = combine(email,password,confirmPassword){email,password,confirmPassword->

        binding.tvErrorMessage.text = ""

        val isEmailValid = email.length > 6
        val isPassword = password.length>6
        val isConfirmPasswordEqualsToPassword = password == confirmPassword

        errorMessage = when{
            isEmailValid.not()->{"Email is not valid"}
            isPassword.not()->{"Password is not valid"}
            isConfirmPasswordEqualsToPassword.not()->{"Confirm password is not equal to password"}
            else->{null}
        }
        errorMessage?.let {
            binding.tvErrorMessage.text = it
        }
        isEmailValid and isPassword and isConfirmPasswordEqualsToPassword
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            edEmail.doOnTextChanged { text, start, before, count ->
                email.value = text.toString()
            }
            edPassword.doOnTextChanged { text, start, before, count ->
                password.value = text.toString()
            }
            edConfirmPassword.doOnTextChanged { text, start, before, count ->
                confirmPassword.value = text.toString()
            }

        }


        lifecycleScope.launchWhenCreated {
            isFormValid.collectLatest {
                binding.btnValidate.apply {
                    backgroundTintList = ColorStateList.valueOf(
                        if(it){
                            ContextCompat.getColor(this@MainActivity,R.color.purple_700)
                        }else{
                            ContextCompat.getColor(this@MainActivity,R.color.black)
                        }
                    )
                }
            }
        }


    }
}