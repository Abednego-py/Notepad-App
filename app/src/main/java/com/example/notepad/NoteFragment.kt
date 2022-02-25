package com.example.notepad

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.transition.Visibility
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notepad.data.Note
import com.example.notepad.databinding.FragmentNoteBinding
import com.example.notepad.viewModels.NoteViewModel
import com.example.notepad.viewModels.NoteViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class NoteFragment : Fragment() {

    private lateinit var _binding : FragmentNoteBinding

    private val SPEECH_REQUEST_CODE = 0

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
        _binding = FragmentNoteBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get the current time
        binding.time.text = getDate()

        binding.speechButton.setOnClickListener {
            displaySpeechRecognizer()
        }

    }


    private fun saveNoteInFragment(){
        val title = binding.title.text.toString()
        val userText = binding.note.text.toString()
        val time = binding.time.text.toString()

        viewModel.addNewNote(userText = userText,title = title,time = time)

        val action = NoteFragmentDirections.actionNoteFragmentToListFragment()
        findNavController().navigate(action)
    }




    //function to get the current date and time
    private fun getDate(): String {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

        return current.format(formatter)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.check -> {
                saveNoteInFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    var isEditing = false


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.check)

        item.isVisible = isEditing

        if(binding.note.text.isNotEmpty() || binding.title.text.isNotEmpty()){
            item.isVisible = true

        }
        updateOptionsMenu()
    }

    private fun updateOptionsMenu() {
        requireActivity().invalidateOptionsMenu()
    }

    // Create an intent that can start the Speech Recognizer activity
    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        }
        // This starts the activity and populates the intent with the speech text.
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results?.get(0)
                }
            if (binding.title.isFocused) {
                binding.title.setText(spokenText)
            }
            binding.note.setText(spokenText)

            if(binding.note.text.isNotEmpty()){
                val note = binding.note.text.toString()
                val newNote = "$note $spokenText"
                binding.note.setText(newNote)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)

    }

}