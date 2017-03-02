package hr.foi.air.international.servemepls.views;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.SessionManager;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;


public class MainActivity extends AppCompatActivity
                          implements FragmentManager.OnBackStackChangedListener,
                                     ClientFragmentQR.ClientFragmentQRListener,
                                     ClientListFragment.ClientListListener,
                                     ServiceListFragment.ViewOrderFragmentListener
{
    private Toolbar customActionBar;

    private DrawerLayout          drawerLayout;
    private ListView              drawerListView;
    private ArrayAdapter<String>  drawerAdapter;

    private SessionManager sessionManager;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
            return;

        sessionManager = new SessionManager(getApplicationContext());

        createAndSetupActionBar();

        createAndSetupDrawer();

        if(sessionManager.isAdmin())
        {
            //Fragment fragment = new ClientFragmentQR();
            fragment = new ServiceListFragmentAdmin();
        }
        else
        {
            fragment = new ServiceListFragmentAdmin();
        }
        exchangeFragment(fragment);
    }

    @Override
    protected void onDestroy()
    {
        //todo: I am not sure when the onDestroy methond is being called exactly,
        //      this might not be the correct way to handle logout at exit
        sessionManager.clearPreferences();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_favorite:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }    }

    @Override
    public void onBackPressed()
    {
        //moveTaskToBack(true);
        super.onBackPressed();
    }

    @Override
    public void onBackStackChanged()
    {
        //todo: Is this depricated?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onPlaceOrder(ArrayList<ListitemOrderItem> order)
    {
        Fragment fragment = new ClientConfirmationFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("Order", order);
        fragment.setArguments(bundle);

        exchangeFragment(fragment);
    }

    @Override
    public void onQRScanned(String QRContent)
    {
        Fragment fragment = new ClientListFragment();
        exchangeFragment(fragment);
    }

    @Override
    public void onView(int selectedIndex)
    {
        Intent intent = new Intent(MainActivity.this,
                                   DetailedViewActivity.class);
        //todo: Manage the keys in a more sane fashion
        intent.putExtra("index", selectedIndex);
        startActivity(intent);
    }

    private void addFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_top,
                android.support.v7.appcompat.R.anim.abc_slide_in_bottom);
        fragmentTransaction.add(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    //todo: Check the difference between add and replace. This is used for refresh
    private void exchangeFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_top,
                android.support.v7.appcompat.R.anim.abc_slide_in_bottom);
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }

    private void createAndSetupActionBar()
    {
        customActionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(customActionBar);
    }

    private void createAndSetupDrawer()
    {
        drawerLayout   = (DrawerLayout) findViewById(R.id.layout_drawer);
        drawerListView = (ListView)findViewById(R.id.navigation_drawer);

        //todo: The menu is just a set of hardcoded strings for now, find the
        //      best solution to build it
        String[] drawerMenuItems = { "Register", "Make me powerful" };
        drawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerMenuItems);
        drawerListView.setAdapter(drawerAdapter);

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                String selection = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, selection,
                                                  Toast.LENGTH_SHORT).show();
                switch(selection)
                {
                    case "Register":
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        break;
                    case "Make me powerful":
                        sessionManager.setLogin(true, true);
                        addFragment(fragment);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}