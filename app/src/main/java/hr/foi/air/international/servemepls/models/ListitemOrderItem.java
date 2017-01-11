package hr.foi.air.international.servemepls.models;

import java.io.Serializable;

public class ListitemOrderItem implements Serializable
{
    public String          category;
    public String          label;
    public int             count;
    public double          itemPrice;
    public double          totalPrice;

    //todo: Remove this maybe, it's here for compatibility because prices were added later
    public ListitemOrderItem(String category, String label, int count)
    {
        this.category = category;
        this.label    = label;
        this.count    = count;
    }

    public ListitemOrderItem(String category, String label, int count, double itemPrice)
    {
        this.category   = category;
        this.label      = label;
        this.count      = count;
        this.itemPrice  = itemPrice;
        this.totalPrice = itemPrice * count;
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
