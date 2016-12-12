package hr.foi.air.international.servemepls.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.ListItemOrder;

public class TestListFragment extends ListFragment
                              implements AddOrderDialogFragment.AddOrderDialogListener
{
    //todo: Maybe not necessary, check this
    private FragmentActivity myContext;

    @Override
    public void onAttach(Context context)
    {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //todo: Fetch data from sqlite database(internal)
        ListItemOrder[] data = {
                                new ListItemOrder("Sample", 3),
                                new ListItemOrder("Sample 2", 1),
                                new ListItemOrder("Sample 3", 1)
                               };

        //OrdersArrayAdapter adapter = new OrdersArrayAdapter(getActivity(), data);
        setListAdapter(adapter);
    }

    public void showAddOrderDialogFragment()
    {
        //todo: Get data from local sqlite db(??) and send it to the fragment
        //The question is when to update the database/sync with the server
        String array[] = {"First", "Second"};
        Bundle bundle = new Bundle();
        bundle.putStringArray("SampleKey", array);
        //bundle.putSerializable("SampleKey", data);

        DialogFragment dialog = new AddOrderDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "AddOrderDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog,
                                      ArrayList<ListItemOrder> newOrder)
    {
        //todo: The order is set and confirmed, send this to the server.
    }
}
