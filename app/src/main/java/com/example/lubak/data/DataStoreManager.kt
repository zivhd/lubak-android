import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lubak.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension to create DataStore
val Context.dataStore by preferencesDataStore(name = "user_prefs")

object DataStoreManager {
    private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
    private val USER_KEY = stringPreferencesKey("user_data")
    private val gson = Gson()
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

    suspend fun saveUser(context:Context,user:User){
        val userJson = gson.toJson(user)
        context.dataStore.edit{preferences->
            preferences[USER_KEY] = userJson
        }
        Log.d("User", "Saved user: $userJson")
    }

    fun getUser(context: Context): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            val userJson = preferences[USER_KEY]
            Log.d("User Get", "Retrieved user JSON: $userJson")
            userJson?.let { gson.fromJson(it, User::class.java) } // Deserialize JSON to User
        }
    }

    // Clear User data
    suspend fun clearUser(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
        }
        Log.d("User", "Cleared user data")
    }

}
