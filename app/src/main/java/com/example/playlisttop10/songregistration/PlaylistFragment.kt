package com.example.playlisttop10.songregistration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
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
import com.example.playlisttop10.Song


class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlistViewModel: PlaylistViewModel

    private lateinit var tv_title: TextView
    private lateinit var btn_register: ImageButton
    private lateinit var cv_list: ComposeView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        playlistViewModel = ViewModelProvider(requireActivity())[PlaylistViewModel::class.java]
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        tv_title = binding.playlistTvTitle
        btn_register = binding.playlistBtnRegister
        cv_list = binding.playlistCvList
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = UserRepository.currUser!!.name
        tv_title.text = "$name's TOP10 List"

        btn_register.setOnClickListener {
            findNavController().navigate(R.id.action_playlistFragment_to_registerSongFragment)
        }

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
    }

    @Composable
    fun ElementList(
        elementList: List<Song>
    ) {
        LazyColumn(content = {
            items(count = elementList.size) {
                Element(elementList[it])
            }
        })
    }

    @Composable
    fun Element(song: Song) {
        Card(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth(),
            elevation = 2.dp,
            backgroundColor = Color.White,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Row{
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.CenterVertically)
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

    }
}