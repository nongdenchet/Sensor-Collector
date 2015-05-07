package com.event.review.review_adapter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.event.review.R;


public class MultiRowActivity extends ActionBarActivity {
    private ListView list;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testAdapter();
    }

    private void testAdapter() {
        setContentView(R.layout.test_adapter_activity);
        getSupportActionBar().setTitle("Test Adapter");

        // set up view
        list = (ListView) findViewById(R.id.list_view);
        adapter = new ItemAdapter(75, this);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
