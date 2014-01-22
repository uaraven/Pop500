package net.ninjacat.pop500.list;

import android.view.View;

/**
 * Interface of holder factory
 */
public interface HolderCreator<T> {
    UiHolder<T> createHolder(View view);
}
