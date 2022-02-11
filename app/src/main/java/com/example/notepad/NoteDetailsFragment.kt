package com.example.notepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.notepad.data.Note
import com.example.notepad.databinding.FragmentNoteDetailsBinding
import com.example.notepad.viewModels.NoteViewModel
import com.example.notepad.viewModels.NoteViewModelFactory


class NoteDetailsFragment : Fragment() {

    private val args: NoteDetailsFragmentArgs by navArgs()

    private lateinit var _binding: FragmentNoteDetailsBinding
    private val binding get() = _binding


    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NotesApplication).database
                .noteDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.customNote.userText = binding.note.text.toString()
        binding.time.text = args.customNote.time
        args.customNote.title = binding.title.text.toString()

        binding.check.setOnClickListener {
            val updatedNote = Note(
                userText = binding.note.text.toString(),
                title = binding.title.text.toString(),
                time = binding.time.text.toString()
            )
            viewModel.updateNote(updatedNote)
        }
    }


}