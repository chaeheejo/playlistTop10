package com.example.playlisttop10.songregisteration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp


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

        btn_register.setOnClickListener{
            findNavController().navigate(R.id.action_playlistFragment_to_registerSongFragment)
        }

        cv_list.setContent {
            val scrollState = rememberScrollState()
            Column (
                Modifier
                    .padding(12.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ){
                element(title = "title")
            }
        }

    }

    @Composable
    fun element(title: String){
        Card(
            Modifier
                .padding(12.dp)
                .border(width = 2.dp, color = Color.Black)
                .height(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center){
                Text(text = title)
            }
        }
    }

}