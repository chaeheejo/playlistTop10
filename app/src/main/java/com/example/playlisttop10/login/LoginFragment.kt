package com.example.playlisttop10.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.playlisttop10.R
import com.example.playlisttop10.User
import com.example.playlisttop10.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel

    private lateinit var et_id: EditText
    private lateinit var et_password: EditText
    private lateinit var btn_logIn: Button
    private lateinit var btn_signUp: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        et_id = binding.logInEtId
        et_password = binding.logInEtPassword
        btn_logIn = binding.logInBtnLogIn
        btn_signUp = binding.logInBtnSignUp
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_logIn.setOnClickListener {
            val id = et_id.text.toString()
            val password = et_password.text.toString()

            val user = User(id, password, "")

            loginViewModel.tryLogIn(user)
        }

        btn_signUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        loginViewModel.isLoggedIn().observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> findNavController().navigate(R.id.action_loginFragment_to_playlistFragment)
                false -> Toast.makeText(activity, loginViewModel.getErrorMessage()!!, Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}