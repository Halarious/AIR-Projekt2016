package hr.foi.air.international.servemepls.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.models.AvailableItem;

public class ClientListAdapter extends ArrayAdapter<AvailableItem>
{

    public interface ClientListListener
    {
    }

    private final Context                  context;
    private final ArrayList<AvailableItem> data;
    private final ClientListListener       clientListListener;

    public ClientListAdapter(Context context, ArrayList<AvailableItem> data,
                             ClientListListener clientListListener)
    {
        super(context, R.layout.listitem_order_client, data);

        this.context = context;
        this.data    = data;
        Collections.sort(data, new Comparator<AvailableItem>() {
            public int compare(AvailableItem first_operand, AvailableItem second_operand) {
                return first_operand.category < second_operand.category ?
                        -1 : 1;
            }
        });
        this.clientListListener = clientListListener;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listitem_order_client, parent, false);


        return rowView;
    }
}
