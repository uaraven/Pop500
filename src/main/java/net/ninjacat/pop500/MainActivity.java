package net.ninjacat.pop500;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
    private AbsListView listView;
    private PopularStreamAdapter adapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (AbsListView) findViewById(R.id.photo_stream);

        adapter = new PopularStreamAdapter(PxApp.getApp().getInjector());
        listView.setAdapter(adapter);
    }
}
