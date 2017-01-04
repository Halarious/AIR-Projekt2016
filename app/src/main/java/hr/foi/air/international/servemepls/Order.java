package hr.foi.air.international.servemepls;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable
{
    public ArrayList<ListitemOrderItem> orderItems;

    public Order(ArrayList<ListitemOrderItem> orderItems)
    {
        this.orderItems = orderItems;
    }
}
