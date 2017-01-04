package hr.foi.air.international.servemepls.helpers;

import android.content.Context;
import android.widget.ArrayAdapter;

public class CategoryArrayAdapter extends ArrayAdapter<String>
{
    String[] itemList;

    public CategoryArrayAdapter(Context context, int resource, String[] itemList)
    {
        super(context, resource, itemList);
        this.itemList = itemList;
    }

    public int getItemPosition(String itemName)
    {
        for(int index = 0; index < itemList.length; ++index)
        {
            String item = itemList[index];
            if(item.equals(itemName))
                return index;
        }
        return -1;
    }
}
