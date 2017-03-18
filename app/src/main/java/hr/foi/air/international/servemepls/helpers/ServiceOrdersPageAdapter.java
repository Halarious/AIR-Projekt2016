package hr.foi.air.international.servemepls.helpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.views.ServiceExpandableListFragment;
import hr.foi.air.international.servemepls.views.ServiceListFragmentAdmin;

public class ServiceOrdersPageAdapter extends FragmentPagerAdapter
{
    private Context context;

    public ServiceOrdersPageAdapter(FragmentManager fragmentManager, Context context)
    {
        super(fragmentManager);

        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        switch(position)
        {
            case 0:
                fragment = new ServiceExpandableListFragment();
                break;
            case 1:
                fragment = new ServiceListFragmentAdmin();
                break;
            case 2:
                fragment = new ServiceListFragmentAdmin();
                break;
            default:
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return context.getString(R.string.tab_1);
            case 1:
                return context.getString(R.string.tab_my_orders);
            case 2:
                return context.getString(R.string.tab_2);
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
