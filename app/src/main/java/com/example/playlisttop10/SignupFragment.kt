package com.example.playlisttop10

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
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
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupBtnSubmit.setOnClickListener {
            id = binding.signupEdittextId.text.toString()
            password = binding.signupEdittextPassword.text.toString()
            name = binding.signupEdittextName.text.toString()

            val result: String? = signupViewModel.checkUserInformationFormat(id, password, name)
            when (result) {
                null -> signupViewModel.trySignup(id, password, name)
                else -> makeToast(result)
            }
        }

        signupViewModel.getSignupState().observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> NavHostFragment.findNavController(this)
                    .navigate(R.id.action_signupFragment_to_loginFragment)
                false -> makeToast("duplicate")
            }

        })
    }

    private fun makeToast(str: String) {
        var toast: String = ""
        when (str) {
            "id" -> toast = "5글자 이상의 id를 입력해주세요."
            "password" -> toast = "4글자 이상의 password를 입력해주세요."
            "name" -> toast = "정확한 이름을 입력해주세요."
            "duplicate" -> toast = "중복된 id를 입력했습니다."
        }
        Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
