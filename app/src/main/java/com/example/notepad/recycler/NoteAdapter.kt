package com.example.notepad.recycler


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.DiffCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.data.Note
import com.example.notepad.databinding.ItemLayoutBinding

class NoteAdapter(private val onItemClicked: (Note) -> Unit) :
        ListAdapter<Note, NoteAdapter.NoteViewHolder>(DiffCallback){

            class NoteViewHolder(private var binding : ItemLayoutBinding) :
                    RecyclerView.ViewHolder(binding.root){

                        fun bind(note : Note){
                            binding.apply{

                                title.text = note.title
                                if(note.title == ""){
                                    title.text = note.userText.take(20)
                                }
                                time.text = note.time
                            }
                        }
                    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Note>(){
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
               return oldItem.time == newItem.time
            }


        }
    }
}