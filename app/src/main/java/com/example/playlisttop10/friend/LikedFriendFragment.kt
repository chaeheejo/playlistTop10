package com.example.playlisttop10.friend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.playlisttop10.R
import com.example.playlisttop10.UserRepository
import com.example.playlisttop10.databinding.FragmentLikedFriendBinding

class LikedFriendFragment : Fragment() {
    private var _binding: FragmentLikedFriendBinding? = null
    private val binding get() = _binding!!
    private lateinit var likedFriendViewModel: LikedFriendViewModel

    private lateinit var btn_playlist: ImageButton
    private lateinit var btn_friends: ImageButton
    private lateinit var btn_like: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        likedFriendViewModel = ViewModelProvider(requireActivity())[LikedFriendViewModel::class.java]
        _binding = FragmentLikedFriendBinding.inflate(inflater, container, false)

        btn_playlist = binding.likedBtnPlaylist
        btn_friends = binding.likedBtnFriends
        btn_like = binding.likedBtnLike

        btn_like.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_playlist.setOnClickListener {
            findNavController().navigate(R.id.action_likedFriendFragment_to_playlistFragment)
        }

        btn_friends.setOnClickListener {
            findNavController().navigate(R.id.action_likedFriendFragment_to_userListFragment)
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                UserRepository.currUser = null
                findNavController().navigate(R.id.action_likedFriendFragment_to_loginFragment)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}