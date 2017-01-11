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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.ClientListAdapter;
import hr.foi.air.international.servemepls.models.AvailableItem;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;

public class ClientListFragment extends Fragment
                                implements ClientListAdapter.ClientListListener
{
    public interface ClientListListener
    {
        void onPlaceOrder(ArrayList<ListitemOrderItem> order);
    }

    private Context            context;
    private Menu               actionBar;
    private ClientListAdapter  adapter;
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
                    + " must implement NoticeDialogListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        //todo: Test data, this should come from the database. Implement
        ArrayList<AvailableItem> data = new ArrayList<>();
        data.add(new AvailableItem("Booze", 1));
        data.add(new AvailableItem("Grub", 0));
        data.add(new AvailableItem("Booze2", 1));
        data.add(new AvailableItem("Grub2", 0));
        data.add(new AvailableItem("Booze3", 1));
        super.onCreate(savedInstanceState);

        //todo: Super horrible, improve this
        Collections.sort(data, new Comparator<AvailableItem>() {
            public int compare(AvailableItem first_operand, AvailableItem second_operand) {
                return first_operand.category < second_operand.category ?
                        -1 : 1;
            }
        });

        adapter = new ClientListAdapter(getActivity(), data, this);
        adapter.addSeparatorItem(0, new AvailableItem());
        for(int index = 1; index < data.size(); ++index)
        {
            if( ((index+1) != data.size()) &&
                (data.get(index).category != data.get(index+1).category)
              )
            {
                adapter.addSeparatorItem(index+1, new AvailableItem());
                ++index;
            }
        }
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
        listView.setAdapter(adapter);
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
                ArrayList<ListitemOrderItem> order = adapter.getSelectedData();
                clientListListener.onPlaceOrder(order);
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