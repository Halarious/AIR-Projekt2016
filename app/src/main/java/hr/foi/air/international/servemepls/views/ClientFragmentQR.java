package hr.foi.air.international.servemepls.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.controllers.AppConfig;
import hr.foi.air.international.servemepls.controllers.AppController;
import hr.foi.air.international.servemepls.helpers.ClientOrderHelper;
import hr.foi.air.international.servemepls.helpers.RequestHandler;

public class ClientFragmentQR extends Fragment implements RequestHandler.RequestHandlerListener
{
    public interface ClientFragmentQRListener
    {
        void onQRScanned(String content, RequestHandler.RequestHandlerListener requestHandlerListener);
        void onRequestOrder();
    }

    public static final String TAG = ClientFragmentQR.class.getSimpleName();

    private static class TableDetails
    {
        static String tableID     = "";
        static String orderStatus = "";
    }

    private Context         context;
    private ProgressDialog  pDialog;
    private Menu            actionBar;

    private ClientFragmentQRListener clientFragmentQRListener;

    @Override
    public void onAttach(Context context)
    {
        this.context = context;
        try
        {
            clientFragmentQRListener = (ClientFragmentQRListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_client, container, false);

        ImageView imageView = (ImageView)rootView.findViewById(R.id.qr_image_prompt);
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                callQRScanner();
            }
        });

        Button orderButton = (Button) rootView.findViewById(R.id.button_show_order);
        orderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clientFragmentQRListener.onRequestOrder();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        this.actionBar = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
        {
            if(result.getContents() == null)
            {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show();
            }
            else
            {
                showDialog();

                String content = result.getContents();

                clientFragmentQRListener.onQRScanned(content, this);
                ClientOrderHelper.getInstance().putQR(content);
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    @Override
    public void onRequestResponse(String response)
    {
        boolean error;
        try
        {
            JSONObject jsonResponse = new JSONObject(response);

            error = jsonResponse.getBoolean("error");
            if (!error)
            {
                String tid      = jsonResponse.getString("tid");
                String status   = jsonResponse.getString("status");

                TableDetails.tableID     = tid;
                TableDetails.orderStatus = status;

                onResponseSuccessful();

                //todo We (s)could receive the available items every read

                //todo If there is an order is the response (maybe check it with status but probably
                //     not the job for here)
                //if()
                //    ClientOrderHelper.getInstance().clearOrder();
            }
            else
            {
                String errorMsg = jsonResponse.getString("error_msg");
                Toast.makeText(context, "Error: " + errorMsg,
                        Toast.LENGTH_LONG).show();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(context, "Json error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        hideDialog();
    }

    private void onResponseSuccessful()
    {
        View rootView = getView();
        RelativeLayout tableDetailsLayout
                                  = (RelativeLayout) rootView.findViewById(R.id.table_details_wrapper);
        TextView tableIdText      = (TextView) rootView.findViewById(R.id.table_identifier_text);
        TextView tableStatusText  = (TextView) rootView.findViewById(R.id.table_order_status_text);
        Button orderButton        = (Button) rootView.findViewById(R.id.button_show_order);

        tableIdText    .setText(TableDetails.tableID);
        tableStatusText.setText(TableDetails.orderStatus);
        //todo Figure out where to put the state strings, depending where we will use them.
        //     For now the plan is to only use them in this fragment and destroy them upon closing
        switch(TableDetails.orderStatus)
        {
            case "awaiting":
                orderButton.setText("Place order");
                break;
            case "queueing":
                orderButton.setText("Edit order");
                break;
            case "placed":
                orderButton.setText("View order");
                break;
            default:
        }

        tableDetailsLayout.setVisibility(View.VISIBLE);
    }

    public void callQRScanner()
    {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

}
