package com.android.prosecagent.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.prosecagent.data.dao.DeviceMetadataDao
import com.android.prosecagent.data.entity.DeviceMetadataEntity

/**
 * Main Room database class for ProtectedSecurityAgent.
 * Contains metadata table and can be extended with more entities.
 */
@Database(
    entities = [DeviceMetadataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun metadataDao(): DeviceMetadataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Singleton pattern to get the database instance.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "protected_security_db"
                )
                    .fallbackToDestructiveMigration() // for now; can be changed
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
