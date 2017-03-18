package hr.foi.air.international.servemepls.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import hr.foi.air.international.servemepls.R;

public class ServiceActiveOrderListAdapter extends BaseExpandableListAdapter
{
    private Context context;

    public ServiceActiveOrderListAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getGroupCount()
    {
        return 3;// todo ServiceOrderHelper.getInstance().getNumberOfOrders();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return 2;// todo ServiceOrderHelper.getInstance().getNumberOfItemsInOrder(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup)
    {
        String orderText = "Order_#";// todo (String) getGroup(groupPosition);

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.service_active_orders_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.service_active_orders_list_group_text);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(orderText);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b,
                             View convertView, ViewGroup viewGroup)
    {
        final String itemText = "item_#";// todo: (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.service_active_orders_list_item, null);
        }

        TextView itemTextView = (TextView) convertView
                .findViewById(R.id.service_active_orders_list_item_text);
        itemTextView.setText(itemText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1)
    {
        return false;
    }
}
