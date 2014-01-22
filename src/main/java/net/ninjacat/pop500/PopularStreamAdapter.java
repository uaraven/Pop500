package net.ninjacat.pop500;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.common.base.Optional;
import net.ninjacat.pop500.api.Photo;
import net.ninjacat.pop500.api.StreamController;
import net.ninjacat.pop500.api.StreamUpdateListener;
import net.ninjacat.pop500.config.Injector;
import net.ninjacat.pop500.list.HolderCreator;
import net.ninjacat.pop500.list.ListViewUtils;
import net.ninjacat.pop500.list.UiHolder;


public class PopularStreamAdapter extends BaseAdapter implements StreamUpdateListener, HolderCreator<Integer> {

    private final StreamController controller;
    private final Context context;
    private Handler uiThreadHandler;

    public PopularStreamAdapter(Injector injector) {
        context = injector.get(Context.class);

        controller = createController(injector);

        uiThreadHandler = new Handler();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Photo getItem(int i) {
        return controller.getPhoto(i).get();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        UiHolder<Integer> holder = ListViewUtils.createHolder(context, this, R.layout.photo, view);
        return holder.fillView(i);
    }

    @Override
    public void onStreamUpdated() {
        uiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public UiHolder<Integer> createHolder(View view) {
        return new PhotoHolder(view);
    }

    public void refresh() {
        controller.reset();
    }

    private StreamController createController(Injector injector) {
        StreamController controller = injector.get(StreamController.class);
        controller.setListener(this);
        return controller;
    }

    private class PhotoHolder extends UiHolder<Integer> {

        private final TextView photoName;
        private final TextView photoRating;
        private final ImageView photoImage;
        private final View progress;

        public PhotoHolder(View view) {
            super(view);
            photoName = (TextView) view.findViewById(R.id.name);
            photoRating = (TextView) view.findViewById(R.id.rating);
            photoImage = (ImageView) view.findViewById(R.id.photo);
            progress = view.findViewById(R.id.progress);
        }

        @Override
        public View fillView(Integer index) {
            Optional<Photo> photo = controller.getPhoto(index);
            if (photo.isPresent()) {
                setPhotoInfoLine(photo.get());
                getAndAssignBitmap(photo.get());
            }

            return view;
        }

        private void setPhotoInfoLine(Photo photo) {
            photoName.setText(photo.getCaption());
            photoRating.setText(String.format("%4.1f", photo.getRating()));
        }

        private void getAndAssignBitmap(Photo photo) {
            Optional<Bitmap> image = controller.getImage(photo);
            if (image.isPresent()) {
                setVisibilityForPresentImage();
                photoImage.setImageBitmap(image.get());
            } else {
                setVisibilityForAbsentImage();
            }
        }

        private void setVisibilityForAbsentImage() {
            photoRating.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            photoImage.setVisibility(View.INVISIBLE);
        }

        private void setVisibilityForPresentImage() {
            progress.setVisibility(View.INVISIBLE);
            photoRating.setVisibility(View.VISIBLE);
            photoImage.setVisibility(View.VISIBLE);
        }
    }
}
