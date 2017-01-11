package hr.foi.air.international.servemepls.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import hr.foi.air.international.servemepls.R;

public class ClientFragmentQR extends Fragment
{
    public interface ClientFragmentQRListener
    {
        public void onQRScanned();
    }

    private Context context;
    private Menu    actionBar;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_client, container, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.qr_image_prompt);
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                callQRScanner();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        this.actionBar = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void callQRScanner()
    {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null)
            {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show();
            } else
            {
                Toast.makeText(context, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                clientFragmentQRListener.onQRScanned();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
