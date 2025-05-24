class MetadataRepository(private val dao: DeviceMetadataDao) {
    suspend fun saveIfNotExists(metadata: DeviceMetadataEntity) {
        if (dao.getMetadata() == null) {
            dao.insert(metadata)
        }
    }
}
