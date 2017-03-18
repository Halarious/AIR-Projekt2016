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
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.ClientListAdapter;
import hr.foi.air.international.servemepls.helpers.ClientOrderHelper;
import hr.foi.air.international.servemepls.models.AvailableItem;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;

public class ClientListFragment extends Fragment
{
    public interface ClientListListener
    {
        void onPlaceOrder(ArrayList<ListitemOrderItem> order);
    }

    public static final String TAG = ClientListFragment.class.getSimpleName();

    private Context            context;
    private Menu               actionBar;
    private ClientListAdapter  clientListAdapter;
    private ClientListListener clientListListener;

    @Override
    public void onAttach(Context context)
    {
        this.context = context;
        try
        {
            clientListListener = (ClientListListener) context;
        } catch (ClassCastException e)
        {

            throw new ClassCastException(context.toString()
                    + " must implement ClientListListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        //todo: Test data, this should come from the database, or bundle
        //      parse here and save (after sort) in internal DB for recovery purposes.
        //      Actually also consider if this can be pre-sorted on the server or something
        ArrayList<AvailableItem> data = new ArrayList<>();
        data.add(new AvailableItem("Booze", 1));
        data.add(new AvailableItem("Grub", 0));
        data.add(new AvailableItem("Booze2", 1));
        data.add(new AvailableItem("Grub2", 0));
        data.add(new AvailableItem("Booze3", 1));

        clientListAdapter = new ClientListAdapter(getActivity(), data);

        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
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
        listView.setAdapter(clientListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l)
            {

            }
        });
        Button orderButton = (Button)getView().findViewById(R.id.client_order_button);
        orderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ArrayList<ListitemOrderItem> order = clientListAdapter.getSelectedData();
                clientListListener.onPlaceOrder(order);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        ClientOrderHelper.getInstance().clearOrder();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        this.actionBar = menu;

        inflater.inflate(R.menu.menu_client, menu);
        menu.findItem(R.id.action_place_order).setVisible(true);
    }
}