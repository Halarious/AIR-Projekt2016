package hr.foi.air.international.servemepls.helpers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.AvailableItem;

public class AddOrderArrayAdapter extends ArrayAdapter
{
    private Context context;
    private ArrayList<AvailableItem> itemList;

    public AddOrderArrayAdapter(Context context, int textViewResourceId, ArrayList<AvailableItem> itemList)
    {
        super(context, textViewResourceId);
        this.context=context;
        this.itemList=itemList;
    }

    public TextView getView(int position, View convertView, ViewGroup parent)
    {
        TextView view = (TextView) super.getView(position, convertView, parent);
        return view;
    }

    public TextView getDropDownView(int position, View convertView,
                                   ViewGroup parent)
    {
        return getView(position, convertView, parent);
    }
}
