package net.ninjacat.pop500;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.common.base.Optional;
import net.ninjacat.pop500.api.Photo;
import net.ninjacat.pop500.api.StreamController;
import net.ninjacat.yumi.AttachTo;
import net.ninjacat.yumi.Layout;
import net.ninjacat.yumi.Yumi;

import javax.inject.Inject;

/**
 * Created on 21/01/14.
 */
@Layout(R.layout.photo_view)
public class PhotoViewActivity extends Activity {

    @AttachTo(R.id.photo)
    private ImageView photo;
    @AttachTo(R.id.user)
    private TextView user;
    @AttachTo(R.id.description)
    private TextView description;
    @AttachTo(R.id.rating)
    private TextView rating;

    @Inject
    private StreamController controller;
    private Photo photoMeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivity();

        photoMeta = getIntent().getParcelableExtra("photo");
        initializeUi(photoMeta);
    }

    private void initializeUi(Photo photoMeta) {
        setTitle(photoMeta.getCaption());
        user.setText(photoMeta.getUserName());
        description.setText(photoMeta.getDescription());
        rating.setText(String.format("%3.1f", photoMeta.getRating()));
        Optional<Bitmap> image = controller.getImage(photoMeta);
        if (image.isPresent()) {
            photo.setImageBitmap(image.get());
        }
    }

    private void setupActivity() {
        Yumi.injectActivity(this);
        PxApp.getApp().getInjector().injectMembers(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
