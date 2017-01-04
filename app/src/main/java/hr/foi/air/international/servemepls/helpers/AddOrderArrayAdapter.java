package hr.foi.air.international.servemepls.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.AvailableItem;
import hr.foi.air.international.servemepls.R;

public class AddOrderArrayAdapter extends ArrayAdapter
{
    private Context context;
    private ArrayList<AvailableItem> itemList;

    public int datasetTag;

    public AddOrderArrayAdapter(Context context, int textViewResourceId, ArrayList<AvailableItem> itemList)
    {
        super(context, textViewResourceId);
        this.context=context;
        this.itemList=itemList;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView text = (TextView) row.findViewById(R.id.spinner_item);
        text.setText(itemList.get(position).label);

        return row;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount()
    {
        return itemList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position)
    {
        return itemList.get(position);
    }

    public int getItemPosition(String itemName)
    {
        for(int index = 0; index < itemList.size(); ++index)
        {
            AvailableItem item = itemList.get(index);
            if(item.label.equals(itemName))
                return index;
        }
        return -1;
    }
}
