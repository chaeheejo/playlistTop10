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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlisttop10.R
import com.example.playlisttop10.UserRepository
import com.example.playlisttop10.databinding.FragmentFavoriteFriendBinding

class FavoriteFriendFragment : Fragment() {
    private var _binding: FragmentFavoriteFriendBinding? = null
    private val binding get() = _binding!!
    private lateinit var likedFriendViewModel: FavoriteFriendViewModel

    private lateinit var btn_playlist: ImageButton
    private lateinit var btn_friends: ImageButton
    private lateinit var btn_like: ImageButton
    private lateinit var rv_friendsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        likedFriendViewModel = ViewModelProvider(requireActivity())[FavoriteFriendViewModel::class.java]
        _binding = FragmentFavoriteFriendBinding.inflate(inflater, container, false)

        btn_playlist = binding.favoriteBtnPlaylist
        btn_friends = binding.favoriteBtnFriends
        btn_like = binding.favoriteBtnFavorite
        rv_friendsList = binding.favoriteRvFriendsList

        btn_like.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue))

        with(rv_friendsList){
            layoutManager = LinearLayoutManager(context)
            adapter = FavoriteFriendRecyclerViewAdapter(likedFriendViewModel.getFriendList())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_playlist.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFriendFragment_to_playlistFragment)
        }

        btn_friends.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFriendFragment_to_friendsFragment)
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                UserRepository.clear()
                findNavController().navigate(R.id.action_favoriteFriendFragment_to_loginFragment)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}