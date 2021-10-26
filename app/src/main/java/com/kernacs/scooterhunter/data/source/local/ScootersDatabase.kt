package com.kernacs.scooterhunter.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kernacs.scooterhunter.data.entity.ScooterEntity

@Database(
    entities = [ScooterEntity::class],
    version = 1,
    exportSchema = true
)
abstract class ScootersDatabase : RoomDatabase() {
    abstract fun scootersDao(): ScootersDao

    companion object {

        @Volatile
        private var instance: ScootersDatabase? = null

        fun getDatabase(context: Context): ScootersDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, ScootersDatabase::class.java, "ScootersDatabase")
                .fallbackToDestructiveMigration()
                .build()

    }
}