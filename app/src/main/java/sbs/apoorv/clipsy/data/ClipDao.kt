package sbs.apoorv.clipsy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {
    @Query("SELECT * FROM clips ORDER BY timestamp DESC")
    fun getAll(): Flow<List<ClipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clip: ClipEntity)

    @Update
    suspend fun update(clip: ClipEntity)

    @Delete
    suspend fun delete(clip: ClipEntity)

    @Query("DELETE FROM clips")
    suspend fun deleteAll()
}
