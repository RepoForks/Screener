package com.mikepenz.materialdrawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountHeader {
    protected static final String BUNDLE_SELECTION_HEADER = "bundle_selection_header";
    protected static final double NAVIGATION_DRAWER_ACCOUNT_ASPECT_RATIO = 0.5625d;
    protected final AccountHeaderBuilder mAccountHeaderBuilder;

    public interface OnAccountHeaderItemLongClickListener {
        boolean onProfileLongClick(View view, IProfile iProfile, boolean z);
    }

    public interface OnAccountHeaderListener {
        boolean onProfileChanged(View view, IProfile iProfile, boolean z);
    }

    public interface OnAccountHeaderProfileImageListener {
        boolean onProfileImageClick(View view, IProfile iProfile, boolean z);

        boolean onProfileImageLongClick(View view, IProfile iProfile, boolean z);
    }

    public interface OnAccountHeaderSelectionViewClickListener {
        boolean onClick(View view, IProfile iProfile);
    }

    protected AccountHeader(AccountHeaderBuilder accountHeaderBuilder) {
        this.mAccountHeaderBuilder = accountHeaderBuilder;
    }

    protected AccountHeaderBuilder getAccountHeaderBuilder() {
        return this.mAccountHeaderBuilder;
    }

    public View getView() {
        return this.mAccountHeaderBuilder.mAccountHeaderContainer;
    }

    public void setDrawer(Drawer drawer) {
        this.mAccountHeaderBuilder.mDrawer = drawer;
    }

    public ImageView getHeaderBackgroundView() {
        return this.mAccountHeaderBuilder.mAccountHeaderBackground;
    }

    public void setHeaderBackground(ImageHolder imageHolder) {
        com.mikepenz.materialize.holder.ImageHolder.applyTo((com.mikepenz.materialize.holder.ImageHolder) imageHolder, this.mAccountHeaderBuilder.mAccountHeaderBackground);
    }

    public void setBackground(Drawable headerBackground) {
        this.mAccountHeaderBuilder.mAccountHeaderBackground.setImageDrawable(headerBackground);
    }

    public void setBackgroundRes(@DrawableRes int headerBackgroundRes) {
        this.mAccountHeaderBuilder.mAccountHeaderBackground.setImageResource(headerBackgroundRes);
    }

    public void toggleSelectionList(Context ctx) {
        this.mAccountHeaderBuilder.toggleSelectionList(ctx);
    }

    public boolean isSelectionListShown() {
        return this.mAccountHeaderBuilder.mSelectionListShown;
    }

    public void setSelectionFirstLineShown(boolean selectionFirstLineShown) {
        this.mAccountHeaderBuilder.mSelectionFirstLineShown = selectionFirstLineShown;
        this.mAccountHeaderBuilder.updateHeaderAndList();
    }

    public void setSelectionSecondLineShown(boolean selectionSecondLineShown) {
        this.mAccountHeaderBuilder.mSelectionSecondLineShown = selectionSecondLineShown;
        this.mAccountHeaderBuilder.updateHeaderAndList();
    }

    public void setSelectionFirstLine(String selectionFirstLine) {
        this.mAccountHeaderBuilder.mSelectionFirstLine = selectionFirstLine;
        this.mAccountHeaderBuilder.updateHeaderAndList();
    }

    public void setSelectionSecondLine(String selectionSecondLine) {
        this.mAccountHeaderBuilder.mSelectionSecondLine = selectionSecondLine;
        this.mAccountHeaderBuilder.updateHeaderAndList();
    }

    public List<IProfile> getProfiles() {
        return this.mAccountHeaderBuilder.mProfiles;
    }

    public void setProfiles(List<IProfile> profiles) {
        this.mAccountHeaderBuilder.mProfiles = profiles;
        this.mAccountHeaderBuilder.updateHeaderAndList();
    }

    public void setActiveProfile(IProfile profile) {
        setActiveProfile(profile, false);
    }

    public void setActiveProfile(IProfile profile, boolean fireOnProfileChanged) {
        boolean isCurrentSelectedProfile = this.mAccountHeaderBuilder.switchProfiles(profile);
        if (this.mAccountHeaderBuilder.mDrawer != null && isSelectionListShown()) {
            this.mAccountHeaderBuilder.mDrawer.setSelection(profile.getIdentifier(), false);
        }
        if (fireOnProfileChanged && this.mAccountHeaderBuilder.mOnAccountHeaderListener != null) {
            this.mAccountHeaderBuilder.mOnAccountHeaderListener.onProfileChanged(null, profile, isCurrentSelectedProfile);
        }
    }

    public void setActiveProfile(long identifier) {
        setActiveProfile(identifier, false);
    }

    public void setActiveProfile(long identifier, boolean fireOnProfileChanged) {
        if (this.mAccountHeaderBuilder.mProfiles != null) {
            for (IProfile profile : this.mAccountHeaderBuilder.mProfiles) {
                if (profile != null && profile.getIdentifier() == identifier) {
                    setActiveProfile(profile, fireOnProfileChanged);
                    return;
                }
            }
        }
    }

    public IProfile getActiveProfile() {
        return this.mAccountHeaderBuilder.mCurrentProfile;
    }

    public void updateProfile(@NonNull IProfile newProfile) {
        updateProfileByIdentifier(newProfile);
    }

    @Deprecated
    public void updateProfileByIdentifier(@NonNull IProfile newProfile) {
        int found = getPositionByIdentifier(newProfile.getIdentifier());
        if (found > -1) {
            this.mAccountHeaderBuilder.mProfiles.set(found, newProfile);
            this.mAccountHeaderBuilder.updateHeaderAndList();
        }
    }

    public void addProfiles(@NonNull IProfile... profiles) {
        if (this.mAccountHeaderBuilder.mProfiles == null) {
            this.mAccountHeaderBuilder.mProfiles = new ArrayList();
        }
        Collections.addAll(this.mAccountHeaderBuilder.mProfiles, profiles);
        this.mAccountHeaderBuilder.updateHeaderAndList();
    }

    public void addProfile(@NonNull IProfile profile, int position) {
        if (this.mAccountHeaderBuilder.mProfiles == null) {
            this.mAccountHeaderBuilder.mProfiles = new ArrayList();
        }
        this.mAccountHeaderBuilder.mProfiles.add(position, profile);
        this.mAccountHeaderBuilder.updateHeaderAndList();
    }

    public void removeProfile(int position) {
        if (this.mAccountHeaderBuilder.mProfiles != null && this.mAccountHeaderBuilder.mProfiles.size() > position) {
            this.mAccountHeaderBuilder.mProfiles.remove(position);
        }
        this.mAccountHeaderBuilder.updateHeaderAndList();
    }

    public void removeProfileByIdentifier(long identifier) {
        int found = getPositionByIdentifier(identifier);
        if (found > -1) {
            this.mAccountHeaderBuilder.mProfiles.remove(found);
        }
        this.mAccountHeaderBuilder.updateHeaderAndList();
    }

    public void removeProfile(@NonNull IProfile profile) {
        removeProfileByIdentifier(profile.getIdentifier());
    }

    public void clear() {
        this.mAccountHeaderBuilder.mProfiles = null;
        this.mAccountHeaderBuilder.calculateProfiles();
        this.mAccountHeaderBuilder.buildProfiles();
    }

    private int getPositionByIdentifier(long identifier) {
        if (this.mAccountHeaderBuilder.mProfiles == null || identifier < 0) {
            return -1;
        }
        int i = 0;
        while (i < this.mAccountHeaderBuilder.mProfiles.size()) {
            if (this.mAccountHeaderBuilder.mProfiles.get(i) != null && ((IProfile) this.mAccountHeaderBuilder.mProfiles.get(i)).getIdentifier() == identifier) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public Bundle saveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putInt(BUNDLE_SELECTION_HEADER, this.mAccountHeaderBuilder.getCurrentSelection());
        }
        return savedInstanceState;
    }
}
