package com.example.playlisttop10.modifySong

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.navArgs
import com.example.playlisttop10.R
import com.example.playlisttop10.Song
import com.example.playlisttop10.databinding.FragmentModifySongBinding
import com.example.playlisttop10.databinding.FragmentRegisterSongBinding
import kotlin.math.sin

class ModifySongFragment : Fragment() {
    private var _binding: FragmentModifySongBinding? = null
    private val binding get() = _binding!!
    private lateinit var modifySongViewModel: ModifySongViewModel
    private val args: ModifySongFragmentArgs by navArgs<ModifySongFragmentArgs>()

    private lateinit var et_title: EditText
    private lateinit var et_singer: EditText
    private lateinit var et_album: EditText
    private lateinit var btn_submit: Button

    private var title: String = args.title
    private var singer: String = args.singer
    private var album: String = args.album

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        modifySongViewModel = ViewModelProvider(this)[ModifySongViewModel::class.java]
        _binding = FragmentModifySongBinding.inflate(inflater, container, false)
        et_title = binding.modifyEtTitle
        et_singer = binding.modifyEtSinger
        et_album = binding.modifyEtAlbum
        btn_submit = binding.modifyBtnSubmit

        et_title.setText(title)
        et_singer.setText(singer)
        et_album.setText(album)

        return inflater.inflate(R.layout.fragment_modify_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_submit.setOnClickListener {
            modifySongViewModel.modifySongInformation(Song(title, singer, album))
        }
    }
}