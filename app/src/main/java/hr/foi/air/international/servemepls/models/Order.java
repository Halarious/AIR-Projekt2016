package hr.foi.air.international.servemepls.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable
{
    //todo Do not store the UID in every order, it defeats the point of confirmation
    public String                       UID;
    public String                       TID;
    public ArrayList<ListitemOrderItem> orderItems;

    public Order(ArrayList<ListitemOrderItem> orderItems, String UID, String TID)
    {
        this.UID        = UID;
        this.TID        = TID;
        this.orderItems = orderItems;
    }
}
