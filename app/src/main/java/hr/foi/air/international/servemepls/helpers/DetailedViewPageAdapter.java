package hr.foi.air.international.servemepls.helpers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.models.Order;
import hr.foi.air.international.servemepls.views.DetailedOrderFragment;

public class DetailedViewPageAdapter extends FragmentStatePagerAdapter
{
    private ArrayList<Order> orders;
    private SessionManager   sessionManager;

    public DetailedViewPageAdapter(FragmentManager fragmentManager, SessionManager sessionManager,
                                   ArrayList<Order> orders)
    {
        super(fragmentManager);

        this.orders = orders;
        this.sessionManager = sessionManager;
    }

    @Override
    public Fragment getItem(int position)
    {

        Bundle arguments = new Bundle();
        Order order = orders.get(position);
        arguments.putSerializable(DetailedOrderFragment.ORDER, order);
        arguments.putBoolean     (DetailedOrderFragment.EDITABLE,
                                  (sessionManager.isAdmin()                     ||
                                   sessionManager.getUID().equals(order.UID))
                                 );

        Fragment orderFragment = new DetailedOrderFragment();
        orderFragment.setArguments(arguments);

        return orderFragment;
    }

    @Override
    public int getCount()
    {
        return orders.size();
    }
}
