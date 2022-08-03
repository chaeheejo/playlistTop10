package com.example.playlisttop10

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.playlisttop10.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding ?=null
    private val binding get() = _binding!!

    private val signupViewModel: SignupViewModel = SignupViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSignupBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupBtnSubmit.setOnClickListener{
            var id = binding.signupEdittextId.text.toString()
            var password =  binding.signupEdittextPassword.text.toString()
            var name =  binding.signupEdittextName.text.toString()

            signupViewModel.setUserInformation(id, password, name)

            when(signupViewModel.checkUserInformationFormat(id, password, name)){
                "id"-> Toast.makeText(requireContext(), "5글자 이상의 id를 입력해주세요.", Toast.LENGTH_SHORT).show()
                "password" -> Toast.makeText(requireContext(), "4글자 이상의 password를 입력해주세요.", Toast.LENGTH_SHORT).show()
                "name" -> Toast.makeText(requireContext(), "정확한 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
            }
    }
}
