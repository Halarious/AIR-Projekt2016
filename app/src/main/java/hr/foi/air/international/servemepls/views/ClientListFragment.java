package hr.foi.air.international.servemepls.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.ClientListAdapter;
import hr.foi.air.international.servemepls.helpers.OrdersArrayAdapter;
import hr.foi.air.international.servemepls.models.AvailableItem;
import hr.foi.air.international.servemepls.models.Order;

public class ClientListFragment extends Fragment
                                implements ClientListAdapter.ClientListListener
{
    private Context           context;
    private Menu              actionBar;
    private ClientListAdapter adapter;

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

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list_client, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        ListView listView = (ListView) getView().findViewById(R.id.orders_list);
        adapter = new ClientListAdapter(getActivity(), new ArrayList<AvailableItem>(), this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l)
            {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        this.actionBar = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }
}