package hr.foi.air.international.servemepls.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.AvailableItem;
import hr.foi.air.international.servemepls.ListItemOrder;
import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.AddOrderArrayAdapter;
import hr.foi.air.international.servemepls.helpers.AvailableOrderItemsORM;
import hr.foi.air.international.servemepls.helpers.OrdersArrayAdapter;

public class AddOrderDialogFragment extends DialogFragment
                                    implements AdapterView.OnItemSelectedListener
{

    public interface AddOrderDialogListener
    {
        public void onDialogPositiveClick(DialogFragment dialog,
                                          ArrayList<ListItemOrder> newOrder);
    }

    ArrayList<AvailableItem> foodItems;
    ArrayList<AvailableItem> drinkItems;
    ArrayList<ListItemOrder> orderItems;
    AddOrderDialogListener dialogListener;
    public int      counter = 0;
    public TextView counterText;
    public Spinner  dialogSpinnerCategory;
    public Spinner  dialogSpinnerAvailableItems;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (dialogSpinnerCategory.getSelectedItem().equals("Food"))
        {
            ArrayAdapter adapter = new AddOrderArrayAdapter(getContext(),
                                                            android.R.layout.simple_spinner_dropdown_item,
                                                            foodItems);
            dialogSpinnerAvailableItems.setAdapter(adapter);
        }
        else if (dialogSpinnerCategory.getSelectedItem().equals("Drinks"))

        {
            ArrayAdapter adapter = new AddOrderArrayAdapter(getContext(),
                                                            android.R.layout.simple_spinner_dropdown_item,
                                                            drinkItems);
            dialogSpinnerAvailableItems.setAdapter(adapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        //todo: Check if something should be done here
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater      inflater = getActivity().getLayoutInflater();
        View              dialogView = inflater.inflate(R.layout.dialog_add_order, null);
        builder.setView(dialogView);

        onAttachFragment(getParentFragment());

        ArrayList<AvailableItem> itemList = AvailableOrderItemsORM.getItems(getContext());
        for(AvailableItem item : itemList)
        {
            if(item.category == 0)
            {
                foodItems.add(item);
            }
            else if(item.category == 1)
            {
                drinkItems.add(item);
            }
        }

        dialogSpinnerCategory = (Spinner) dialogView.findViewById(R.id.add_order_category);
        String[] dialogSpinnerItems = new String[]{"Food", "Drinks"};
        ArrayAdapter<String> dialogSpinnerAdapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dialogSpinnerItems);
        dialogSpinnerCategory.setAdapter(dialogSpinnerAdapter);
        dialogSpinnerCategory.setOnItemSelectedListener(this);

        dialogSpinnerAvailableItems = (Spinner) dialogView.findViewById(R.id.add_order_item);

        counterText = (TextView) dialogView.findViewById(R.id.add_order_counter_text);

        ListView listView = (ListView) dialogView.findViewById(R.id.order_items_list);
        listView.setAdapter(new OrdersArrayAdapter(getContext(), orderItems));

        Button addOrderButton  = (Button) dialogView.findViewById(R.id.add_order_button);
        Button incrementButton = (Button) dialogView.findViewById(R.id.add_order_increment_button);
        Button decrementButton = (Button) dialogView.findViewById(R.id.add_order_decrement_button);
        addOrderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String itemLabel = dialogSpinnerAvailableItems.getSelectedItem().toString();
                int itemCount    = Integer.parseInt(counterText.getText().toString());
                ListItemOrder orderItem = new ListItemOrder(itemLabel, itemCount);
                orderItems.add(orderItem);
            }
        });
        incrementButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ++counter;
                counterText.setText(counter);
            }
        });
        decrementButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(counter > 0)
                {
                    --counter;
                    counterText.setText(counter);
                }
            }
        });

        builder.setPositiveButton(R.string.add_order_dialog_positive, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int id)
            {
                //todo: Add item to the activities list, then find a way to sync everything
                //with the server
                dialogListener.onDialogPositiveClick(AddOrderDialogFragment.this,
                                                     orderItems);
            }
        });
        builder.setNegativeButton(R.string.add_order_dialog_negative, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                AddOrderDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttachFragment(Fragment fragment)
    {
        try
        {
            dialogListener = (AddOrderDialogListener) fragment;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(fragment.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
