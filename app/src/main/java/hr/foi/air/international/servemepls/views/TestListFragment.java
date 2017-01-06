package hr.foi.air.international.servemepls.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.models.ListitemOrderItem;
import hr.foi.air.international.servemepls.models.Order;
import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.OrdersArrayAdapter;
import hr.foi.air.international.servemepls.helpers.SessionManager;

public class TestListFragment extends Fragment
                              implements AddOrderDialogFragment.AddOrderDialogListener,
                                         OrdersArrayAdapter.OrdersArrayAdapterListener
{
    //todo: Maybe not necessary, check this
    private FragmentActivity    context;
    private OrdersArrayAdapter  adapter;
    private Menu                actionBar;

    @Override
    public void onAttach(Context context)
    {
        this.context = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        this.actionBar = menu;
        inflater.inflate(R.menu.menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                showAddOrderDialogFragment(false);
                return true;
            case R.id.action_edit:
                showAddOrderDialogFragment(true);
                return true;
            case R.id.action_delete:
                adapter.removeSelectedItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SessionManager session = new SessionManager(context);
        setHasOptionsMenu(true);
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
                showAddOrderDialogFragment(false);
            }
        });

        //todo: Fetch data from sqlite database(internal)
        ArrayList<ListitemOrderItem> sampleOrder = new ArrayList<ListitemOrderItem>();
        sampleOrder.add(new ListitemOrderItem("Drinks", "Booze", 2));
        sampleOrder.add(new ListitemOrderItem("Food"  , "Grub" , 5));

        final ListView listView = (ListView) getView().findViewById(R.id.orders_list);
        adapter = new OrdersArrayAdapter(getActivity(), new ArrayList<Order>(), this);
        adapter.addItem(new Order(sampleOrder));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l)
            {
                adapter.setSelectedItem(index, view);
            }
        });

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog,
                                      ArrayList<ListitemOrderItem> newOrder,
                                      int orderIndex)
    {
        //todo: The order is set and confirmed, send this to the server.
        Order order = new Order(newOrder);
        if(orderIndex != -1)
            adapter.updateItem(orderIndex, order);
        else
        {
            adapter.addItem(order);
        }
    }

    @Override
    public void onSelect()
    {
        actionBar.findItem(R.id.action_edit).setVisible(true);
        actionBar.findItem(R.id.action_delete).setVisible(true);
    }

    @Override
    public void onDeselect()
    {
        actionBar.findItem(R.id.action_edit).setVisible(false);
        actionBar.findItem(R.id.action_delete).setVisible(false);
    }

    @Override
    public void onRemove()
    {

    }

    public void showAddOrderDialogFragment(boolean edit)
    {
        DialogFragment dialog = new AddOrderDialogFragment();
        if(edit)
        {
            Bundle bundle = new Bundle();
            bundle.putInt("OrderIndex", adapter.getSelectedItemIndex());
            bundle.putSerializable("Order", (Order)adapter.getSelectedItem());
            dialog.setArguments(bundle);
        }
        //todo: Get data from local sqlite db(??) and send it to the fragment
        //The question is when to update the database/sync with the server
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "AddOrderDialogFragment");
    }
}