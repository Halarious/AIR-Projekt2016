package hr.foi.air.international.servemepls.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import hr.foi.air.international.servemepls.R;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar customActionBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(customActionBar);

    }
}
