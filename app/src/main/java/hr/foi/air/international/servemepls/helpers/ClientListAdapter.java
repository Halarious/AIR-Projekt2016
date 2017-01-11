package hr.foi.air.international.servemepls.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.models.AvailableItem;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;

public class ClientListAdapter extends ArrayAdapter<AvailableItem>
{
    public interface ClientListListener
    {
    }

    private String array[] = {"Food", "Drinks"};

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private final Context                  context;
    private final ArrayList<AvailableItem> data;
    private       int                      itemCounts[];
    private       TreeSet<Integer>         categorySeperators = new TreeSet<Integer>();
    private final ClientListListener       clientListListener;

    public ClientListAdapter(Context context, ArrayList<AvailableItem> data,
                             ClientListListener clientListListener)
    {
        super(context, R.layout.listitem_order_client, data);

        this.context    = context;
        this.data       = data;
        this.itemCounts = new int[data.size()];
        this.clientListListener = clientListListener;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

        AvailableItem item = data.get(position);
        int type = getItemViewType(position);
        switch (type)
        {
            case TYPE_ITEM:
                rowView = inflater.inflate(R.layout.listitem_order_client, null);

                TextView label = (TextView) rowView.findViewById(R.id.list_item_client_label);
                label.setText(item.label);

                TextView price = (TextView) rowView.findViewById(R.id.list_item_client_price);
                price.setText(item.price + "kn");

                TextView count = (TextView) rowView.findViewById(R.id.client_item_count);
                int separatorCount = 0;
                for(Integer categorySeparatorPosition : categorySeperators)
                {
                    if( position < categorySeparatorPosition)
                        break;
                    ++separatorCount;
                }
                final int adjustedPosition = position - separatorCount;
                count.setText(Integer.toString(itemCounts[adjustedPosition]));

                final Button decrementButton = (Button) rowView.findViewById(R.id.client_order_decrement_button);
                decrementButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        decrementCount(adjustedPosition );
                    }
                });
                Button incrementButton = (Button) rowView.findViewById(R.id.client_order_increment_button);
                incrementButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        incrementCount(adjustedPosition );
                    }
                });
                break;
            case TYPE_SEPARATOR:
                rowView  = inflater.inflate(R.layout.listitem_order_category, null);
                TextView categoryLabel = (TextView) rowView.findViewById(R.id.category_string);
                int categoryID = data.get(position+1).category;
                categoryLabel.setText( getCategoryString(categoryID) );
                break;
            default:
                rowView = null;
        }

        return rowView;
    }

    @Override
    public int getItemViewType(int position)
    {
        return categorySeperators.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    public void addSeparatorItem(final int index, final AvailableItem item) {
        data.add(index, item);
        categorySeperators.add(index);
        notifyDataSetChanged();
    }

    public ArrayList<ListitemOrderItem> getSelectedData()
    {
        int separatorCount = 0;
        ArrayList<ListitemOrderItem> returnList = new ArrayList<ListitemOrderItem>();
        for(int index = 0; index < data.size(); ++index)
        {
            if (getItemViewType(index) == TYPE_SEPARATOR)
            {
                ++separatorCount;
                continue;
            }
            int itemCount = itemCounts[index - separatorCount];
            if(itemCount != 0)
            {
                AvailableItem item = data.get(index);
                String category    = getCategoryString(item.category);
                double price       = Double.parseDouble(item.price);
                returnList.add( new ListitemOrderItem(category, item.label, itemCount, price) );
            }
        }
        return returnList;
    }

    private void incrementCount(int position)
    {
        ++itemCounts[position];
        notifyDataSetChanged();
    }

    private void decrementCount(int position)
    {
        if( !(itemCounts[position] <= 0) )
            --itemCounts[position];
        notifyDataSetChanged();
    }

    private String getCategoryString(int categoryID)
    {
        return array[categoryID];
    }
}
