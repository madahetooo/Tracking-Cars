package com.carsecurity.carsecurity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Help extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ListView helpListView =  findViewById(R.id.helpListView);
        ArrayList<String >help =new ArrayList<String>();
        help.add("Police : 122");
        help.add("Ambulance : 123");
        help.add("Firefighter : 180");
        help.add("Winch 1 : 01225066250");
        help.add("Winch 2 : 01002971775");
        help.add("Winch 3 : 01001967743");
        help.add("Winch 4 : 01097950005");
        help.add("Winch 5 : 01220787081");
        help.add("Winch 6 : 01274644112");
        help.add("Winch 7 : 01288811148");
        help.add("Winch 8 : 01148023313");
        help.add("Winch 9 : 01224552222");
        help.add("Winch 10 : 01282819994");
        help.add("Winch 11 : 01282820289");
        help.add("Winch 12 : 01282820289");
        help.add("Winch 13 : 01282819994");
        help.add("Winch 14 : 01282819994");
        help.add("Winch 15 : 01282820289");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,help);
        helpListView.setAdapter(arrayAdapter);

    }
}
