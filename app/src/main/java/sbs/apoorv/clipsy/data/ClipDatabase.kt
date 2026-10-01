package sbs.apoorv.clipsy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ClipEntity::class], version = 1, exportSchema = false)
abstract class ClipDatabase : RoomDatabase() {
    abstract fun clipDao(): ClipDao

    companion object {
        @Volatile
        private var INSTANCE: ClipDatabase? = null

        fun getInstance(context: Context): ClipDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ClipDatabase::class.java,
                    "clipsy.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
