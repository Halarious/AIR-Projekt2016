package hr.foi.air.international.servemepls.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.ServiceActiveOrderListAdapter;
import hr.foi.air.international.servemepls.helpers.SessionManager;

public class ServiceExpandableListFragment extends Fragment
{
    public interface ServiceExpandableListFragmentListener
    {

    }

    private Context             context;
    private Menu                actionBar;
    private SessionManager      sessionManager;

    private ServiceActiveOrderListAdapter         serviceActiveOrderListAdapter;
    private ServiceExpandableListFragmentListener serviceExpandableListFragmentListener;


    @Override
    public void onAttach(Context context)
    {
        this.context = context;

        try
        {
            serviceExpandableListFragmentListener
                    = (ServiceExpandableListFragmentListener) context;
        }
        catch (ClassCastException exception)
        {
            throw new ClassCastException(context.toString()
                    + " must implement ServiceExpandableListFragmentListener");
        }

        super.onAttach(context);
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
        return inflater.inflate(R.layout.fragment_service_expandable_list_view,
                                container,
                                false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        final ExpandableListView expandableListView
            = (ExpandableListView) getView().findViewById(R.id.service_active_orders_list);

        serviceActiveOrderListAdapter = new ServiceActiveOrderListAdapter(getActivity());

        expandableListView.setAdapter(serviceActiveOrderListAdapter);
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
                return true;
            case R.id.action_view:
                return true;
            case R.id.action_edit:
                return true;
            case R.id.action_delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*@Override
    public void onDialogPositiveClick(DialogFragment dialog,
                                      ArrayList<ListitemOrderItem> newOrder,
                                      int orderIndex)
    {
        //todo: The order is set and confirmed, send this to the server and refresh internal db.
        Order order = new Order(newOrder, sessionManager.getUID(), "6");
        if(orderIndex != -1)
            adapter.updateItem(orderIndex, order);
        else
            adapter.addItem(order);
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
    }*/
}
