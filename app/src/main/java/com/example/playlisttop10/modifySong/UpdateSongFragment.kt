package com.example.playlisttop10.modifySong

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playlisttop10.R
import com.example.playlisttop10.Song
import com.example.playlisttop10.databinding.FragmentUpdateSongBinding

class UpdateSongFragment : Fragment() {
    private var _binding: FragmentUpdateSongBinding? = null
    private val binding get() = _binding!!
    private lateinit var updateSongViewModel: UpdateSongViewModel
    private val args: UpdateSongFragmentArgs by navArgs<UpdateSongFragmentArgs>()

    private lateinit var tv_title: TextView
    private lateinit var et_singer: EditText
    private lateinit var et_album: EditText
    private lateinit var btn_submit: Button

    private lateinit var title: String
    private lateinit var singer: String
    private lateinit var album: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        updateSongViewModel = ViewModelProvider(this)[UpdateSongViewModel::class.java]
        _binding = FragmentUpdateSongBinding.inflate(inflater, container, false)
        tv_title = binding.updateTvSongTitle
        et_singer = binding.updateEtSinger
        et_album = binding.updateEtAlbum
        btn_submit = binding.updateBtnSubmit

        title = args.title
        singer = args.singer
        album = args.album

        tv_title.text = title
        et_singer.setText(singer)
        et_album.setText(album)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_submit.setOnClickListener {
            updateSongViewModel.updateSongInformation(
                Song(title, singer, album),
                Song(tv_title.text.toString(), et_singer.text.toString(), et_album.text.toString())
            )
        }

        updateSongViewModel.isSongUpdated.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.action_updateSongFragment_to_playlistFragment)
            }
        })

        updateSongViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
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