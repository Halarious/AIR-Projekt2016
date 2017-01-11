package hr.foi.air.international.servemepls.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;

public class ClientConfirmationFragment extends Fragment
{
    private Context context;
    private ArrayList<ListitemOrderItem> order;

    @Override
    public void onAttach(Context context)
    {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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
        View view = inflater.inflate(R.layout.fragment_client_confirmation, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
}
