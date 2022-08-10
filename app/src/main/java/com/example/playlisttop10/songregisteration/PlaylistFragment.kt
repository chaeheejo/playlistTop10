package com.example.playlisttop10.songregisteration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.playlisttop10.R
import com.example.playlisttop10.UserRepository
import com.example.playlisttop10.databinding.FragmentPlaylistBinding


class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlistViewModel: PlaylistViewModel

    private lateinit var tv_title: TextView
    private lateinit var btn_register: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        playlistViewModel = ViewModelProvider(requireActivity())[PlaylistViewModel::class.java]
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        tv_title = binding.playlistTvTitle
        btn_register = binding.playlistBtnRegister
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var name = UserRepository.currUser!!.name
        tv_title.text = "$name's TOP10 List"

        btn_register.setOnClickListener{
            findNavController().navigate(R.id.action_playlistFragment_to_registerSongFragment)
        }

    }
}