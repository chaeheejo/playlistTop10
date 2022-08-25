package com.example.playlisttop10.friend

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.playlisttop10.MainActivity
import com.example.playlisttop10.R
import com.example.playlisttop10.Song
import com.example.playlisttop10.UserRepository
import com.example.playlisttop10.databinding.FragmentUserListBinding

class UserListFragment : Fragment() {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private lateinit var userListViewModel: UserListViewModel

    private lateinit var cv_users: ComposeView
    private lateinit var btn_playlist: ImageButton
    private lateinit var btn_friends: ImageButton
    private lateinit var btn_like: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userListViewModel = ViewModelProvider(requireActivity())[UserListViewModel::class.java]
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        cv_users = binding.userCvUsers
        btn_playlist = binding.userBtnPlaylist
        btn_friends = binding.userBtnFriends
        btn_like = binding.userBtnLike

        userListViewModel.loadUserList()

        btn_friends.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_playlist.setOnClickListener {
            findNavController().navigate(R.id.action_userListFragment_to_playlistFragment)
        }

        btn_like.setOnClickListener {
            findNavController().navigate(R.id.action_userListFragment_to_likedFriendFragment)
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                UserRepository.currUser = null
                findNavController().navigate(R.id.action_userListFragment_to_loginFragment)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        userListViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if (it != null && it != "") {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })

        userListViewModel.isUserListLoaded.observe(viewLifecycleOwner, Observer {
            if (it) {
                setUserListAtComposeView()
            }
        })
    }

    private fun setUserListAtComposeView() {
        cv_users.setContent {
            val userList = userListViewModel.userList
            Column(
                Modifier
                    .padding(10.dp)
            ) {
                UserElementList(userList.toList())
            }
        }
    }

    private fun onClick(id: String) {
        val playlist = userListViewModel.getPlaylistById(id)

        if (playlist != null) {
            cv_users.setContent {
                Column(
                    Modifier
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$id's TOP10 playlist",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Medium
                    )
                    PlaylistElementList(playlist = playlist)
                }
            }
        } else {
            cv_users.setContent {
                Column(
                    Modifier
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "empty playlist",
                        fontSize = 33.sp
                    )
                }
            }
        }

    }

    @Composable
    fun UserElementList(
        elementList: List<String>
    ) {
        LazyColumn(content = {
            items(count = elementList.size) {
                UserElement(elementList[it])
            }
        })
    }

    @Composable
    fun UserElement(id: String) {
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
                    text = id, fontSize = 33.sp,
                    fontWeight = FontWeight.Medium
                )
//                Row{
//                    Text(
//                        text = user.myLikeNumber.toString(), fontSize = 20.sp
//                    )
//                    FavoriteButton(modifier = Modifier.padding(8.dp))
//                }
            }
        }
    }

    @Composable
    fun FavoriteButton(
        modifier: Modifier = Modifier,
        color: Color = Color(0xffE91E63)
    ) {

        var isFavorite by remember { mutableStateOf(false) }

        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = {
                isFavorite = !isFavorite
            }
        ) {
            Icon(
                tint = color,
                modifier = modifier.graphicsLayer {
                    scaleX = 1.3f
                    scaleY = 1.3f
                },
                imageVector = if (isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null
            )
        }
    }

    @Composable
    fun PlaylistElementList(
        playlist: List<Song>
    ) {
        LazyColumn(content = {
            items(count = playlist.size) {
                PlaylistElement(playlist[it])
            }
        })
    }

    @Composable
    fun PlaylistElement(song: Song) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
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
                    text = song.title, fontSize = 33.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = song.album, fontSize = 20.sp
                )
                Text(
                    text = song.singer, fontSize = 20.sp
                )
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}