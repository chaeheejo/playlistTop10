package com.example.playlisttop10.playlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.playlisttop10.R
import com.example.playlisttop10.UserRepository
import com.example.playlisttop10.databinding.FragmentPlaylistBinding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.example.playlisttop10.Song


class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlistViewModel: PlaylistViewModel

    private lateinit var tv_title: TextView
    private lateinit var cv_list: ComposeView
    private lateinit var btn_register: ImageButton
    private lateinit var btn_playlist: ImageButton
    private lateinit var btn_friends: ImageButton
    private lateinit var btn_like: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        playlistViewModel = ViewModelProvider(requireActivity())[PlaylistViewModel::class.java]
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        tv_title = binding.playlistTvTitle
        cv_list = binding.playlistCvList
        btn_register = binding.playlistBtnRegister
        btn_playlist = binding.playlistBtnPlaylist
        btn_friends = binding.playlistBtnFriends
        btn_like = binding.playlistBtnFavorite

        btn_playlist.setColorFilter(getColor(requireContext(), R.color.light_blue))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = UserRepository.currUser!!.name
        tv_title.text = "$name's TOP10 playlist"

        playlistViewModel.loadMySongs()

        cv_list.setContent {
            val songList = playlistViewModel.songList
            Column(
                Modifier
                    .padding(10.dp)
            ) {
                ElementList(songList)
            }
        }

        btn_register.setOnClickListener {
            findNavController().navigate(R.id.action_playlistFragment_to_registerSongFragment)
        }

        btn_friends.setOnClickListener {
            findNavController().navigate(R.id.action_playlistFragment_to_allUsersFragment)
        }

        btn_like.setOnClickListener {
            findNavController().navigate(R.id.action_playlistFragment_to_favoriteFriendFragment)
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                UserRepository.clear()
                findNavController().navigate(R.id.action_playlistFragment_to_loginFragment)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun onClick(song: Song) {
        val action = PlaylistFragmentDirections.actionPlaylistFragmentToUpdateSongFragment(
            song.title,
            song.singer,
            song.album
        )
        findNavController().navigate(action)
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
                .fillMaxWidth()
                .clickable { onClick(song) },
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}