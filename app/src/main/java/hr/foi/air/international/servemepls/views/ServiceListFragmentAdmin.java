package hr.foi.air.international.servemepls.views;


import android.view.MenuItem;

import hr.foi.air.international.servemepls.R;

public class ServiceListFragmentAdmin extends ServiceListFragment
{

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(super.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId())
        {
            case R.id.action_edit:
                showAddOrderDialogFragment(true);
                return true;
            case R.id.action_delete:
                adapter.removeSelectedItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        actionBar.findItem(R.id.action_view).setVisible(false);
        actionBar.findItem(R.id.action_edit).setVisible(false);
        actionBar.findItem(R.id.action_delete).setVisible(false);
    }

    @Override
    public void onRemove()
    {

    }
}
