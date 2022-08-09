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
import com.example.playlisttop10.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel

    private lateinit var et_id: EditText
    private lateinit var et_password: EditText
    private lateinit var btn_logIn: Button
    private lateinit var btn_signUp: Button

    private var id: String = ""
    private var password: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
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
            id = et_id.text.toString()
            password = et_password.text.toString()

            loginViewModel.tryLogIn(id, password)
        }

        btn_signUp.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginFragment_to_signupFragment)
        }

        loginViewModel.isLoggedIn().observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    val action = LoginFragmentDirections.actionLoginFragmentToPlaylistFragment(id)
                    findNavController().navigate(action)
                }
                false -> Toast.makeText(activity, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}