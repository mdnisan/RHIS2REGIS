package com.data.rhis2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Nisan on 11/27/2015.
 */
public class Twolistview extends Activity {

    private ListView asiaListView;
    private ListView europeListView;

    private String[] asiaCountries = {"Vietnam", "China", "Japan", "Korea", "India", "Singapore", "Thailand", "Malaysia"};
    private String[] europeCountries = {"France", "Germany", "Sweden", "Denmark", "England", "Spain", "Portugal", "Norway"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twolistview);

        //locate Views
        asiaListView = (ListView) findViewById(R.id.listView1);
        europeListView = (ListView) findViewById(R.id.listView2);

        //set all Listview adapter
        asiaListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, asiaCountries));
        europeListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, europeCountries));

        //set dynmic height for all listviews
        setDynamicHeight(asiaListView);
        setDynamicHeight(europeListView);
    }

    /**
     * Set listview height based on listview children
     *
     * @param listView
     */
    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }
}