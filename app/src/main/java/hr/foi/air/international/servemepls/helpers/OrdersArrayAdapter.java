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

import hr.foi.air.international.servemepls.ListItemOrder;
import hr.foi.air.international.servemepls.R;

public class OrdersArrayAdapter extends ArrayAdapter<ListItemOrder>
{
    private final Context context;
    private final ArrayList<ListItemOrder> data;

    public OrdersArrayAdapter(Context context, ArrayList<ListItemOrder> data)
    {
        super(context, R.layout.listitem_order, data);
        this.context = context;
        this.data = data;
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
                data.remove(position);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }

    //todo: necessary or not? Will the list update and redraw automatically?
    public void addOrder()
    {
        notifyDataSetChanged();
    }
}
