package hr.foi.air.international.servemepls.views;

import android.content.Context;
import android.content.Intent;
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

abstract public class ServiceListFragment extends Fragment
                                          implements AddOrderDialogFragment.AddOrderDialogListener,
                                                     OrdersArrayAdapter.OrdersArrayAdapterListener
{
    public interface ViewOrderFragmentListener
    {
        void onView(int selectedIndex);
    }

    //todo: Maybe not necessary, check this
    FragmentActivity    context;
    OrdersArrayAdapter  adapter;
    Menu                actionBar;
    SessionManager      sessionManager;

    ViewOrderFragmentListener viewOrderFragmentListener;

    @Override
    public void onAttach(Context context)
    {
        this.context = (FragmentActivity) context;

        try
        {
            viewOrderFragmentListener = (ViewOrderFragmentListener) context;
        }
        catch (ClassCastException exception)
        {

        }

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
            case R.id.action_view:
                viewOrderFragmentListener.onView(adapter.getSelectedItemIndex());
                return true;
            case R.id.action_edit:
                showAddOrderDialogFragment(true);
                return true;
            case R.id.action_delete:
                adapter.removeSelectedItem();
                return true;
            case R.id.action_logout:
                sessionManager.clearPreferences();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                context.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(context);
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

        //todo: Fetch data from sqlite database(internal)
        ArrayList<ListitemOrderItem> sampleOrder = new ArrayList<ListitemOrderItem>();
        sampleOrder.add(new ListitemOrderItem("Drinks", "Booze", 2));
        sampleOrder.add(new ListitemOrderItem("Food"  , "Grub" , 5));

        Button addOrderButton = (Button) getView().findViewById(R.id.button_new_order);
        addOrderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAddOrderDialogFragment(false);
            }
        });

        final ListView listView = (ListView) getView().findViewById(R.id.orders_list);
        adapter = new OrdersArrayAdapter(getActivity(), new ArrayList<Order>(), this);
        adapter.addItem(new Order(sampleOrder, sessionManager.getUID()));
        adapter.addItem(new Order(sampleOrder, sessionManager.getUID()));
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
        //todo: The order is set and confirmed, send this to the server and refresh internal db.
        Order order = new Order(newOrder, sessionManager.getUID());
        if(orderIndex != -1)
            adapter.updateItem(orderIndex, order);
        else
            adapter.addItem(order);
    }

    @Override
    public void onSelect()
    {
        actionBar.findItem(R.id.action_view).setVisible(true);
        actionBar.findItem(R.id.action_edit).setVisible(true);
        actionBar.findItem(R.id.action_delete).setVisible(true);
    }

    @Override
    public void onDeselect()
    {
        actionBar.findItem(R.id.action_view).setVisible(true);
        actionBar.findItem(R.id.action_edit).setVisible(false);
        actionBar.findItem(R.id.action_delete).setVisible(false);
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

        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "AddOrderDialogFragment");
    }
}