package com.example.playlisttop10.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playlisttop10.R
import com.example.playlisttop10.Song
import com.example.playlisttop10.databinding.FragmentPlaylistByUserBinding

class PlaylistByUserFragment : Fragment() {
    private var _binding: FragmentPlaylistByUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlistByUserViewModel: PlaylistByUserViewModel
    private val args: PlaylistByUserFragmentArgs by navArgs<PlaylistByUserFragmentArgs>()

    private lateinit var cv_list: ComposeView
    private lateinit var btn_playlist: ImageButton
    private lateinit var btn_friends: ImageButton
    private lateinit var btn_favorite: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        playlistByUserViewModel =
            ViewModelProvider(requireActivity())[PlaylistByUserViewModel::class.java]
        _binding = FragmentPlaylistByUserBinding.inflate(inflater, container, false)

        cv_list = binding.byUserCvList
        btn_playlist = binding.byUserBtnPlaylist
        btn_friends = binding.byUserBtnFriends
        btn_favorite = binding.byUserBtnFavorite

        btn_friends.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_blue))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_playlist.setOnClickListener {
            findNavController().navigate(R.id.action_playlistByUserFragment_to_playlistFragment)
        }

        btn_favorite.setOnClickListener {
            findNavController().navigate(R.id.action_playlistByUserFragment_to_favoriteFriendFragment)
        }

        val playlist = playlistByUserViewModel.getPlaylistById(args.id)

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
                    ElementList(playlist)
                }
            }
        } else {
            cv_list.setContent {
                Column {
                    Text(
                        text = args.id + "'s playlist is empty",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(top = 24.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }
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
}