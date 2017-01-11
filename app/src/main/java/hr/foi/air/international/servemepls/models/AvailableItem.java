package hr.foi.air.international.servemepls.models;

public class AvailableItem
{
    public long   id;
    public String label;
    public String price;
    public int    category;

    public AvailableItem()
    {

    }

    public AvailableItem(String label, int category)
    {
        this.label    = label;
        this.category = category;
        //todo: unhardcode this
        this.price    = "69.99";
    }

    @Override
    public String toString()
    {
        return label;
    }
}
