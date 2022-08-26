package com.example.playlisttop10.friend

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.playlisttop10.R
import com.example.playlisttop10.UserRepository
import com.example.playlisttop10.databinding.FragmentFriendsBinding

class FriendsFragment : Fragment() {
    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private lateinit var friendsViewModel: FriendsViewModel

    private lateinit var cv_friends: ComposeView
    private lateinit var btn_playlist: ImageButton
    private lateinit var btn_friends: ImageButton
    private lateinit var btn_like: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        friendsViewModel = ViewModelProvider(requireActivity())[FriendsViewModel::class.java]
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)

        cv_friends = binding.friendsCvList
        btn_playlist = binding.friendsBtnPlaylist
        btn_friends = binding.friendsBtnFriends
        btn_like = binding.friendsBtnFavorite

        friendsViewModel.loadFriends()
        friendsViewModel.loadMyFavoriteFriendList()

        btn_friends.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_playlist.setOnClickListener {
            findNavController().navigate(R.id.action_friendsFragment_to_playlistFragment)
        }

        btn_like.setOnClickListener {
            findNavController().navigate(R.id.action_friendsFragment_to_favoriteFriendFragment)
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                UserRepository.clear()
                findNavController().navigate(R.id.action_friendsFragment_to_loginFragment)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        friendsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if (it != null && it != "") {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })

        friendsViewModel.isFriendListLoaded.observe(viewLifecycleOwner, Observer {
            if (it) {
                cv_friends.setContent {
                    val friendIdList = friendsViewModel.friendIdList
                    friendIdList.remove(UserRepository.currUser!!.id)

                    Column(
                        Modifier
                            .padding(10.dp)
                    ) {
                        ElementList(friendIdList)
                    }
                }
            }
        })
    }

    private fun onClick(id: String) {
        val action = FriendsFragmentDirections.actionFriendsFragmentToPlaylistByUserFragment(id)
        findNavController().navigate(action)
    }

    @Composable
    fun ElementList(
        elementList: List<String>
    ) {
        LazyColumn(content = {
            items(count = elementList.size) {
                Element(elementList[it])
            }
        })
    }

    @Composable
    fun Element(id: String) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable { onClick(id) },
            elevation = 2.dp,
            backgroundColor = Color.White,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = id, fontSize = 25.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}