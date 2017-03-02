package hr.foi.air.international.servemepls.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager
{
    private static String TAG = SessionManager.class.getSimpleName();

    SharedPreferences        preferences;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME       = "ServeMePlsCredentials";
    private static final String KEY_UID         = "uid";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_ADMIN    = "isAdmin";

    public SessionManager(Context context)
    {
        this.context  = context;
        preferences   = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor        = preferences.edit();
    }

    public void setUID(String uid)
    {
        editor.putString(KEY_UID, uid);
        editor.commit();
        Log.d(TAG, "UID modified");
    }

    public void setLogin(boolean isLoggedIn, boolean isAdmin)
    {
        editor.putBoolean(KEY_IS_LOGGEDIN,  isLoggedIn);
        editor.putBoolean(KEY_IS_ADMIN,     isAdmin);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void clearPreferences()
    {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn()
    {
        return preferences.getBoolean(KEY_IS_LOGGEDIN, false);
    }
    public boolean isAdmin()
    {
        return preferences.getBoolean(KEY_IS_LOGGEDIN, false);
    }
    public String  getUID()
    {
        return preferences.getString(KEY_UID, "");
    }
}
