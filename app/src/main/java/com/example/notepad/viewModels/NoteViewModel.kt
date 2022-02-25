package com.example.notepad.viewModels

import android.content.ClipData
import androidx.lifecycle.*
import com.example.notepad.data.Note
import com.example.notepad.data.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {

    val allNotes : LiveData<List<Note>> = noteDao.getNotes().asLiveData()

    fun allNotes() : Flow<List<Note>> = noteDao.getNotes()

    fun updateNote(note: Note){
        viewModelScope.launch {
            noteDao.update(note)
        }
    }

    fun searchItem(searchQuery : String) : LiveData<List<Note>>{
        val search = "%$searchQuery%"
        return noteDao.searchDatabase(search).asLiveData()
    }

    /**
     * Retrieve an item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<Note> {
        return noteDao.getNote(id).asLiveData()
    }

    fun updatedNote(title : String, time: String, userText : String, id : Int){
        val newNote = getUpdatedNote(title, time, userText,id)
        updateNote(newNote)
    }
    fun addNewNote(title : String, time: String, userText : String) {
        val newNote = getNewNote(title, time, userText)
        createNote(newNote)
    }
    private fun createNote(note : Note) {
        viewModelScope.launch {
            noteDao.create(note)
        }
    }


    private fun getNewNote(title : String, time: String, userText : String) : Note{
        return Note(userText = userText,title = title,time = time)
    }


    private fun getUpdatedNote(title : String, time: String, userText : String, id : Int) : Note{
        return Note(userText = userText,title = title,time = time, id = id)
    }
    fun deleteNotes() {
        viewModelScope.launch {
            noteDao.deleteAll()
        }
    }

    fun deleteNote(note : Note) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }
}
/**
 * Factory class to instantiate the [ViewModel] instance.
 */

class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}