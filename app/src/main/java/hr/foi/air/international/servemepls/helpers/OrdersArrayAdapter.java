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
import hr.foi.air.international.servemepls.Order;
import hr.foi.air.international.servemepls.R;

public class OrdersArrayAdapter<T> extends ArrayAdapter<T>
{
    private final Context      context;
    private final ArrayList<T> data;
    private OrdersArrayAdapterListener ordersArrayAdapterListener;

    public OrdersArrayAdapter(Context context, ArrayList<T> data, OrdersArrayAdapterListener ordersArrayAdapterListener)
    {
        super(context, R.layout.listitem_order, data);
        this.context = context;
        this.data = data;
        this.ordersArrayAdapterListener = ordersArrayAdapterListener;
    }

    public interface OrdersArrayAdapterListener
    {
        void onRemoveButton(int itemPosition);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listitem_order, parent, false);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.icon);
        TextView label      = (TextView) rowView.findViewById(R.id.label);
        TextView  count     = (TextView) rowView.findViewById(R.id.item_count);
        Button removeButton = (Button)   rowView.findViewById(R.id.button_remove);

        Object item = data.get(position);
        if(item instanceof ListitemOrderItem)
        {
            label.setText( ((ListitemOrderItem)item).label );
            count.setText( Integer.toString(((ListitemOrderItem)item).count) );
        }
        else
        if(item instanceof Order)
        {
            label.setText( "Booze" );
            count.setText( "1" );
        }

        removeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ordersArrayAdapterListener.onRemoveButton(position);
            }
        });


        return rowView;
    }
}
