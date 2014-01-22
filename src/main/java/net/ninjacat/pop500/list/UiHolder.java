package net.ninjacat.pop500.list;

import android.view.View;

/**
 * Basic interface for Holder. Should be used by Holder pattern implementation
 */
public abstract class UiHolder<T> {

    protected final View view;

    public UiHolder(View view) {
        this.view = view;
    }

    /**
     * Fills view using supplied data object
     *
     * @param object data object
     */
    public abstract View fillView(T object);
}
