package hr.foi.air.international.servemepls.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.controllers.AppConfig;
import hr.foi.air.international.servemepls.controllers.AppController;
import hr.foi.air.international.servemepls.helpers.SQLiteHandler;
import hr.foi.air.international.servemepls.helpers.SessionManager;

public class LoginActivity extends Activity
{
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button         btnLogin;
    private Button         btnLinkToRegister;
    private EditText       inputEmail;
    private EditText       inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler  db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail        = (EditText) findViewById(R.id.email);
        inputPassword     = (EditText) findViewById(R.id.password);
        btnLogin          = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn())
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                String email    = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty())
                    checkLogin(email, password);
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        btnLinkToRegister.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void checkLogin(final String email, final String password)
    {
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error)
                    {
                        String uid = jObj.getString("uid");

                        JSONObject user   = jObj.getJSONObject("user");
                        String name       = user.getString("name");
                        String email      = user.getString("email");
                        String role       = user.getString("role");
                        String created_at = user
                                .getString("created_at");

                        db.addUser(name, email, role, uid, created_at);

                        session.setUID(uid);
                        if(role.equals("Privileged"))
                            session.setLogin(true, true);
                        else
                            session.setLogin(true, false);

                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(),
                                   Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog()
    {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog()
    {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
