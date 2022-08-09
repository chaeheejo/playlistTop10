package com.example.playlisttop10.songregisteration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.playlisttop10.R
import com.example.playlisttop10.databinding.FragmentPlaylistBinding


class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val playlistViewModel: PlaylistViewModel = PlaylistViewModel()
    private val args: PlaylistFragmentArgs by navArgs<PlaylistFragmentArgs>()

    private var name: String =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val id = args.userId

        playlistViewModel.getReceivedName().observe(viewLifecycleOwner, Observer{
            binding.playTextTitle.text = it+"'s TOP10 List"
        })

        binding.registerBtn.setOnClickListener{
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_playlistFragment_to_registerSongFragment)
        }

    }

    private fun makeToast(str: String) {
        var toast: String = ""
        when (str) {
            "name" -> toast = "사용자의 이름 불러오기에 실패하였습니다."
        }
        Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
    }
}