import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class ProtectedSecurityAgentApp : Application() {
    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getInstance(this@ProtectedSecurityAgentApp)
            val metadata = MetadataCollector(this@ProtectedSecurityAgentApp).collect()
            MetadataRepository(db.deviceMetadataDao()).saveIfNotExists(metadata)
        }
    }
}
