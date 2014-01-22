package net.ninjacat.pop500.list;

import android.content.Context;
import android.view.View;

/**
 * User: ovoronin
 * Date: 05/10/12
 */
public class ListViewUtils {

    /**
     * Creates or retrieves holder. If necessary will inflate view and and create holder for that view
     *
     * @param context     used by inflator
     * @param creator     factory for creating holder
     * @param layoutResId list item layout id
     * @param reuseView   View to reuse
     * @return
     */
    public static <T> UiHolder<T> createHolder(Context context, HolderCreator<T> creator, int layoutResId, View reuseView) {
        View resultView;
        UiHolder<T> holder;
        if (reuseView == null) {
            resultView = View.inflate(context, layoutResId, null);
            holder = creator.createHolder(resultView);
            resultView.setTag(holder);
        } else {
            resultView = reuseView;
            holder = (UiHolder<T>) resultView.getTag();
        }
        return holder;
    }
}
