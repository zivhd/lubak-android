import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension to create DataStore
val Context.dataStore by preferencesDataStore(name = "user_prefs")

object DataStoreManager {
    private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")

    // Save JWT Token
    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[JWT_TOKEN_KEY] = token
        }
        Log.d("Tokens","Saved token $token")
    }

    // Get JWT Token
    fun getToken(context: Context): Flow<String?> {

        return context.dataStore.data.map { preferences ->
            Log.d("Token Get","${preferences[JWT_TOKEN_KEY]}")
            preferences[JWT_TOKEN_KEY]
        }

    }

    // Clear JWT Token (optional)
    suspend fun clearToken(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(JWT_TOKEN_KEY)
        }
    }
}
