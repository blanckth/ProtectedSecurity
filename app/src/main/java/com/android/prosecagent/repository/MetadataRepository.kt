import com.android.prosecagent.data.dao.DeviceMetadataDao
import com.android.prosecagent.data.entity.DeviceMetadata

class MetadataRepository(private val dao: DeviceMetadataDao) {
    suspend fun saveIfNotExists(metadata: DeviceMetadata) {
        if (dao.getMetadata() == null) {
            dao.insertMetadata(metadata)
        }
    }
}
