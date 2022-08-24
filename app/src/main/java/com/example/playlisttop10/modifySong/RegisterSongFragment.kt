package com.example.playlisttop10.modifySong

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
import androidx.navigation.fragment.findNavController
import com.example.playlisttop10.R
import com.example.playlisttop10.Song
import com.example.playlisttop10.databinding.FragmentRegisterSongBinding

class RegisterSongFragment : Fragment() {
    private var _binding: FragmentRegisterSongBinding? = null
    private val binding get() = _binding!!
    private lateinit var registerSongViewModel: RegisterSongViewModel

    private lateinit var et_title: EditText
    private lateinit var et_singer: EditText
    private lateinit var et_album: EditText
    private lateinit var btn_submit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerSongViewModel = ViewModelProvider(this)[RegisterSongViewModel::class.java]
        _binding = FragmentRegisterSongBinding.inflate(inflater, container, false)
        et_title = binding.registerEtTitle
        et_singer = binding.registerEtSinger
        et_album = binding.registerEtAlbum
        btn_submit = binding.registerBtnSubmit
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_submit.setOnClickListener {
            val title = et_title.text.toString()
            val singer = et_singer.text.toString()
            val album = et_album.text.toString()

            val song = Song(title, singer, album)

            registerSongViewModel.tryRegisterSong(song)
        }

        registerSongViewModel.isSongRegistered.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.action_registerSongFragment_to_playlistFragment)
            }
        })

        registerSongViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if (it != null && it != "") {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}