package net.ninjacat.pop500;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import net.ninjacat.pop500.api.Photo;
import net.ninjacat.yumi.AttachTo;
import net.ninjacat.yumi.Layout;
import net.ninjacat.yumi.Yumi;

@Layout(R.layout.main)
public class MainActivity extends Activity {

    @AttachTo(R.id.stream)
    private GridView stream;
    private PopularStreamAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Yumi.injectActivity(this);

        adapter = new PopularStreamAdapter(PxApp.getApp().getInjector());
        stream.setAdapter(adapter);

        stream.setOnItemClickListener(new StreamItemClickListener());
    }

    private class StreamItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Photo photo = adapter.getItem(i);
            Intent photoView = new Intent(MainActivity.this, PhotoViewActivity.class);
            photoView.putExtra("photo", photo);
            startActivity(photoView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            resetStream();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void resetStream() {
        adapter.refresh();
        adapter = new PopularStreamAdapter(PxApp.getApp().getInjector());
        stream.setAdapter(adapter);
    }
}
