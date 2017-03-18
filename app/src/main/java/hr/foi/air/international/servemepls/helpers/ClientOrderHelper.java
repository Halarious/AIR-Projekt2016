package hr.foi.air.international.servemepls.helpers;

import java.util.HashMap;

public class ClientOrderHelper
{
    private static ClientOrderHelper instance = null;
    private static String qrContent           = "";
    private static HashMap<String, Integer> clientOrder;

    private ClientOrderHelper()
    {
    }

    public static synchronized ClientOrderHelper getInstance()
    {
        if(instance == null)
        {
            instance = new ClientOrderHelper();
            clientOrder = new HashMap<String, Integer>();
        }
        return instance;
    }

    public HashMap<String, Integer> getOrder()
    {
        return clientOrder;
    }

    public void clearOrder()
    {
        clientOrder.clear();
    }

    public void putQR(String qrContent)
    {
        this.qrContent = qrContent;
    }

    public String getQR()
    {
        return qrContent;
    }
}
