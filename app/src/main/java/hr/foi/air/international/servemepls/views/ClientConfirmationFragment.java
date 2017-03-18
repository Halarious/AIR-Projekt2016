package hr.foi.air.international.servemepls.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;

public class ClientConfirmationFragment extends Fragment
{
    public interface ClientConfirmationFragmentListener
    {
        void onConfirmOrder(String jsonOrder);
    }

    public static final String TAG = ClientConfirmationFragment.class.getSimpleName();

    private Context context;
    private ClientConfirmationFragmentListener clientConfirmationFragmentListener;
    private ArrayList<ListitemOrderItem> order;

    @Override
    public void onAttach(Context context)
    {
        this.context = context;
        clientConfirmationFragmentListener = (ClientConfirmationFragmentListener)context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)context).getSupportActionBar().hide();
        Bundle receivedBundle = getArguments();
        if(receivedBundle != null)
        {
            if(receivedBundle.containsKey("Order"))
            {
                order = (ArrayList<ListitemOrderItem>)receivedBundle.getSerializable("Order");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_client_confirmation, container, false);

        Button confirmButton = (Button) rootView.findViewById(R.id.button_confirm_order);
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clientConfirmationFragmentListener.onConfirmOrder("");
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView()
    {
        ((AppCompatActivity)context).getSupportActionBar().show();
        super.onDestroyView();
    }
}
