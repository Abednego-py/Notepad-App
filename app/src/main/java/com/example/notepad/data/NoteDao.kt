package com.example.notepad.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    suspend fun create(note : Note)

    @Query("select * from Note")
    fun getNotes() : Flow<List<Note>>

    @Query("SELECT * from Note WHERE id = :id")
    fun getNote(id : Int) : Flow<Note>

    @Query("select * from Note where userText like :searchquery or title like :searchquery or time like :searchquery")
    fun searchDatabase(searchquery : String) : Flow<List<Note>>

    @Query("delete from Note")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteNote(note : Note)

    @Update
    suspend fun update(note : Note)
}