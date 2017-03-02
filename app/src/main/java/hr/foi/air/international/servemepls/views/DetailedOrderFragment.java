package hr.foi.air.international.servemepls.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import hr.foi.air.international.servemepls.R;
import hr.foi.air.international.servemepls.models.Order;

public class DetailedOrderFragment extends Fragment
{
    private boolean showEdit;

    public static final String ORDER    = "order";
    public static final String EDITABLE = "editable";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_detailed_view, container, false);

        Bundle arguments = getArguments();
        Order order      = (Order)arguments.getSerializable(ORDER);
        showEdit         = arguments.getBoolean(EDITABLE);

        //todo Work on an actual display
        if(showEdit)
        {
            (rootView.findViewById(R.id.detV_button)).setVisibility(View.VISIBLE);
        }
        ((TextView)rootView.findViewById(R.id.detV_text)).setText(order.orderItems.get(0).label);

        return rootView;
    }
}
