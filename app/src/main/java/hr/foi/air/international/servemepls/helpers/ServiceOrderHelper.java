package hr.foi.air.international.servemepls.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import hr.foi.air.international.servemepls.models.AvailableItem;

public class ServiceOrderHelper
{
    private static ServiceOrderHelper               instance = null;
    private static ArrayList<
                    HashMap<String,
                            Integer>
                            >                       maps     = null;
    private static HashMap<String, AvailableItem>   items    = null;

    private ServiceOrderHelper()
    {
    }

    public static synchronized ServiceOrderHelper getInstance()
    {
        if(instance == null)
        {
            instance = new ServiceOrderHelper();
        }
        return instance;
    }

    public int getNumberOfOrders()
    {
        return maps.size();
    }

    public int getNumberOfItemsInOrder(int index)
    {
        return maps.get(index).size();
    }
}
