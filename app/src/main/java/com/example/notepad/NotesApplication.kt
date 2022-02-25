package com.example.notepad

import android.app.Application
import com.example.notepad.data.AppDatabase

// instantiate the database instance
class NotesApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

}