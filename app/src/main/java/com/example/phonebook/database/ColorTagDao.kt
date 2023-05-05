package com.example.phonebook.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ColorTagDao {
    @Query("SELECT * FROM ColorTagDbModel")
    fun getAll(): LiveData<List<ColorTagDbModel>>

    @Query("SELECT * FROM ColorTagDbModel")
    fun getAllSync(): List<ColorTagDbModel>

    @Query("SELECT * FROM ColorTagDbModel ORDER BY RANDOM() LIMIT 1")
    fun getRandomColor(): List<ColorTagDbModel?>


    @Insert
    fun insertAll(vararg colorTagDbModels: ColorTagDbModel)
}