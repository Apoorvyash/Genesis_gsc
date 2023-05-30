package com.androrubin.genesis.journaling.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androrubin.genesis.journaling.Model.Note
import com.androrubin.genesis.utilities.DATABASE_NAME


@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract  class NoteDatabase :  RoomDatabase(){

    abstract fun getNoteDao() : JournelDao

    companion object{
        @Volatile
        private  var INSTANCE : NoteDatabase? = null

        fun getDatabase(context : Context): NoteDatabase{

            return INSTANCE?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance
                instance
            }
        }

    }

}