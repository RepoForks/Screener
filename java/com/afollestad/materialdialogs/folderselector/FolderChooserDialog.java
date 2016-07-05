package com.afollestad.materialdialogs.folderselector;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallback;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.afollestad.materialdialogs.commons.R;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FolderChooserDialog extends DialogFragment implements ListCallback {
    private static final String DEFAULT_TAG = "[MD_FOLDER_SELECTOR]";
    private boolean canGoUp = true;
    private FolderCallback mCallback;
    private File[] parentContents;
    private File parentFolder;

    public static class Builder implements Serializable {
        @StringRes
        protected int mCancelButton = 17039360;
        @StringRes
        protected int mChooseButton = R.string.md_choose_label;
        @NonNull
        protected final transient AppCompatActivity mContext;
        protected String mInitialPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        protected String mTag;

        public <ActivityType extends AppCompatActivity & FolderCallback> Builder(@NonNull ActivityType context) {
            this.mContext = context;
        }

        @NonNull
        public Builder chooseButton(@StringRes int text) {
            this.mChooseButton = text;
            return this;
        }

        @NonNull
        public Builder cancelButton(@StringRes int text) {
            this.mCancelButton = text;
            return this;
        }

        @NonNull
        public Builder initialPath(@Nullable String initialPath) {
            if (initialPath == null) {
                initialPath = File.separator;
            }
            this.mInitialPath = initialPath;
            return this;
        }

        @NonNull
        public Builder tag(@Nullable String tag) {
            if (tag == null) {
                tag = FolderChooserDialog.DEFAULT_TAG;
            }
            this.mTag = tag;
            return this;
        }

        @NonNull
        public FolderChooserDialog build() {
            FolderChooserDialog dialog = new FolderChooserDialog();
            Bundle args = new Bundle();
            args.putSerializable("builder", this);
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        public FolderChooserDialog show() {
            FolderChooserDialog dialog = build();
            dialog.show(this.mContext);
            return dialog;
        }
    }

    public interface FolderCallback {
        void onFolderSelection(@NonNull FolderChooserDialog folderChooserDialog, @NonNull File file);
    }

    private static class FolderSorter implements Comparator<File> {
        private FolderSorter() {
        }

        public int compare(File lhs, File rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }

    String[] getContentsArray() {
        int i = 1;
        if (this.parentContents != null) {
            int length = this.parentContents.length;
            if (!this.canGoUp) {
                i = 0;
            }
            String[] results = new String[(i + length)];
            if (this.canGoUp) {
                results[0] = "...";
            }
            for (int i2 = 0; i2 < this.parentContents.length; i2++) {
                if (this.canGoUp) {
                    i = i2 + 1;
                } else {
                    i = i2;
                }
                results[i] = this.parentContents[i2].getName();
            }
            return results;
        } else if (!this.canGoUp) {
            return new String[0];
        } else {
            return new String[]{"..."};
        }
    }

    File[] listFiles() {
        File[] contents = this.parentFolder.listFiles();
        List<File> results = new ArrayList();
        if (contents == null) {
            return null;
        }
        for (File fi : contents) {
            if (fi.isDirectory()) {
                results.add(fi);
            }
        }
        Collections.sort(results, new FolderSorter());
        return (File[]) results.toArray(new File[results.size()]);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getActivity(), "android.permission.READ_EXTERNAL_STORAGE") != 0) {
            return new com.afollestad.materialdialogs.MaterialDialog.Builder(getActivity()).title(R.string.md_error_label).content(R.string.md_storage_perm_error).positiveText(17039370).build();
        }
        if (getArguments() == null || !getArguments().containsKey("builder")) {
            throw new IllegalStateException("You must create a FolderChooserDialog using the Builder.");
        }
        if (!getArguments().containsKey("current_path")) {
            getArguments().putString("current_path", getBuilder().mInitialPath);
        }
        this.parentFolder = new File(getArguments().getString("current_path"));
        this.parentContents = listFiles();
        return new com.afollestad.materialdialogs.MaterialDialog.Builder(getActivity()).title(this.parentFolder.getAbsolutePath()).items(getContentsArray()).itemsCallback(this).onPositive(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                FolderChooserDialog.this.mCallback.onFolderSelection(FolderChooserDialog.this, FolderChooserDialog.this.parentFolder);
            }
        }).onNegative(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).autoDismiss(false).positiveText(getBuilder().mChooseButton).negativeText(getBuilder().mCancelButton).build();
    }

    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence s) {
        boolean z = true;
        if (this.canGoUp && i == 0) {
            this.parentFolder = this.parentFolder.getParentFile();
            if (this.parentFolder.getAbsolutePath().equals("/storage/emulated")) {
                this.parentFolder = this.parentFolder.getParentFile();
            }
            if (this.parentFolder.getParent() == null) {
                z = false;
            }
            this.canGoUp = z;
        } else {
            File[] fileArr = this.parentContents;
            if (this.canGoUp) {
                i--;
            }
            this.parentFolder = fileArr[i];
            this.canGoUp = true;
            if (this.parentFolder.getAbsolutePath().equals("/storage/emulated")) {
                this.parentFolder = Environment.getExternalStorageDirectory();
            }
        }
        this.parentContents = listFiles();
        MaterialDialog dialog = (MaterialDialog) getDialog();
        dialog.setTitle(this.parentFolder.getAbsolutePath());
        getArguments().putString("current_path", this.parentFolder.getAbsolutePath());
        dialog.setItems(getContentsArray());
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mCallback = (FolderCallback) activity;
    }

    public void show(FragmentActivity context) {
        String tag = getBuilder().mTag;
        Fragment frag = context.getSupportFragmentManager().findFragmentByTag(tag);
        if (frag != null) {
            ((DialogFragment) frag).dismiss();
            context.getSupportFragmentManager().beginTransaction().remove(frag).commit();
        }
        show(context.getSupportFragmentManager(), tag);
    }

    @NonNull
    private Builder getBuilder() {
        return (Builder) getArguments().getSerializable("builder");
    }
}
