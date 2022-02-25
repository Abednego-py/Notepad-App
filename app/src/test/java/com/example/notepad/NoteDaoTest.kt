package com.example.notepad

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notepad.data.AppDatabase
import com.example.notepad.data.NoteDao
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.notepad.data.Note
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mock
import java.io.IOException
import kotlin.jvm.Throws


@RunWith(AndroidJUnit4::class)
class NoteDaoTest {
lateinit var noteDao: NoteDao
lateinit var db: AppDatabase

    @Mock
    lateinit var mockContext: Context
    @Before
    fun createDb() {



         db = Room.inMemoryDatabaseBuilder(mockContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        noteDao = db.noteDao()
    }
    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }
    @Test
    @Throws(Exception::class)
    fun insertAndGetNote() = runBlocking {
        val note = Note(userText = "This is a new note", time = "23/05/2021")
        noteDao.create(note)
        val allNotes = noteDao.getNotes().first()
        assertThat(allNotes.userText, equalTo(note.userText))
    }


}