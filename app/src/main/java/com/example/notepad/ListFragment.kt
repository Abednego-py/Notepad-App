package com.example.notepad

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.data.Note
import com.example.notepad.databinding.FragmentListBinding
import com.example.notepad.recycler.NoteAdapter
import com.example.notepad.viewModels.NoteViewModel
import com.example.notepad.viewModels.NoteViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ListFragment : Fragment(R.layout.fragment_list), SearchView.OnQueryTextListener {

    private var _binding : FragmentListBinding? = null

    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NotesApplication).database
                .noteDao()
        )
    }

    private lateinit var notesAdapter : NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListBinding.bind(view)

      notesAdapter = NoteAdapter{
          val action = ListFragmentDirections.actionListFragmentToNoteDetailsFragment(it)
          this.findNavController().navigate(action)
      }

        val mLayoutManager = LinearLayoutManager(requireContext())
        mLayoutManager.reverseLayout = true;
        mLayoutManager.stackFromEnd = true;
        
        binding.apply {
            recyclerList.apply {
                adapter = notesAdapter
                layoutManager = mLayoutManager
            }
        }

        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.allNotes.observe(viewLifecycleOwner){
            notesAdapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToNoteFragment()
            findNavController().navigate(action)
        }



    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.app_bar_search)
        val searchView  = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }



    override fun onOptionsItemSelected(item: MenuItem)
            = when(item.itemId){
        R.id.delete -> {
            showConfirmationDialog()
            true
        }
        R.id.app_bar_search -> {
            true
        }
        else ->{
            super.onOptionsItemSelected(item)
        }
    }
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage("Do you want to delete All note?")
            .setCancelable(false)
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                deleteAllNotes()
            }
            .show()
    }

    fun searchNotes(query: String){
        viewModel.searchItem(query).observe(viewLifecycleOwner) {
                list -> list.let {
            notesAdapter.submitList(it)
        }
        }
    }

    private fun deleteAllNotes(){
        viewModel.deleteNotes()
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        if(p0 != null){
            searchNotes(p0)
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
