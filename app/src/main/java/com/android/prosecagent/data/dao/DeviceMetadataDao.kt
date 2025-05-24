package com.android.prosecagent.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.prosecagent.data.entity.DeviceMetadata

/**
 * DAO interface for accessing device metadata table.
 * Provides methods to insert and retrieve device metadata.
 */
@Dao
interface DeviceMetadataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: DeviceMetadata)

    @Query("SELECT * FROM device_metadata LIMIT 1")
    suspend fun getMetadata(): DeviceMetadata?
}