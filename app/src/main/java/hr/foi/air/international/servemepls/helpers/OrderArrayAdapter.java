package hr.foi.air.international.servemepls.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.ListitemOrderItem;
import hr.foi.air.international.servemepls.R;

public class OrderArrayAdapter extends ArrayAdapter<ListitemOrderItem>
{
    public interface OrderArrayAdapterListener
    {
        public void onRemoveOrder();
    }

    private Context context;
    private OrderArrayAdapterListener listener;
    private ArrayList<ListitemOrderItem> data;

    public OrderArrayAdapter(Context context, OrderArrayAdapterListener listener,
                             ArrayList<ListitemOrderItem> data)
    {
        super(context, R.layout.listitem_order, data);
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listitem_order, parent, false);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView  label     = (TextView) rowView.findViewById(R.id.label);
        TextView  count     = (TextView) rowView.findViewById(R.id.item_count);
        Button removeButton = (Button)   rowView.findViewById(R.id.button_remove);

        label.setText( data.get(position).label );
        count.setText( Integer.toString(data.get(position).count) );

        removeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                removeOrder(position);
                listener.onRemoveOrder();
            }
        });

        return rowView;
    }

    public ArrayList<ListitemOrderItem> getData()
    {
        return data;
    }

    public void addOrder(ListitemOrderItem item)
    {
        data.add(item);
        notifyDataSetChanged();
    }

    public void removeOrder(int index)
    {
        data.remove(index);
        notifyDataSetChanged();
    }
}
