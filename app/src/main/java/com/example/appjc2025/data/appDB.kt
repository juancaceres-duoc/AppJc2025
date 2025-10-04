package com.example.appjc2025.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [Usuario::class], version = 1)
abstract class AppDB:RoomDatabase() {
    abstract fun usuarioDAO() : UsuarioDAO
    companion object{
        @Volatile private var INSTANCE: AppDB? = null
        fun getDatabase(context: Context): AppDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, AppDB::class.java,"app_db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}