package com.example.playlisttop10.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlisttop10.databinding.ObjectFavoritefriendBinding

class FavoriteFriendRecyclerViewAdapter(
    private val values: List<String>,
    private val onClick: View.OnClickListener
) : RecyclerView.Adapter<FavoriteFriendRecyclerViewAdapter.ViewHolder>() {

    val TAG: String = "FavoriteFriendRecyclerViewAdapter DEBUG:"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ObjectFavoritefriendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.tv_friendName.text = item
        holder.card_friendCard.setOnClickListener(onClick)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ObjectFavoritefriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_friendName: TextView = binding.objFavoritefriendTvFriend
        val card_friendCard: CardView = binding.objFavoritefriendCard

        override fun toString(): String {
            return super.toString() + " '" + tv_friendName.text + "'"
        }
    }
}