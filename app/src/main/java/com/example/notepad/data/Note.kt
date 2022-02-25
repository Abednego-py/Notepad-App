package com.example.notepad.data

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @NonNull val userText : String,
    @NonNull val title : String,
    @NonNull val time : String
) : Parcelable
