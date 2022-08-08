package com.example.playlisttop10.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.playlisttop10.R
import com.example.playlisttop10.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val signupViewModel: SignupViewModel = SignupViewModel()

    private var id: String = ""
    private var password: String = ""
    private var name: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupBtnSubmit.setOnClickListener {
            id = binding.signupEdittextId.text.toString()
            password = binding.signupEdittextPassword.text.toString()
            name = binding.signupEdittextName.text.toString()

            val result: String? = signupViewModel.checkUserInformationFormat(id, password, name)
            when (result) {
                null -> signupViewModel.tryGetIdList()
                else -> makeToast(result)
            }
        }

        signupViewModel.getDuplicatedState().observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    if (id !in signupViewModel.getIdList()){
                        signupViewModel.trySignup(id, password, name)
                    }
                    else{
                        makeToast("duplicate")
                    }
                }
                false -> makeToast("fail")
            }
        })

        signupViewModel.getSignupState().observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    makeToast("success")
                    NavHostFragment.findNavController(this)
                        .navigate(R.id.action_signupFragment_to_loginFragment)
                }
                false -> makeToast("fail")
            }
        })
    }

    private fun makeToast(str: String) {
        var toast: String = ""
        when (str) {
            "id" -> toast = "5글자 이상의 id를 입력해주세요."
            "password" -> toast = "4글자 이상의 password를 입력해주세요."
            "name" -> toast = "정확한 이름을 입력해주세요."
            "success" -> toast = "회원가입에 성공하셨습니다."
            "fail" -> toast = "회원가입에 실패하였습니다."
            "duplicate" -> toast = "중복된 id를 입력했습니다."
        }
        Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
