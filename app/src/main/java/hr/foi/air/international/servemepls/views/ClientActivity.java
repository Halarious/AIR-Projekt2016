package hr.foi.air.international.servemepls.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.controllers.AppConfig;
import hr.foi.air.international.servemepls.helpers.ClientOrderHelper;
import hr.foi.air.international.servemepls.helpers.RequestHandler;
import hr.foi.air.international.servemepls.helpers.SessionManager;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;

public class ClientActivity extends AppCompatActivity
                            implements ClientListFragment.ClientListListener,
                                       ClientFragmentQR.ClientFragmentQRListener,
                                       FragmentManager.OnBackStackChangedListener,
                                       ClientConfirmationFragment.ClientConfirmationFragmentListener
{
    private Toolbar        customActionBar;
    private RequestHandler requestHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view);

        createAndSetupActionBar();

        requestHandler = new RequestHandler(this);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        Fragment fragment = new ClientFragmentQR();
        exchangeFragment(fragment);
    }

    @Override
    public void onBackStackChanged()
    {
        shouldDisplayHomeUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_client, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
            //todo: Put this in the fragment
            case R.id.action_place_order:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();

    }

    @Override
    public void onQRScanned(String content, RequestHandler.RequestHandlerListener listener)
    {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("qrcontent", content);

        requestHandler.sendPostRequest(AppConfig.URL_DECODE, params, listener);
    }

    @Override
    public void onRequestOrder()
    {
        Fragment fragment = new ClientListFragment();
        addFragment(fragment);
    }

    @Override
    public void onPlaceOrder(ArrayList<ListitemOrderItem> order)
    {
        Fragment fragment = new ClientConfirmationFragment();

        //todo: Do we want to allow this to ever happen?
        if(order == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putSerializable("Order", order);
        fragment.setArguments(bundle);

        addFragment(fragment);
    }

    @Override
    public void onConfirmOrder(String jsonOrder)
    {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("qrcontent", ClientOrderHelper.getInstance().getQR());
        params.put("order"    , jsonOrder);

        ClientFragmentQR listeningFragment
                = (ClientFragmentQR)getSupportFragmentManager()
                  .findFragmentByTag(ClientFragmentQR.class.getSimpleName());
        requestHandler.sendPostRequest(AppConfig.URL_DECODE, params, listeningFragment);

        getSupportFragmentManager().popBackStack(listeningFragment.TAG, 0);
    }

    public void shouldDisplayHomeUp()
    {
        boolean canBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canBack);
    }

    private void addFragment(Fragment fragment)
    {
        String backStateName = fragment.getClass().getSimpleName();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_top,
                                                android.support.v7.appcompat.R.anim.abc_slide_in_bottom,
                                                android.support.v7.appcompat.R.anim.abc_popup_enter,
                                                android.support.v7.appcompat.R.anim.abc_popup_exit);
        fragmentTransaction.add(R.id.client_fragment_container, fragment, backStateName)
                           .commit();
    }

    //todo: Check the difference between add and replace. This is used for refresh right now
    private void exchangeFragment(Fragment fragment)
    {
        String backStateName = fragment.getClass().getSimpleName();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_top,
                                                android.support.v7.appcompat.R.anim.abc_slide_in_bottom,
                                                android.support.v7.appcompat.R.anim.abc_popup_enter,
                                                android.support.v7.appcompat.R.anim.abc_popup_exit);
        fragmentTransaction.replace(R.id.client_fragment_container, fragment, backStateName)
                           .addToBackStack(backStateName)
                           .commit();
    }

    private void createAndSetupActionBar()
    {
        customActionBar = (Toolbar) findViewById(R.id.toolbar_client_view);
        setSupportActionBar(customActionBar);
    }
}
