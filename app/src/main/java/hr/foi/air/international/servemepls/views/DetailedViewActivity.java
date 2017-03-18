package hr.foi.air.international.servemepls.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.DetailedViewPageAdapter;
import hr.foi.air.international.servemepls.helpers.SessionManager;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;
import hr.foi.air.international.servemepls.models.Order;

public class DetailedViewActivity extends AppCompatActivity
{
    private SessionManager sessionManager;
    private Toolbar        customActionBar;

    public ViewPager viewPager;
    public DetailedViewPageAdapter detailedViewPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        sessionManager = new SessionManager(getApplicationContext());

        customActionBar = (Toolbar) findViewById(R.id.toolbar_detailed_view);
        setSupportActionBar(customActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //todo Test data. Pull this from an actual source (the list). Try to get
        //     the actual refrences so changes are immediate
        ArrayList<ListitemOrderItem> sampleOrder = new ArrayList<ListitemOrderItem>();
        sampleOrder.add(new ListitemOrderItem("Drinks", "Booze", 2));
        sampleOrder.add(new ListitemOrderItem("Food"  , "Grub" , 5));
        ArrayList<ListitemOrderItem> sampleOrder2 = new ArrayList<ListitemOrderItem>();
        sampleOrder2.add(new ListitemOrderItem("Drinks2", "Booze2", 4));
        sampleOrder2.add(new ListitemOrderItem("Food2"  , "Grub2" , 7));
        ArrayList<Order> orders = new ArrayList<Order>();
        orders.add(new Order(sampleOrder, sessionManager.getUID(), "1"));
        orders.add(new Order(sampleOrder2, "1a", "2"));

        viewPager = (ViewPager) findViewById(R.id.detailed_view_pager);

        detailedViewPageAdapter = new DetailedViewPageAdapter(getSupportFragmentManager(),
                                                              sessionManager,
                                                              orders);
        viewPager.setAdapter(detailedViewPageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                //todo: Check it this works out as planned
                Object o = viewPager.getCurrentItem();
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        int seletedIndex = getIntent().getIntExtra("index", 0);
        viewPager.setCurrentItem(seletedIndex);
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
}