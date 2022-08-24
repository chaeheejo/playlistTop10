package com.example.playlisttop10.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.playlisttop10.R
import com.example.playlisttop10.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var signupViewModel: SignupViewModel

    private lateinit var et_id: EditText
    private lateinit var et_password: EditText
    private lateinit var et_name: EditText
    private lateinit var btn_submit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signupViewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        et_id = binding.signUpEtId
        et_password = binding.signUpEtPassword
        et_name = binding.signUpEtName
        btn_submit = binding.signUpBtnSubmit
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_submit.setOnClickListener {
            val id = et_id.text.toString()
            val password = et_password.text.toString()
            val name = et_name.text.toString()

            when (val result: String? =
                signupViewModel.validateInformationForm(id, password, name)) {
                null -> signupViewModel.trySignUp(id, password, name)
                else -> makeRightToastMessage(result)
            }
        }

        signupViewModel.isSignedUp.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
        })

        signupViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if (it != null && it != "") {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun makeRightToastMessage(str: String) {
        var toast = ""
        when (str) {
            "id" -> toast = "enter least 5 characters id"
            "password" -> toast = "enter least 4 characters password"
            "name" -> toast = "enter least 2 characters name"
            "success" -> toast = "sign up successful"
        }
        Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
