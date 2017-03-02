package hr.foi.air.international.servemepls.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import hr.foi.air.international.servemepls.controllers.AppController;
import hr.foi.air.international.servemepls.helpers.SQLiteHandler;
import hr.foi.air.international.servemepls.helpers.SessionManager;

public class RegisterActivity extends AppCompatActivity
{
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Toolbar customActionBar;
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private Spinner  inputRole;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        customActionBar = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(customActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputFullName = (EditText)  findViewById(R.id.name);
        inputEmail =    (EditText)  findViewById(R.id.email);
        inputPassword = (EditText)  findViewById(R.id.password);
        inputRole =     (Spinner)   findViewById(R.id.user_category);
        btnRegister =   (Button)    findViewById(R.id.btnRegister);
        btnLinkToLogin =(Button)    findViewById(R.id.btnLinkToLoginScreen);

        //Drop down menu (TODO: Pull locations from the database)
        String[] items = new String[]{ "Employee", "Privileged"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        inputRole.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String name =     inputFullName.getText().toString().trim();
                String email =    inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String role =     inputRole.getSelectedItem().toString().trim();

                if (!name.isEmpty()     &&
                    !email.isEmpty()    &&
                    !password.isEmpty() &&
                    !role.isEmpty())
                {
                    registerUser(name, email, password, role);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Please enter ALL your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btnLinkToLogin.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password, role) to register url
     * TODO: Role is as of yet not used everywhere, waiting for upgrade to the database
     */
    private void registerUser(final String name, final String email,
                              final String password, final String role)
    {
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        //todo: this is hardcoded because 'AppConfig.URL_REGISTER' returns null for some reason. Investigate
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://192.168.12.1/android_login_api/register.php", new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "Register Response: " + response.toString());
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
                        String created_at = user.getString("created_at");

                        db.addUser(name, email, role, uid, created_at);

                        Toast.makeText(
                                getApplicationContext(),
                                "User successfully registered. Try login now!",
                                Toast.LENGTH_LONG
                        ).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
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
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "Registration Error: " + error.getMessage());
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
                params.put("name",  name);
                params.put("email", email);
                params.put("password", password);
                params.put("role",  role);

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
