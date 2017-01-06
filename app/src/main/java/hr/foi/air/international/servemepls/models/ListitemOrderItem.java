package hr.foi.air.international.servemepls.models;

import java.io.Serializable;

public class ListitemOrderItem implements Serializable
{
    public String          category;
    public String          label;
    public int             count;

    public ListitemOrderItem(String category, String label, int count)
    {
        this.category = category;
        this.label    = label;
        this.count    = count;
    }

    public ListitemOrderItem(ListitemOrderItem item)
    {
        this.category  = item.category;
        this.label     = item.label;
        this.count     = item.count;
    }


    @Override
    public String toString()
    {
        return label;
    }
}
