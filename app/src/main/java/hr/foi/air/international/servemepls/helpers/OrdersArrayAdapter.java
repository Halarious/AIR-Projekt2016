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

import hr.foi.air.international.servemepls.models.ListitemOrderItem;
import hr.foi.air.international.servemepls.models.Order;
import hr.foi.air.international.servemepls.R;

public class OrdersArrayAdapter<T> extends ArrayAdapter<T>
{
    public interface OrdersArrayAdapterListener
    {
        void onSelect();
        void onDeselect();
        void onRemove();
    }

    private final Context      context;
    private final ArrayList<T> data;
    private       T            selectedItem = null;
    private       View         selectedView = null;
    private OrdersArrayAdapterListener ordersArrayAdapterListener;

    public OrdersArrayAdapter(Context context, ArrayList<T> data, OrdersArrayAdapterListener ordersArrayAdapterListener)
    {
        super(context, R.layout.listitem_order, data);
        this.context = context;
        this.data = data;
        this.ordersArrayAdapterListener = ordersArrayAdapterListener;
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
                if(position == getSelectedItemIndex())
                    deselectItem();
                removeItem(position);
                ordersArrayAdapterListener.onRemove();
            }
        });


        return rowView;
    }

    public ArrayList<T> getData()
    {
        return data;
    }

    public void addItem(T item)
    {
        data.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int index)
    {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void removeSelectedItem()
    {
        data.remove(data.indexOf(selectedItem));
        deselectItem();
        notifyDataSetChanged();
    }

    public void updateItem(int index, T item)
    {
        if(item instanceof ListitemOrderItem)
        {
        }
        else
        if(item instanceof Order)
        {
            Order order = (Order)data.get(index);
            order.orderItems.clear();
            for(ListitemOrderItem orderItem : ((Order) item).orderItems)
                order.orderItems.add(orderItem);
        }
    }

    public void setSelectedItem(int index, View view)
    {
        selectedItem = data.get(index);
        selectedView = view;
        selectedView.setSelected(true);
        ordersArrayAdapterListener.onSelect();
    }

    public void deselectItem()
    {
        selectedItem = null;
        selectedView.setSelected(false);
        selectedView = null;
        ordersArrayAdapterListener.onDeselect();
    }

    public T getSelectedItem()
    {
        return selectedItem;
    }

    public int getSelectedItemIndex()
    {
        return data.indexOf(selectedItem);
    }
}
