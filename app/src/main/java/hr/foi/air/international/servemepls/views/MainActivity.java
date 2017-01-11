package hr.foi.air.international.servemepls.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.ClientListAdapter;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;


public class MainActivity extends AppCompatActivity
                          implements ClientFragmentQR.ClientFragmentQRListener,
                                     ClientListFragment.ClientListListener
{
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
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar customActionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(customActionBar);

        if(savedInstanceState != null)
            return;

        Fragment fragment = new ClientFragmentQR();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_top,
                android.support.v7.appcompat.R.anim.abc_slide_in_bottom);
        fragmentTransaction.add(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onPlaceOrder(ArrayList<ListitemOrderItem> order)
    {
        Fragment fragment = new ClientConfirmationFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("Order", order);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_top,
                android.support.v7.appcompat.R.anim.abc_slide_in_bottom);
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onQRScanned()
    {
        Fragment fragment = new ClientListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_top,
                                                android.support.v7.appcompat.R.anim.abc_slide_in_bottom);
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }
}