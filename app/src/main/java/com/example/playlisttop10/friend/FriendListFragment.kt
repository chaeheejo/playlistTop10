package com.example.playlisttop10.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.playlisttop10.R
import com.example.playlisttop10.databinding.FragmentFriendListBinding
import com.example.playlisttop10.databinding.FragmentPlaylistBinding
import com.example.playlisttop10.playlist.PlaylistViewModel

class FriendListFragment : Fragment() {
    private var _binding: FragmentFriendListBinding? = null
    private val binding get() = _binding!!
    private lateinit var friendListViewModel: FriendListViewModel

    private lateinit var btn_home: ImageButton
    private lateinit var btn_friends: ImageButton
    private lateinit var btn_like: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        friendListViewModel = ViewModelProvider(requireActivity())[FriendListViewModel::class.java]
        _binding = FragmentFriendListBinding.inflate(inflater, container, false)

        btn_home = binding.friendBtnHome
        btn_friends = binding.friendBtnFriends
        btn_like = binding.friendBtnLike

        btn_friends.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_home.setOnClickListener {
            findNavController().navigate(R.id.action_friendListFragment_to_playlistFragment)
        }

        btn_like.setOnClickListener {
            findNavController().navigate(R.id.action_friendListFragment_to_likedFriendFragment)
        }
    }
}