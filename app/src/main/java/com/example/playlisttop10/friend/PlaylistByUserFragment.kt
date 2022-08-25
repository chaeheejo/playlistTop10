package com.example.playlisttop10.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlisttop10.R
import com.example.playlisttop10.databinding.FragmentPlaylistByUserBinding
import com.example.playlisttop10.databinding.FragmentUserListBinding

class PlaylistByUserFragment : Fragment() {
    private var _binding: FragmentPlaylistByUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlistByUserViewModel: PlaylistByUserViewModel

    private lateinit var cv_users: ComposeView
    private lateinit var btn_playlist: ImageButton
    private lateinit var btn_friends: ImageButton
    private lateinit var btn_like: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        playlistByUserViewModel = ViewModelProvider(requireActivity())[PlaylistByUserViewModel::class.java]
        _binding = FragmentPlaylistByUserBinding.inflate(inflater, container, false)

        btn_friends.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue))

        return binding.root
    }

}