package com.mikepenz.aboutlibraries;

import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import com.mikepenz.aboutlibraries.Libs.SpecialButton;
import com.mikepenz.aboutlibraries.entity.Library;
import com.mikepenz.aboutlibraries.ui.item.HeaderItem.ViewHolder;
import com.mikepenz.aboutlibraries.ui.item.LibraryItem;

public class LibsConfiguration {
    private static LibsConfiguration SINGLETON = null;
    private ItemAnimator mItemAnimator;
    private LayoutAnimationController mLayoutAnimationController = null;
    private LibTaskCallback mLibTaskCallback;
    private LibsListener mListener = null;
    private LibsRecyclerViewListener mRecyclerViewListener = null;
    private LibsUIListener mUiListener = null;

    public interface LibsListener {
        boolean onExtraClicked(View view, SpecialButton specialButton);

        void onIconClicked(View view);

        boolean onIconLongClicked(View view);

        boolean onLibraryAuthorClicked(View view, Library library);

        boolean onLibraryAuthorLongClicked(View view, Library library);

        boolean onLibraryBottomClicked(View view, Library library);

        boolean onLibraryBottomLongClicked(View view, Library library);

        boolean onLibraryContentClicked(View view, Library library);

        boolean onLibraryContentLongClicked(View view, Library library);
    }

    public abstract class LibsListenerImpl implements LibsListener {
        public void onIconClicked(View v) {
        }

        public boolean onLibraryAuthorClicked(View v, Library library) {
            return false;
        }

        public boolean onLibraryContentClicked(View v, Library library) {
            return false;
        }

        public boolean onLibraryBottomClicked(View v, Library library) {
            return false;
        }

        public boolean onExtraClicked(View v, SpecialButton specialButton) {
            return false;
        }

        public boolean onIconLongClicked(View v) {
            return true;
        }

        public boolean onLibraryAuthorLongClicked(View v, Library library) {
            return true;
        }

        public boolean onLibraryContentLongClicked(View v, Library library) {
            return true;
        }

        public boolean onLibraryBottomLongClicked(View v, Library library) {
            return true;
        }
    }

    public interface LibsRecyclerViewListener {
        void onBindViewHolder(ViewHolder viewHolder);

        void onBindViewHolder(LibraryItem.ViewHolder viewHolder);
    }

    public abstract class LibsRecyclerViewListenerImpl implements LibsRecyclerViewListener {
        public void onBindViewHolder(ViewHolder headerViewHolder) {
        }

        public void onBindViewHolder(LibraryItem.ViewHolder holder) {
        }
    }

    public interface LibsUIListener {
        View postOnCreateView(View view);

        View preOnCreateView(View view);
    }

    private LibsConfiguration() {
    }

    public static LibsConfiguration getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new LibsConfiguration();
        }
        return SINGLETON;
    }

    public void setListener(LibsListener libsListener) {
        this.mListener = libsListener;
    }

    public LibsListener getListener() {
        return this.mListener;
    }

    public void removeListener() {
        this.mListener = null;
    }

    public LibsUIListener getUiListener() {
        return this.mUiListener;
    }

    public void setUiListener(LibsUIListener uiListener) {
        this.mUiListener = uiListener;
    }

    public void removeUiListener() {
        this.mUiListener = null;
    }

    public LibsRecyclerViewListener getLibsRecyclerViewListener() {
        return this.mRecyclerViewListener;
    }

    public void setLibsRecyclerViewListener(LibsRecyclerViewListener recyclerViewListener) {
        this.mRecyclerViewListener = recyclerViewListener;
    }

    public void removeLibsRecyclerViewListener() {
        this.mRecyclerViewListener = null;
    }

    public LayoutAnimationController getLayoutAnimationController() {
        return this.mLayoutAnimationController;
    }

    public void setLayoutAnimationController(LayoutAnimationController layoutAnimationController) {
        this.mLayoutAnimationController = layoutAnimationController;
    }

    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }

    public void setItemAnimator(ItemAnimator itemAnimator) {
        this.mItemAnimator = itemAnimator;
    }

    public LibTaskCallback getLibTaskCallback() {
        return this.mLibTaskCallback;
    }

    public void setLibTaskCallback(LibTaskCallback mLibTaskCallback) {
        this.mLibTaskCallback = mLibTaskCallback;
    }

    public void reset() {
        SINGLETON = null;
    }
}
