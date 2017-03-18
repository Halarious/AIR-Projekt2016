package hr.foi.air.international.servemepls.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.ServiceOrdersPageAdapter;

public class ServiceOrdersActivity extends AppCompatActivity
                                   implements ServiceListFragment.ViewOrderFragmentListener,
                                              ServiceExpandableListFragment.ServiceExpandableListFragmentListener
{
    private Toolbar customActionBar;

    public ViewPager                viewPager;
    public ServiceOrdersPageAdapter serviceOrdersPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_main);

        customActionBar = (Toolbar) findViewById(R.id.toolbar_tabbed);
        setSupportActionBar(customActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.service_orders_pager);
        serviceOrdersPageAdapter
                = new ServiceOrdersPageAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(serviceOrdersPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onView(int selectedIndex)
    {

    }
}
