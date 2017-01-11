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

    private static final String PREF_NAME = "AndroidHiveLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context)
    {
        this.context  = context;
        preferences   = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor        = preferences.edit();
    }

    public void setLogin(boolean isLoggedIn)
    {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn()
    {
        return preferences.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
