package com.androrubin.genesis.journaling.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.androrubin.genesis.journaling.Model.Note


@Dao
interface JournelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note : Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("Select * from notes_table order by id ASC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query(" UPDATE notes_table Set title=:title,note = :note WHERE id= :id")
    suspend fun update(id: Int?, title: String?,note :String? )
}