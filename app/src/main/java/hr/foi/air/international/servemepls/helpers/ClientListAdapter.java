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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.models.AvailableItem;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;

public class ClientListAdapter extends ArrayAdapter<AvailableItem>
{
    //todo Move this (or more precisely read it)
    private String array[] = {"Food", "Drinks"};

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private final Context                  context;
    private final ArrayList<AvailableItem> data;
    private       HashMap<String, Integer> itemCounts;
    private       TreeSet<Integer>         categorySeperators = new TreeSet<Integer>();

    public ClientListAdapter(Context context, ArrayList<AvailableItem> data)
    {
        super(context, R.layout.listitem_order_client, data);

        this.context    = context;
        this.data       = data;
        this.itemCounts = ClientOrderHelper.getInstance().getOrder();

        //todo: Super horrible, improve this (or keep it sorted beforehand somehow?)
        Collections.sort(data, new Comparator<AvailableItem>() {
            public int compare(AvailableItem first_operand, AvailableItem second_operand) {
                return first_operand.category < second_operand.category ?
                        -1 : 1;
            }
        });

        addSeparatorItem(0, new AvailableItem());
        for(int index = 1; index < data.size(); ++index)
        {
            if( ((index+1) != data.size()) &&
                    (data.get(index).category != data.get(index+1).category)
                    )
            {
                addSeparatorItem(index+1, new AvailableItem());
                ++index;
            }
        }
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

        final AvailableItem item = data.get(position);
        int type = getItemViewType(position);
        switch (type)
        {
            case TYPE_ITEM:
                rowView = inflater.inflate(R.layout.listitem_order_client, null);

                TextView labelView = (TextView) rowView.findViewById(R.id.list_item_client_label);
                labelView.setText(item.label);

                TextView priceView = (TextView) rowView.findViewById(R.id.list_item_client_price);
                priceView.setText(item.price + "kn");

                TextView countView = (TextView) rowView.findViewById(R.id.client_item_count);
                Integer count = itemCounts.get(item.label);
                if(count != null)
                    countView.setText(Integer.toString(count));
                else
                    countView.setText("0");


                final Button decrementButton = (Button) rowView.findViewById(R.id.client_order_decrement_button);
                decrementButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        decrementCount(item.label);
                    }
                });
                Button incrementButton = (Button) rowView.findViewById(R.id.client_order_increment_button);
                incrementButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        incrementCount(item.label);
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

    public void addSeparatorItem(final int index, final AvailableItem item)
    {
        data.add(index, item);
        categorySeperators.add(index);
        notifyDataSetChanged();
    }

    //todo: We could do this a lot faster now that we use HashMap for the counts...
    public ArrayList<ListitemOrderItem> getSelectedData()
    {
        ArrayList<ListitemOrderItem> returnList = new ArrayList<ListitemOrderItem>();
        for(int index = 0; index < data.size(); ++index)
        {
            if (getItemViewType(index) == TYPE_SEPARATOR)
                continue;

            AvailableItem item = data.get(index);
            if(itemCounts.containsKey(item.label))
            {
                int itemCount      = itemCounts.get(item.label);
                String category    = getCategoryString(item.category);
                double price       = Double.parseDouble(item.price);
                returnList.add( new ListitemOrderItem(category, item.label, itemCount, price) );
            }
        }
        return returnList;
    }

    private void incrementCount(String label)
    {
        int count = 1;
        if(itemCounts.containsKey(label))
        {
            count = itemCounts.get(label) + 1;
        }
        itemCounts.put(label, count);
        notifyDataSetChanged();
    }

    private void decrementCount(String label)
    {
        int count = 0;
        if(itemCounts.containsKey(label))
        {
            count = itemCounts.get(label) - 1;
            if(count <= 0)
                itemCounts.remove(label);
            else
                itemCounts.put(label, count);
        }
        notifyDataSetChanged();
    }

    private String getCategoryString(int categoryID)
    {
        return array[categoryID];
    }
}
