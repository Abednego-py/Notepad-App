package com.example.notepad

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notepad.data.Note
import com.example.notepad.databinding.FragmentNoteDetailsBinding
import com.example.notepad.viewModels.NoteViewModel
import com.example.notepad.viewModels.NoteViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        viewModel.retrieveItem(args.customNote.id).observe(
            this.viewLifecycleOwner
        ) { note ->
            bind(note)
        }


    }

    private fun updateNoteInFragment() {
        val userText = binding.notes.text.toString()
        val title = binding.title.text.toString()
        val time = binding.time.text.toString()

        viewModel.updatedNote(title, time, userText, args.customNote.id)

        val action = NoteDetailsFragmentDirections.actionNoteDetailsFragmentToListFragment()
        findNavController().navigate(action)
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage("Do you want to delete this note?")
            .setCancelable(false)
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                deleteItem()
            }
            .show()
    }

    /**
     * Deletes the current item and navigates to the list fragment.
     */

    private fun deleteItem() {
        viewModel.deleteNote(args.customNote)
        findNavController().navigateUp()
    }

    private fun bind(note: Note) {
        binding.apply {
            title.setText(note.title)
            time.text = note.time
            notes.setText(note.userText)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.delete -> {
                showConfirmationDialog()
                true
            }
            R.id.check -> {
                updateNoteInFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}