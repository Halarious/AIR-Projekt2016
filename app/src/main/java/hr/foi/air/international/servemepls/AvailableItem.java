package hr.foi.air.international.servemepls;

public class AvailableItem
{
    public long   id;
    public int    category;
    public String label;

    public AvailableItem()
    {

    }

    public AvailableItem(String label, String description, int category)
    {
        this.label    = label;
        this.category = category;
    }

    @Override
    public String toString()
    {
        return label;
    }
}
