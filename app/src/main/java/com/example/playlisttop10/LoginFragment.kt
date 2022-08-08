package com.example.playlisttop10

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.playlisttop10.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel = LoginViewModel()

    private var id: String = ""
    private var password: String = ""
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtnSigIn.setOnClickListener {
            id = binding.loginEdittextId.text.toString()
            password = binding.loginEdittextPassword.text.toString()

            loginViewModel.tryLogIn(id, password)
        }

        binding.loginBtnSignUp.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginFragment_to_signupFragment)
        }

        loginViewModel.getLoginState().observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> NavHostFragment.findNavController(this)
                    .navigate(R.id.action_loginFragment_to_playlistFragment)
                false -> Toast.makeText(activity, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
            }
    }


}