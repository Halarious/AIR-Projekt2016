package hr.foi.air.international.servemepls.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.ListitemOrderItem;
import hr.foi.air.international.servemepls.Order;
import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.OrdersArrayAdapter;

public class TestListFragment extends Fragment
                              implements AddOrderDialogFragment.AddOrderDialogListener,
                                         OrdersArrayAdapter.OrdersArrayAdapterListener
{
    //todo: Maybe not necessary, check this
    private FragmentActivity myContext;
    private ArrayList<Order> orders = new ArrayList<Order>();

    public OrdersArrayAdapter adapter;

    @Override
    public void onAttach(Context context)
    {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        Button addOrderButton = (Button) getView().findViewById(R.id.button_new_order);
        addOrderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAddOrderDialogFragment();
            }
        });

        //todo: Fetch data from sqlite database(internal)
        ArrayList<ListitemOrderItem> sampleOrder = new ArrayList<ListitemOrderItem>();
        sampleOrder.add(new ListitemOrderItem("Drinks", "Booze", 2));
        sampleOrder.add(new ListitemOrderItem("Food"  , "Grub" , 5));
        orders.add(new Order(sampleOrder));

        final ListView listView = (ListView) getView().findViewById(R.id.orders_list);
        adapter = new OrdersArrayAdapter(getActivity(), orders, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Order selectedOrder = (Order)adapterView.getItemAtPosition(i);
                showAddOrderDialogFragment(selectedOrder);
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog,
                                      ArrayList<ListitemOrderItem> newOrder)
    {
        //todo: The order is set and confirmed, send this to the server.
        orders.add(new Order(newOrder));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRemoveButton(int orderPosition)
    {
        orders.remove(orderPosition);
        adapter.notifyDataSetChanged();
    }

    public void showAddOrderDialogFragment()
    {
        //todo: Get data from local sqlite db(??) and send it to the fragment
        //The question is when to update the database/sync with the server
        DialogFragment dialog = new AddOrderDialogFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "AddOrderDialogFragment");
    }

    public void showAddOrderDialogFragment(Order order)
    {
        //todo: Get data from local sqlite db(??) and send it to the fragment
        //The question is when to update the database/sync with the server
        Bundle bundle = new Bundle();
        bundle.putSerializable("SampleKey", order);

        DialogFragment dialog = new AddOrderDialogFragment();
        dialog.setArguments(bundle);
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "AddOrderDialogFragment");
    }
}