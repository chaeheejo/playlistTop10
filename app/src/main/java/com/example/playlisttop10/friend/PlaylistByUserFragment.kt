package com.example.playlisttop10.friend

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
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
import androidx.navigation.fragment.navArgs
import com.example.playlisttop10.R
import com.example.playlisttop10.Song
import com.example.playlisttop10.UserRepository
import com.example.playlisttop10.databinding.FragmentPlaylistByUserBinding

class PlaylistByUserFragment : Fragment() {
    private var _binding: FragmentPlaylistByUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlistByUserViewModel: PlaylistByUserViewModel
    private val args: PlaylistByUserFragmentArgs by navArgs<PlaylistByUserFragmentArgs>()

    private lateinit var cv_list: ComposeView
    private lateinit var btn_playlist: ImageButton
    private lateinit var btn_users: ImageButton
    private lateinit var btn_favorite: ImageButton

    private var _isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        playlistByUserViewModel =
            ViewModelProvider(requireActivity())[PlaylistByUserViewModel::class.java]
        _binding = FragmentPlaylistByUserBinding.inflate(inflater, container, false)

        cv_list = binding.byUserCvList
        btn_playlist = binding.byUserBtnPlaylist
        btn_users = binding.byUserBtnUsers
        btn_favorite = binding.byUserBtnFavorite

        btn_users.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_playlist.setOnClickListener {
            findNavController().navigate(R.id.action_playlistByUserFragment_to_playlistFragment)
        }

        btn_users.setOnClickListener {
            findNavController().navigate(R.id.action_playlistByUserFragment_to_allUsersFragment)
        }

        btn_favorite.setOnClickListener {
            findNavController().navigate(R.id.action_playlistByUserFragment_to_favoriteFriendFragment)
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                UserRepository.clear()
                findNavController().navigate(R.id.action_playlistByUserFragment_to_loginFragment)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        playlistByUserViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if (it != null && it != "") {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })

        val playlist = playlistByUserViewModel.getPlaylistById(args.id)
        val favoriteFriendList = playlistByUserViewModel.getMyFavoriteFriendList()
        _isFavorite = args.id in favoriteFriendList

        if (playlist != null) {
            cv_list.setContent {
                Column(
                    Modifier
                        .padding(10.dp)
                ) {
                    Text(
                        text = args.id + "'s playlist",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    FavoriteButton(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 5.dp)
                    )
                    ElementList(playlist)
                }
            }
        } else {
            cv_list.setContent {
                Column {
                    Text(
                        text = args.id + "'s playlist is empty",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

    private fun onClick(isFavorite: Boolean) {
        if (isFavorite) {
            playlistByUserViewModel.tryAddFavoriteFriend(args.id)
        } else {
            playlistByUserViewModel.tryDeleteFavoriteFriend(args.id)
        }
    }


    @Composable
    fun ElementList(
        elementList: List<Song>
    ) {
        LazyColumn(content = {
            items(count = elementList.size) {
                Element(elementList[it], it + 1)
            }
        })
    }

    @Composable
    fun Element(song: Song, index: Int) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            elevation = 2.dp,
            backgroundColor = Color.White,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Row(
                Modifier.padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = index.toString(), fontSize = 20.sp
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = song.title, fontSize = 25.sp,
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
    }

    @Composable
    fun FavoriteButton(
        modifier: Modifier,
        color: Color = Color(0xffE91E63)
    ) {

        var isFavorite by remember { mutableStateOf(_isFavorite) }

        IconToggleButton(
            modifier = modifier,
            checked = isFavorite,
            onCheckedChange = {
                isFavorite = !isFavorite
                onClick(isFavorite)
            }
        ) {
            Icon(
                tint = color,
                modifier = Modifier.graphicsLayer {
                    scaleX = 1.5f
                    scaleY = 1.5f
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


