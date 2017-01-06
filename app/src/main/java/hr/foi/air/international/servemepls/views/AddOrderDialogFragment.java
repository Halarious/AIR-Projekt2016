package hr.foi.air.international.servemepls.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.helpers.OrdersArrayAdapter;
import hr.foi.air.international.servemepls.models.AvailableItem;
import hr.foi.air.international.servemepls.models.ListitemOrderItem;
import hr.foi.air.international.servemepls.models.Order;
import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.helpers.AddOrderArrayAdapter;
import hr.foi.air.international.servemepls.helpers.CategoryArrayAdapter;

public class AddOrderDialogFragment extends DialogFragment
                                    implements AdapterView.OnItemSelectedListener,
                                               OrdersArrayAdapter.OrdersArrayAdapterListener
{

    public interface AddOrderDialogListener
    {
        public void onDialogPositiveClick(DialogFragment dialog,
                                          ArrayList<ListitemOrderItem> newOrder,
                                          int orderIndex);
    }

    private Context context;
    private ArrayList<AvailableItem> foodItems      = new ArrayList<AvailableItem>();
    private ArrayList<AvailableItem> drinkItems     = new ArrayList<AvailableItem>();
    private OrdersArrayAdapter orderArrayAdapter    = null;
    private Order loadedOrder                       = null;
    private AddOrderDialogListener dialogListener;

    public Integer  counter = 0;
    public TextView counterText;
    public Spinner  dialogSpinnerCategory;
    public Spinner  dialogSpinnerAvailableItems;

    @Override
    public void onAttachFragment(Fragment fragment)
    {
        super.onAttachFragment(fragment);
        try
        {
            dialogListener = (AddOrderDialogListener) fragment;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(fragment.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        setItemSpinner((String)dialogSpinnerCategory.getSelectedItem());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        //todo: Check if something should be done here
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
        LayoutInflater      inflater = getActivity().getLayoutInflater();
        View              dialogView = inflater.inflate(R.layout.dialog_add_order, null);
        builder.setView(dialogView);

        final int orderIndex;
        Bundle receivedBundle = getArguments();
        if(receivedBundle != null)
        {
            if(receivedBundle.containsKey("Order") && receivedBundle.containsKey("OrderIndex"))
            {
                orderIndex  = receivedBundle.getInt("OrderIndex");
                loadedOrder = (Order)receivedBundle.getSerializable("Order");
            }
            else
                orderIndex = -1;
        }
        else
            orderIndex = -1;

        //todo: This should NOT be called manually!
        onAttachFragment(getTargetFragment());

        foodItems.add (new AvailableItem(getResources().getString(R.string.spinner_no_selection), 0));
        drinkItems.add(new AvailableItem(getResources().getString(R.string.spinner_no_selection), 0));

        //todo: The mysqlite database must exists at this point and have some data
        //ArrayList<AvailableItem> itemList = AvailableOrderItemsORM.getItems(getContext());
        ArrayList<AvailableItem> itemList = new ArrayList<AvailableItem>();
        itemList.add(new AvailableItem("Booze", 1));
        itemList.add(new AvailableItem("Grub", 0));
        itemList.add(new AvailableItem("Booze2", 1));
        itemList.add(new AvailableItem("Grub2", 0));
        itemList.add(new AvailableItem("Booze3", 1));
        {
            for (AvailableItem item : itemList)
            {
                if (item.category == 0)
                {
                    foodItems.add(item);
                } else if (item.category == 1)
                {
                    drinkItems.add(item);
                }
            }
        }

        dialogSpinnerCategory = (Spinner) dialogView.findViewById(R.id.add_order_category);
        final String[] dialogSpinnerItems = new String[]{"Food", "Drinks"};
        ArrayAdapter<String> dialogSpinnerAdapter =
                new CategoryArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, dialogSpinnerItems);
        dialogSpinnerCategory.setAdapter(dialogSpinnerAdapter);
        dialogSpinnerCategory.setOnItemSelectedListener(this);

        dialogSpinnerAvailableItems = (Spinner) dialogView.findViewById(R.id.add_order_item);

        counterText = (TextView) dialogView.findViewById(R.id.add_order_counter_text);

        final ListView listView = (ListView) dialogView.findViewById(R.id.order_items_list);
        listView.setEmptyView(dialogView.findViewById(R.id.empty_list));
        orderArrayAdapter = new OrdersArrayAdapter(getContext(), new ArrayList<ListitemOrderItem>(), this);
        listView.setAdapter(orderArrayAdapter);

        final Button addOrderButton  = (Button) dialogView.findViewById(R.id.add_order_button);
        Button incrementButton = (Button) dialogView.findViewById(R.id.add_order_increment_button);
        Button decrementButton = (Button) dialogView.findViewById(R.id.add_order_decrement_button);
        addOrderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int itemCount    = Integer.parseInt(counterText.getText().toString());
                if(itemCount > 0)
                {
                    ListitemOrderItem selectedItem = (ListitemOrderItem) orderArrayAdapter.getSelectedItem();
                    if(selectedItem != null)
                    {
                        selectedItem.count = itemCount;
                        orderArrayAdapter.deselectItem();
                        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                    }
                    else
                    {
                        String itemCategory = ((String)dialogSpinnerCategory.getSelectedItem());
                        String itemLabel    = dialogSpinnerAvailableItems.getSelectedItem().toString();
                        ListitemOrderItem orderItem = new ListitemOrderItem(itemCategory, itemLabel,
                                                                            itemCount);
                        orderArrayAdapter.addItem(orderItem);
                    }

                    addOrderButton.setText("Add Order");
                    resetCounter();
                    dialogSpinnerAvailableItems.setSelection(0);
                }
            }
        });
        incrementButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                incrementCounter();
            }
        });
        decrementButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(counter > 0)
                {
                    decrementCounter();
                }
            }
        });

        builder.setPositiveButton(R.string.add_order_dialog_positive, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int id)
            {
                //todo: Add item to the activities list, then find a way to sync everything
                //with the server. Maybe not do the sync here?
                ArrayList<ListitemOrderItem> order = orderArrayAdapter.getData();
                if(order.size() > 0)
                {
                    dialogListener.onDialogPositiveClick(AddOrderDialogFragment.this, order,
                                                         orderIndex);
                }

            }
        });
        builder.setNegativeButton(R.string.add_order_dialog_negative, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                AddOrderDialogFragment.this.getDialog().cancel();
            }
        });
        dialogSpinnerAvailableItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                AvailableItem selection = (AvailableItem)dialogSpinnerAvailableItems.getSelectedItem();
                if(selection.toString().equals(getResources().getString(R.string.spinner_no_selection)))
                    addOrderButton.setEnabled(false);
                else
                {
                    addOrderButton.setEnabled(true);
                    for(ListitemOrderItem item : (ArrayList<ListitemOrderItem>)orderArrayAdapter.getData())
                    {
                        if(selection.label.equals(item.label))
                        {
                            addOrderButton.setText("Update");
                            setCounter(item.count);

                            int itemIndex = orderArrayAdapter.getData().indexOf(item);
                            orderArrayAdapter.setSelectedItem(itemIndex,
                                                              listView.getChildAt(itemIndex));
                            return;
                        }
                    }
                    ListitemOrderItem selectedItem = (ListitemOrderItem) orderArrayAdapter.getSelectedItem();
                    if(selectedItem != null)
                        orderArrayAdapter.deselectItem();

                    addOrderButton.setText("Add Order");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l)
            {
                //todo: this is not a very good solution. Look into it
                orderArrayAdapter.setSelectedItem(index, view);

                int categoryPosition =
                        ((CategoryArrayAdapter)dialogSpinnerCategory.getAdapter())
                                .getItemPosition( ((ListitemOrderItem)(orderArrayAdapter.getSelectedItem())).category );
                dialogSpinnerCategory.setSelection(categoryPosition);
                dialogSpinnerCategory.getOnItemSelectedListener().onItemSelected(null, null, 0, 0);

                int itemPosition =
                        ((AddOrderArrayAdapter)dialogSpinnerAvailableItems.getAdapter())
                                .getItemPosition( ((ListitemOrderItem)(orderArrayAdapter.getSelectedItem())).label );
                dialogSpinnerAvailableItems.setSelection(itemPosition);
            }
        });

        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if(loadedOrder != null)
            //loadOrder(loadedOrder);
        {
            for(ListitemOrderItem item : loadedOrder.orderItems)
            {
                String category = item.category;
                String label    = item.label;
                int    count    = item.count;

                //todo: Maybe there could be some improvement here if we use HashMaps or a similar
                //structure for easier search. Keeping things sorted is also an option. For we're
                //keeping it slow
                for(int i = dialogSpinnerCategory.getAdapter().getCount(); i != 0; --i)
                {
                    String spinner_category = (String)dialogSpinnerCategory.getAdapter().getItem(i-1);
                    if(spinner_category.equals(category))
                    {
                        setItemSpinner(category);
                        for(int j = dialogSpinnerAvailableItems.getAdapter().getCount(); j != 0; --j)
                        {
                            AvailableItem spinner_item =
                                    (AvailableItem)dialogSpinnerAvailableItems.getAdapter().getItem(j-1);
                            if(spinner_item.label.equals(label))
                            {
                                orderArrayAdapter.addItem(new ListitemOrderItem(category, label, count));
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onSelect()
    {

    }

    @Override
    public void onDeselect()
    {

    }

    @Override
    public void onRemove()
    {

    }

    public void incrementCounter()
    {
        ++counter;
        counterText.setText(counter.toString());
    }

    public void decrementCounter()
    {
        --counter;
        counterText.setText(counter.toString());
    }

    public void setCounter(int count)
    {
        counter      = count;
        counterText.setText(((Integer)count).toString());
    }

    public void resetCounter()
    {
        counter = 0;
        counterText.setText(counter.toString());
    }

    //todo: Stop the ridiculous creation of adapters each time, just exchange the data set
    //todo: Get rid of the tag and implement a better sistem to recongnize the data sets
    public void setItemSpinner(String selected)
    {
        int tag = -1;
        if(dialogSpinnerAvailableItems.getAdapter() != null)
            tag = ((AddOrderArrayAdapter)dialogSpinnerAvailableItems.getAdapter()).datasetTag;

        if (selected.equals("Food") && (tag != 0))
        {
            AddOrderArrayAdapter adapter = new AddOrderArrayAdapter(getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    foodItems);
            adapter.datasetTag = 0;
            dialogSpinnerAvailableItems.setAdapter(adapter);
        }
        else if (selected.equals("Drinks") && (tag != 1))

        {
            AddOrderArrayAdapter adapter = new AddOrderArrayAdapter(getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    drinkItems);
            adapter.datasetTag = 1;
            dialogSpinnerAvailableItems.setAdapter(adapter);
        }
    }
}
