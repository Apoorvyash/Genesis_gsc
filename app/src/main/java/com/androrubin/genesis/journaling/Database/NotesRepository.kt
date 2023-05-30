package com.androrubin.genesis.journaling.Database

import androidx.lifecycle.LiveData
import com.androrubin.genesis.journaling.Model.Note


class NotesRepository (private val noteDao: JournelDao) {

    val allNotes : LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note){

        noteDao.insert(note)
    }

    suspend fun delete(note: Note)
    {
        noteDao.delete(note)
    }

    suspend fun update(note: Note){

        noteDao.update(note.id, note.title, note.note)
    }

}