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
import androidx.navigation.fragment.NavHostFragment
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
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        signupViewModel = ViewModelProvider(requireActivity())[SignupViewModel::class.java]
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

            when (val result: String ?= signupViewModel.validateInformationForm(id, password, name)) {
                null -> signupViewModel.trySignUp(id, password, name)
                else -> makeRightToastMessage(result)
            }
        }

        signupViewModel.isSignedUp().observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    makeRightToastMessage("success")
                    NavHostFragment.findNavController(this)
                        .navigate(R.id.action_signupFragment_to_loginFragment)
                }
                false -> Toast.makeText(activity, signupViewModel.getErrorMessage(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun makeRightToastMessage(str: String) {
        var toast = ""
        when (str) {
            "id" -> toast = "5글자 이상의 id를 입력해주세요."
            "password" -> toast = "4글자 이상의 password를 입력해주세요."
            "name" -> toast = "정확한 이름을 입력해주세요."
            "success" -> toast = "회원가입에 성공하셨습니다."
            "fail" -> toast = "회원가입에 실패하였습니다."
        }
        Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
