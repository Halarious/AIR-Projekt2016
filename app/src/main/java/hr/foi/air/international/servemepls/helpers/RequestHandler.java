package hr.foi.air.international.servemepls.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hr.foi.air.international.servemepls.controllers.AppController;

public class RequestHandler
{
    public interface RequestHandlerListener
    {
        void onRequestResponse(String response);
    }

    private static final String  TAG     = SQLiteHandler.class.getSimpleName();
    private              Context context;

    public RequestHandler(Context context)
    {
        this.context = context;
    }

    public void sendPostRequest(final String url, final HashMap<String, String> params,
                                final RequestHandlerListener listener)
    {
        String tag_string_req = "req_sent";

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, "Login Response: " + response.toString());
                        listener.onRequestResponse(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText( context,
                                error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                })

                    {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            return params;
                        }

                    };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
