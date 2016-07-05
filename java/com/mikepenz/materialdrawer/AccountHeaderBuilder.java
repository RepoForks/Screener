package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.mikepenz.fastadapter.IIdentifyable;
import com.mikepenz.fastadapter.utils.IdDistributor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader.OnAccountHeaderItemLongClickListener;
import com.mikepenz.materialdrawer.AccountHeader.OnAccountHeaderListener;
import com.mikepenz.materialdrawer.AccountHeader.OnAccountHeaderProfileImageListener;
import com.mikepenz.materialdrawer.AccountHeader.OnAccountHeaderSelectionViewClickListener;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemLongClickListener;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont.Icon;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader.Tags;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialdrawer.view.BezelImageView;
import com.mikepenz.materialize.holder.StringHolder;
import com.mikepenz.materialize.util.UIUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class AccountHeaderBuilder {
    protected View mAccountHeader;
    protected ImageView mAccountHeaderBackground;
    protected View mAccountHeaderContainer;
    protected View mAccountHeaderTextSection;
    protected int mAccountHeaderTextSectionBackgroundResource = -1;
    protected ImageView mAccountSwitcherArrow;
    protected Activity mActivity;
    protected boolean mAlternativeProfileHeaderSwitching = false;
    protected Boolean mCloseDrawerOnProfileListClick = null;
    protected boolean mCompactStyle = false;
    protected boolean mCurrentHiddenInList = false;
    protected IProfile mCurrentProfile;
    protected TextView mCurrentProfileEmail;
    protected TextView mCurrentProfileName;
    protected BezelImageView mCurrentProfileView;
    protected boolean mDividerBelowHeader = true;
    protected Drawer mDrawer;
    protected Typeface mEmailTypeface;
    protected ImageHolder mHeaderBackground;
    protected ScaleType mHeaderBackgroundScaleType = null;
    protected DimenHolder mHeight;
    protected Typeface mNameTypeface;
    protected OnAccountHeaderItemLongClickListener mOnAccountHeaderItemLongClickListener;
    protected OnAccountHeaderListener mOnAccountHeaderListener;
    protected OnAccountHeaderProfileImageListener mOnAccountHeaderProfileImageListener;
    protected OnAccountHeaderSelectionViewClickListener mOnAccountHeaderSelectionViewClickListener;
    protected int mOnProfileClickDrawerCloseDelay = 100;
    protected boolean mOnlyMainProfileImageVisible = false;
    protected boolean mOnlySmallProfileImagesVisible = false;
    protected boolean mPaddingBelowHeader = true;
    protected IProfile mProfileFirst;
    protected BezelImageView mProfileFirstView;
    protected boolean mProfileImagesClickable = true;
    protected boolean mProfileImagesVisible = true;
    protected IProfile mProfileSecond;
    protected BezelImageView mProfileSecondView;
    protected IProfile mProfileThird;
    protected BezelImageView mProfileThirdView;
    protected List<IProfile> mProfiles;
    protected boolean mResetDrawerOnProfileListClick = true;
    protected Bundle mSavedInstance;
    protected String mSelectionFirstLine;
    protected boolean mSelectionFirstLineShown = true;
    protected boolean mSelectionListEnabled = true;
    protected boolean mSelectionListEnabledForSingleProfile = true;
    protected boolean mSelectionListShown = false;
    protected String mSelectionSecondLine;
    protected boolean mSelectionSecondLineShown = true;
    protected ColorHolder mTextColor;
    protected boolean mThreeSmallProfileImages = false;
    protected boolean mTranslucentStatusBar = true;
    protected Typeface mTypeface;
    private OnClickListener onCurrentProfileClickListener = new OnClickListener() {
        public void onClick(View v) {
            AccountHeaderBuilder.this.onProfileImageClick(v, true);
        }
    };
    private OnLongClickListener onCurrentProfileLongClickListener = new OnLongClickListener() {
        public boolean onLongClick(View v) {
            if (AccountHeaderBuilder.this.mOnAccountHeaderProfileImageListener == null) {
                return false;
            }
            return AccountHeaderBuilder.this.mOnAccountHeaderProfileImageListener.onProfileImageLongClick(v, (IProfile) v.getTag(R.id.material_drawer_profile_header), true);
        }
    };
    private OnDrawerItemClickListener onDrawerItemClickListener = new OnDrawerItemClickListener() {
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            boolean isCurrentSelectedProfile;
            if (drawerItem != null && (drawerItem instanceof IProfile) && drawerItem.isSelectable()) {
                isCurrentSelectedProfile = AccountHeaderBuilder.this.switchProfiles((IProfile) drawerItem);
            } else {
                isCurrentSelectedProfile = false;
            }
            if (AccountHeaderBuilder.this.mResetDrawerOnProfileListClick) {
                AccountHeaderBuilder.this.mDrawer.setOnDrawerItemClickListener(null);
            }
            if (!(!AccountHeaderBuilder.this.mResetDrawerOnProfileListClick || AccountHeaderBuilder.this.mDrawer == null || view == null || view.getContext() == null)) {
                AccountHeaderBuilder.this.resetDrawerContent(view.getContext());
            }
            if (!(AccountHeaderBuilder.this.mDrawer == null || AccountHeaderBuilder.this.mDrawer.getDrawerBuilder() == null || AccountHeaderBuilder.this.mDrawer.getDrawerBuilder().mMiniDrawer == null)) {
                AccountHeaderBuilder.this.mDrawer.getDrawerBuilder().mMiniDrawer.onProfileClick();
            }
            boolean consumed = false;
            if (!(drawerItem == null || !(drawerItem instanceof IProfile) || AccountHeaderBuilder.this.mOnAccountHeaderListener == null)) {
                consumed = AccountHeaderBuilder.this.mOnAccountHeaderListener.onProfileChanged(view, (IProfile) drawerItem, isCurrentSelectedProfile);
            }
            if (AccountHeaderBuilder.this.mCloseDrawerOnProfileListClick != null) {
                consumed = consumed && !AccountHeaderBuilder.this.mCloseDrawerOnProfileListClick.booleanValue();
            }
            if (!(AccountHeaderBuilder.this.mDrawer == null || consumed)) {
                AccountHeaderBuilder.this.mDrawer.mDrawerBuilder.closeDrawerDelayed();
            }
            return true;
        }
    };
    private OnDrawerItemLongClickListener onDrawerItemLongClickListener = new OnDrawerItemLongClickListener() {
        public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
            if (AccountHeaderBuilder.this.mOnAccountHeaderItemLongClickListener == null) {
                return false;
            }
            boolean isCurrentSelectedProfile;
            if (drawerItem == null || !drawerItem.isSelected()) {
                isCurrentSelectedProfile = false;
            } else {
                isCurrentSelectedProfile = true;
            }
            if (drawerItem == null || !(drawerItem instanceof IProfile)) {
                return false;
            }
            return AccountHeaderBuilder.this.mOnAccountHeaderItemLongClickListener.onProfileLongClick(view, (IProfile) drawerItem, isCurrentSelectedProfile);
        }
    };
    private OnClickListener onProfileClickListener = new OnClickListener() {
        public void onClick(View v) {
            AccountHeaderBuilder.this.onProfileImageClick(v, false);
        }
    };
    private OnLongClickListener onProfileLongClickListener = new OnLongClickListener() {
        public boolean onLongClick(View v) {
            if (AccountHeaderBuilder.this.mOnAccountHeaderProfileImageListener == null) {
                return false;
            }
            return AccountHeaderBuilder.this.mOnAccountHeaderProfileImageListener.onProfileImageLongClick(v, (IProfile) v.getTag(R.id.material_drawer_profile_header), false);
        }
    };
    private OnClickListener onSelectionClickListener = new OnClickListener() {
        public void onClick(View v) {
            boolean consumed = false;
            if (AccountHeaderBuilder.this.mOnAccountHeaderSelectionViewClickListener != null) {
                consumed = AccountHeaderBuilder.this.mOnAccountHeaderSelectionViewClickListener.onClick(v, (IProfile) v.getTag(R.id.material_drawer_profile_header));
            }
            if (AccountHeaderBuilder.this.mAccountSwitcherArrow.getVisibility() == 0 && !consumed) {
                AccountHeaderBuilder.this.toggleSelectionList(v.getContext());
            }
        }
    };

    public AccountHeaderBuilder withActivity(@NonNull Activity activity) {
        this.mActivity = activity;
        return this;
    }

    public AccountHeaderBuilder withCompactStyle(boolean compactStyle) {
        this.mCompactStyle = compactStyle;
        return this;
    }

    public AccountHeaderBuilder withTypeface(@NonNull Typeface typeface) {
        this.mTypeface = typeface;
        return this;
    }

    public AccountHeaderBuilder withNameTypeface(@NonNull Typeface typeface) {
        this.mNameTypeface = typeface;
        return this;
    }

    public AccountHeaderBuilder withEmailTypeface(@NonNull Typeface typeface) {
        this.mEmailTypeface = typeface;
        return this;
    }

    public AccountHeaderBuilder withHeightPx(int heightPx) {
        this.mHeight = DimenHolder.fromPixel(heightPx);
        return this;
    }

    public AccountHeaderBuilder withHeightDp(int heightDp) {
        this.mHeight = DimenHolder.fromDp(heightDp);
        return this;
    }

    public AccountHeaderBuilder withHeightRes(@DimenRes int heightRes) {
        this.mHeight = DimenHolder.fromResource(heightRes);
        return this;
    }

    public AccountHeaderBuilder withTextColor(@ColorInt int textColor) {
        this.mTextColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public AccountHeaderBuilder withTextColorRes(@ColorRes int textColorRes) {
        this.mTextColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    public AccountHeaderBuilder withCurrentProfileHiddenInList(boolean currentProfileHiddenInList) {
        this.mCurrentHiddenInList = currentProfileHiddenInList;
        return this;
    }

    @Deprecated
    public AccountHeaderBuilder withSelectionFistLineShown(boolean selectionFirstLineShown) {
        this.mSelectionFirstLineShown = selectionFirstLineShown;
        return this;
    }

    public AccountHeaderBuilder withSelectionFirstLineShown(boolean selectionFirstLineShown) {
        this.mSelectionFirstLineShown = selectionFirstLineShown;
        return this;
    }

    public AccountHeaderBuilder withSelectionSecondLineShown(boolean selectionSecondLineShown) {
        this.mSelectionSecondLineShown = selectionSecondLineShown;
        return this;
    }

    public AccountHeaderBuilder withSelectionFirstLine(String selectionFirstLine) {
        this.mSelectionFirstLine = selectionFirstLine;
        return this;
    }

    public AccountHeaderBuilder withSelectionSecondLine(String selectionSecondLine) {
        this.mSelectionSecondLine = selectionSecondLine;
        return this;
    }

    public AccountHeaderBuilder withPaddingBelowHeader(boolean paddingBelowHeader) {
        this.mPaddingBelowHeader = paddingBelowHeader;
        return this;
    }

    public AccountHeaderBuilder withDividerBelowHeader(boolean dividerBelowHeader) {
        this.mDividerBelowHeader = dividerBelowHeader;
        return this;
    }

    public AccountHeaderBuilder withTranslucentStatusBar(boolean translucentStatusBar) {
        this.mTranslucentStatusBar = translucentStatusBar;
        return this;
    }

    public AccountHeaderBuilder withHeaderBackground(Drawable headerBackground) {
        this.mHeaderBackground = new ImageHolder(headerBackground);
        return this;
    }

    public AccountHeaderBuilder withHeaderBackground(@DrawableRes int headerBackgroundRes) {
        this.mHeaderBackground = new ImageHolder(headerBackgroundRes);
        return this;
    }

    public AccountHeaderBuilder withHeaderBackground(ImageHolder headerBackground) {
        this.mHeaderBackground = headerBackground;
        return this;
    }

    public AccountHeaderBuilder withHeaderBackgroundScaleType(ScaleType headerBackgroundScaleType) {
        this.mHeaderBackgroundScaleType = headerBackgroundScaleType;
        return this;
    }

    public AccountHeaderBuilder withProfileImagesVisible(boolean profileImagesVisible) {
        this.mProfileImagesVisible = profileImagesVisible;
        return this;
    }

    public AccountHeaderBuilder withOnlyMainProfileImageVisible(boolean onlyMainProfileImageVisible) {
        this.mOnlyMainProfileImageVisible = onlyMainProfileImageVisible;
        return this;
    }

    public AccountHeaderBuilder withOnlySmallProfileImagesVisible(boolean onlySmallProfileImagesVisible) {
        this.mOnlySmallProfileImagesVisible = onlySmallProfileImagesVisible;
        return this;
    }

    public AccountHeaderBuilder withCloseDrawerOnProfileListClick(boolean closeDrawerOnProfileListClick) {
        this.mCloseDrawerOnProfileListClick = Boolean.valueOf(closeDrawerOnProfileListClick);
        return this;
    }

    public AccountHeaderBuilder withResetDrawerOnProfileListClick(boolean resetDrawerOnProfileListClick) {
        this.mResetDrawerOnProfileListClick = resetDrawerOnProfileListClick;
        return this;
    }

    public AccountHeaderBuilder withProfileImagesClickable(boolean profileImagesClickable) {
        this.mProfileImagesClickable = profileImagesClickable;
        return this;
    }

    public AccountHeaderBuilder withAlternativeProfileHeaderSwitching(boolean alternativeProfileHeaderSwitching) {
        this.mAlternativeProfileHeaderSwitching = alternativeProfileHeaderSwitching;
        return this;
    }

    public AccountHeaderBuilder withThreeSmallProfileImages(boolean threeSmallProfileImages) {
        this.mThreeSmallProfileImages = threeSmallProfileImages;
        return this;
    }

    public AccountHeaderBuilder withOnProfileClickDrawerCloseDelay(int onProfileClickDrawerCloseDelay) {
        this.mOnProfileClickDrawerCloseDelay = onProfileClickDrawerCloseDelay;
        return this;
    }

    public AccountHeaderBuilder withOnAccountHeaderProfileImageListener(OnAccountHeaderProfileImageListener onAccountHeaderProfileImageListener) {
        this.mOnAccountHeaderProfileImageListener = onAccountHeaderProfileImageListener;
        return this;
    }

    public AccountHeaderBuilder withOnAccountHeaderSelectionViewClickListener(OnAccountHeaderSelectionViewClickListener onAccountHeaderSelectionViewClickListener) {
        this.mOnAccountHeaderSelectionViewClickListener = onAccountHeaderSelectionViewClickListener;
        return this;
    }

    public AccountHeaderBuilder withSelectionListEnabledForSingleProfile(boolean selectionListEnabledForSingleProfile) {
        this.mSelectionListEnabledForSingleProfile = selectionListEnabledForSingleProfile;
        return this;
    }

    public AccountHeaderBuilder withSelectionListEnabled(boolean selectionListEnabled) {
        this.mSelectionListEnabled = selectionListEnabled;
        return this;
    }

    public AccountHeaderBuilder withAccountHeader(@NonNull View accountHeader) {
        this.mAccountHeaderContainer = accountHeader;
        return this;
    }

    public AccountHeaderBuilder withAccountHeader(@LayoutRes int resLayout) {
        if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }
        if (resLayout != -1) {
            this.mAccountHeaderContainer = this.mActivity.getLayoutInflater().inflate(resLayout, null, false);
        } else if (this.mCompactStyle) {
            this.mAccountHeaderContainer = this.mActivity.getLayoutInflater().inflate(R.layout.material_drawer_compact_header, null, false);
        } else {
            this.mAccountHeaderContainer = this.mActivity.getLayoutInflater().inflate(R.layout.material_drawer_header, null, false);
        }
        return this;
    }

    public AccountHeaderBuilder withProfiles(@NonNull List<IProfile> profiles) {
        this.mProfiles = IdDistributor.checkIds((List) profiles);
        return this;
    }

    public AccountHeaderBuilder addProfiles(@NonNull IProfile... profiles) {
        if (this.mProfiles == null) {
            this.mProfiles = new ArrayList();
        }
        Collections.addAll(this.mProfiles, IdDistributor.checkIds((IIdentifyable[]) profiles));
        return this;
    }

    public AccountHeaderBuilder withOnAccountHeaderListener(OnAccountHeaderListener onAccountHeaderListener) {
        this.mOnAccountHeaderListener = onAccountHeaderListener;
        return this;
    }

    public AccountHeaderBuilder withOnAccountHeaderItemLongClickListener(OnAccountHeaderItemLongClickListener onAccountHeaderItemLongClickListener) {
        this.mOnAccountHeaderItemLongClickListener = onAccountHeaderItemLongClickListener;
        return this;
    }

    public AccountHeaderBuilder withDrawer(@NonNull Drawer drawer) {
        this.mDrawer = drawer;
        drawer.getRecyclerView().setPadding(drawer.getRecyclerView().getPaddingLeft(), 0, drawer.getRecyclerView().getPaddingRight(), drawer.getRecyclerView().getPaddingBottom());
        return this;
    }

    public AccountHeaderBuilder withSavedInstance(Bundle savedInstance) {
        this.mSavedInstance = savedInstance;
        return this;
    }

    private void setHeaderHeight(int height) {
        if (this.mAccountHeaderContainer != null) {
            LayoutParams params = this.mAccountHeaderContainer.getLayoutParams();
            if (params != null) {
                params.height = height;
                this.mAccountHeaderContainer.setLayoutParams(params);
            }
            View accountHeader = this.mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header);
            if (accountHeader != null) {
                params = accountHeader.getLayoutParams();
                params.height = height;
                accountHeader.setLayoutParams(params);
            }
            View accountHeaderBackground = this.mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header_background);
            if (accountHeaderBackground != null) {
                params = accountHeaderBackground.getLayoutParams();
                params.height = height;
                accountHeaderBackground.setLayoutParams(params);
            }
        }
    }

    private void handleSelectionView(IProfile profile, boolean on) {
        if (on) {
            if (VERSION.SDK_INT >= 21) {
                ((FrameLayout) this.mAccountHeaderContainer).setForeground(UIUtils.getCompatDrawable(this.mAccountHeaderContainer.getContext(), this.mAccountHeaderTextSectionBackgroundResource));
                this.mAccountHeaderContainer.setOnClickListener(this.onSelectionClickListener);
                this.mAccountHeaderContainer.setTag(R.id.material_drawer_profile_header, profile);
                return;
            }
            this.mAccountHeaderTextSection.setBackgroundResource(this.mAccountHeaderTextSectionBackgroundResource);
            this.mAccountHeaderTextSection.setOnClickListener(this.onSelectionClickListener);
            this.mAccountHeaderTextSection.setTag(R.id.material_drawer_profile_header, profile);
        } else if (VERSION.SDK_INT >= 21) {
            ((FrameLayout) this.mAccountHeaderContainer).setForeground(null);
            this.mAccountHeaderContainer.setOnClickListener(null);
        } else {
            UIUtils.setBackground(this.mAccountHeaderTextSection, null);
            this.mAccountHeaderTextSection.setOnClickListener(null);
        }
    }

    public AccountHeader build() {
        int height;
        if (this.mAccountHeaderContainer == null) {
            withAccountHeader(-1);
        }
        this.mAccountHeader = this.mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header);
        int defaultHeaderMinHeight = this.mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_account_header_height);
        int statusBarHeight = UIUtils.getStatusBarHeight(this.mActivity, true);
        if (this.mHeight != null) {
            height = this.mHeight.asPixel(this.mActivity);
        } else if (this.mCompactStyle) {
            height = this.mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_account_header_height_compact);
        } else {
            height = (int) (((double) DrawerUIUtils.getOptimalDrawerWidth(this.mActivity)) * 0.5625d);
            if (VERSION.SDK_INT < 19) {
                int tempHeight = height - statusBarHeight;
                if (((float) tempHeight) > ((float) defaultHeaderMinHeight) - UIUtils.convertDpToPixel(8.0f, this.mActivity)) {
                    height = tempHeight;
                }
            }
        }
        if (this.mTranslucentStatusBar && VERSION.SDK_INT >= 21) {
            this.mAccountHeader.setPadding(this.mAccountHeader.getPaddingLeft(), this.mAccountHeader.getPaddingTop() + statusBarHeight, this.mAccountHeader.getPaddingRight(), this.mAccountHeader.getPaddingBottom());
            if (this.mCompactStyle) {
                height += statusBarHeight;
            } else if (height - statusBarHeight <= defaultHeaderMinHeight) {
                height = defaultHeaderMinHeight + statusBarHeight;
            }
        }
        setHeaderHeight(height);
        this.mAccountHeaderBackground = (ImageView) this.mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header_background);
        com.mikepenz.materialize.holder.ImageHolder.applyTo(this.mHeaderBackground, this.mAccountHeaderBackground, Tags.ACCOUNT_HEADER.name());
        if (this.mHeaderBackgroundScaleType != null) {
            this.mAccountHeaderBackground.setScaleType(this.mHeaderBackgroundScaleType);
        }
        int textColor = com.mikepenz.materialize.holder.ColorHolder.color(this.mTextColor, this.mActivity, R.attr.material_drawer_header_selection_text, R.color.material_drawer_header_selection_text);
        if (this.mCompactStyle) {
            this.mAccountHeaderTextSection = this.mAccountHeader;
        } else {
            this.mAccountHeaderTextSection = this.mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header_text_section);
        }
        this.mAccountHeaderTextSectionBackgroundResource = UIUtils.getSelectableBackgroundRes(this.mActivity);
        handleSelectionView(this.mCurrentProfile, true);
        this.mAccountSwitcherArrow = (ImageView) this.mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header_text_switcher);
        this.mAccountSwitcherArrow.setImageDrawable(new IconicsDrawable(this.mActivity, Icon.mdf_arrow_drop_down).sizeRes(R.dimen.material_drawer_account_header_dropdown).paddingRes(R.dimen.material_drawer_account_header_dropdown_padding).color(textColor));
        this.mCurrentProfileView = (BezelImageView) this.mAccountHeader.findViewById(R.id.material_drawer_account_header_current);
        this.mCurrentProfileName = (TextView) this.mAccountHeader.findViewById(R.id.material_drawer_account_header_name);
        this.mCurrentProfileEmail = (TextView) this.mAccountHeader.findViewById(R.id.material_drawer_account_header_email);
        if (this.mNameTypeface != null) {
            this.mCurrentProfileName.setTypeface(this.mNameTypeface);
        } else if (this.mTypeface != null) {
            this.mCurrentProfileName.setTypeface(this.mTypeface);
        }
        if (this.mEmailTypeface != null) {
            this.mCurrentProfileEmail.setTypeface(this.mEmailTypeface);
        } else if (this.mTypeface != null) {
            this.mCurrentProfileEmail.setTypeface(this.mTypeface);
        }
        this.mCurrentProfileName.setTextColor(textColor);
        this.mCurrentProfileEmail.setTextColor(textColor);
        this.mProfileFirstView = (BezelImageView) this.mAccountHeader.findViewById(R.id.material_drawer_account_header_small_first);
        this.mProfileSecondView = (BezelImageView) this.mAccountHeader.findViewById(R.id.material_drawer_account_header_small_second);
        this.mProfileThirdView = (BezelImageView) this.mAccountHeader.findViewById(R.id.material_drawer_account_header_small_third);
        calculateProfiles();
        buildProfiles();
        if (this.mSavedInstance != null) {
            int selection = this.mSavedInstance.getInt("bundle_selection_header", -1);
            if (selection != -1 && this.mProfiles != null && selection > -1 && selection < this.mProfiles.size()) {
                switchProfiles((IProfile) this.mProfiles.get(selection));
            }
        }
        if (this.mDrawer != null) {
            this.mDrawer.setHeader(this.mAccountHeaderContainer, this.mPaddingBelowHeader, this.mDividerBelowHeader);
        }
        this.mActivity = null;
        return new AccountHeader(this);
    }

    protected void calculateProfiles() {
        if (this.mProfiles == null) {
            this.mProfiles = new ArrayList();
        }
        int i;
        if (this.mCurrentProfile == null) {
            int setCount = 0;
            i = 0;
            while (i < this.mProfiles.size()) {
                if (this.mProfiles.size() > i && ((IProfile) this.mProfiles.get(i)).isSelectable()) {
                    if (setCount == 0 && this.mCurrentProfile == null) {
                        this.mCurrentProfile = (IProfile) this.mProfiles.get(i);
                    } else if (setCount == 1 && this.mProfileFirst == null) {
                        this.mProfileFirst = (IProfile) this.mProfiles.get(i);
                    } else if (setCount == 2 && this.mProfileSecond == null) {
                        this.mProfileSecond = (IProfile) this.mProfiles.get(i);
                    } else if (setCount == 3 && this.mProfileThird == null) {
                        this.mProfileThird = (IProfile) this.mProfiles.get(i);
                    }
                    setCount++;
                }
                i++;
            }
            return;
        }
        IProfile[] previousActiveProfiles = new IProfile[]{this.mCurrentProfile, this.mProfileFirst, this.mProfileSecond, this.mProfileThird};
        IProfile[] newActiveProfiles = new IProfile[4];
        Stack<IProfile> unusedProfiles = new Stack();
        for (i = 0; i < this.mProfiles.size(); i++) {
            IProfile p = (IProfile) this.mProfiles.get(i);
            if (p.isSelectable()) {
                boolean used = false;
                for (int j = 0; j < 4; j++) {
                    if (previousActiveProfiles[j] == p) {
                        newActiveProfiles[j] = p;
                        used = true;
                        break;
                    }
                }
                if (!used) {
                    unusedProfiles.push(p);
                }
            }
        }
        Stack<IProfile> activeProfiles = new Stack();
        for (i = 0; i < 4; i++) {
            if (newActiveProfiles[i] != null) {
                activeProfiles.push(newActiveProfiles[i]);
            } else if (!unusedProfiles.isEmpty()) {
                activeProfiles.push(unusedProfiles.pop());
            }
        }
        Stack<IProfile> reversedActiveProfiles = new Stack();
        while (!activeProfiles.empty()) {
            reversedActiveProfiles.push(activeProfiles.pop());
        }
        if (reversedActiveProfiles.isEmpty()) {
            this.mCurrentProfile = null;
        } else {
            this.mCurrentProfile = (IProfile) reversedActiveProfiles.pop();
        }
        if (reversedActiveProfiles.isEmpty()) {
            this.mProfileFirst = null;
        } else {
            this.mProfileFirst = (IProfile) reversedActiveProfiles.pop();
        }
        if (reversedActiveProfiles.isEmpty()) {
            this.mProfileSecond = null;
        } else {
            this.mProfileSecond = (IProfile) reversedActiveProfiles.pop();
        }
        if (reversedActiveProfiles.isEmpty()) {
            this.mProfileThird = null;
        } else {
            this.mProfileThird = (IProfile) reversedActiveProfiles.pop();
        }
    }

    protected boolean switchProfiles(IProfile newSelection) {
        if (newSelection == null) {
            return false;
        }
        if (this.mCurrentProfile == newSelection) {
            return true;
        }
        if (this.mAlternativeProfileHeaderSwitching) {
            int prevSelection = -1;
            if (this.mProfileFirst == newSelection) {
                prevSelection = 1;
            } else if (this.mProfileSecond == newSelection) {
                prevSelection = 2;
            } else if (this.mProfileThird == newSelection) {
                prevSelection = 3;
            }
            IProfile tmp = this.mCurrentProfile;
            this.mCurrentProfile = newSelection;
            if (prevSelection == 1) {
                this.mProfileFirst = tmp;
            } else if (prevSelection == 2) {
                this.mProfileSecond = tmp;
            } else if (prevSelection == 3) {
                this.mProfileThird = tmp;
            }
        } else if (this.mProfiles != null) {
            ArrayList<IProfile> previousActiveProfiles = new ArrayList(Arrays.asList(new IProfile[]{this.mCurrentProfile, this.mProfileFirst, this.mProfileSecond, this.mProfileThird}));
            if (previousActiveProfiles.contains(newSelection)) {
                int position = -1;
                for (int i = 0; i < 4; i++) {
                    if (previousActiveProfiles.get(i) == newSelection) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    previousActiveProfiles.remove(position);
                    previousActiveProfiles.add(0, newSelection);
                    this.mCurrentProfile = (IProfile) previousActiveProfiles.get(0);
                    this.mProfileFirst = (IProfile) previousActiveProfiles.get(1);
                    this.mProfileSecond = (IProfile) previousActiveProfiles.get(2);
                    this.mProfileThird = (IProfile) previousActiveProfiles.get(3);
                }
            } else {
                this.mProfileThird = this.mProfileSecond;
                this.mProfileSecond = this.mProfileFirst;
                this.mProfileFirst = this.mCurrentProfile;
                this.mCurrentProfile = newSelection;
            }
        }
        if (this.mOnlySmallProfileImagesVisible) {
            this.mProfileThird = this.mProfileSecond;
            this.mProfileSecond = this.mProfileFirst;
            this.mProfileFirst = this.mCurrentProfile;
        }
        buildProfiles();
        return false;
    }

    protected void buildProfiles() {
        this.mCurrentProfileView.setVisibility(4);
        this.mAccountHeaderTextSection.setVisibility(4);
        this.mAccountSwitcherArrow.setVisibility(8);
        this.mProfileFirstView.setVisibility(8);
        this.mProfileFirstView.setOnClickListener(null);
        this.mProfileSecondView.setVisibility(8);
        this.mProfileSecondView.setOnClickListener(null);
        this.mProfileThirdView.setVisibility(8);
        this.mProfileThirdView.setOnClickListener(null);
        this.mCurrentProfileName.setText(BuildConfig.FLAVOR);
        this.mCurrentProfileEmail.setText(BuildConfig.FLAVOR);
        if (!this.mCompactStyle) {
            this.mAccountHeaderTextSection.setPadding(0, 0, (int) UIUtils.convertDpToPixel(56.0f, this.mAccountHeaderTextSection.getContext()), 0);
        }
        handleSelectionView(this.mCurrentProfile, true);
        if (this.mCurrentProfile != null) {
            if ((this.mProfileImagesVisible || this.mOnlyMainProfileImageVisible) && !this.mOnlySmallProfileImagesVisible) {
                setImageOrPlaceholder(this.mCurrentProfileView, this.mCurrentProfile.getIcon());
                if (this.mProfileImagesClickable) {
                    this.mCurrentProfileView.setOnClickListener(this.onCurrentProfileClickListener);
                    this.mCurrentProfileView.setOnLongClickListener(this.onCurrentProfileLongClickListener);
                    this.mCurrentProfileView.disableTouchFeedback(false);
                } else {
                    this.mCurrentProfileView.disableTouchFeedback(true);
                }
                this.mCurrentProfileView.setVisibility(0);
                this.mCurrentProfileView.invalidate();
            } else if (this.mCompactStyle) {
                this.mCurrentProfileView.setVisibility(8);
            }
            this.mAccountHeaderTextSection.setVisibility(0);
            handleSelectionView(this.mCurrentProfile, true);
            this.mAccountSwitcherArrow.setVisibility(0);
            this.mCurrentProfileView.setTag(R.id.material_drawer_profile_header, this.mCurrentProfile);
            StringHolder.applyTo(this.mCurrentProfile.getName(), this.mCurrentProfileName);
            StringHolder.applyTo(this.mCurrentProfile.getEmail(), this.mCurrentProfileEmail);
            if (!(this.mProfileFirst == null || !this.mProfileImagesVisible || this.mOnlyMainProfileImageVisible)) {
                setImageOrPlaceholder(this.mProfileFirstView, this.mProfileFirst.getIcon());
                this.mProfileFirstView.setTag(R.id.material_drawer_profile_header, this.mProfileFirst);
                if (this.mProfileImagesClickable) {
                    this.mProfileFirstView.setOnClickListener(this.onProfileClickListener);
                    this.mProfileFirstView.setOnLongClickListener(this.onProfileLongClickListener);
                    this.mProfileFirstView.disableTouchFeedback(false);
                } else {
                    this.mProfileFirstView.disableTouchFeedback(true);
                }
                this.mProfileFirstView.setVisibility(0);
                this.mProfileFirstView.invalidate();
            }
            if (!(this.mProfileSecond == null || !this.mProfileImagesVisible || this.mOnlyMainProfileImageVisible)) {
                setImageOrPlaceholder(this.mProfileSecondView, this.mProfileSecond.getIcon());
                this.mProfileSecondView.setTag(R.id.material_drawer_profile_header, this.mProfileSecond);
                if (this.mProfileImagesClickable) {
                    this.mProfileSecondView.setOnClickListener(this.onProfileClickListener);
                    this.mProfileSecondView.setOnLongClickListener(this.onProfileLongClickListener);
                    this.mProfileSecondView.disableTouchFeedback(false);
                } else {
                    this.mProfileSecondView.disableTouchFeedback(true);
                }
                this.mProfileSecondView.setVisibility(0);
                this.mProfileSecondView.invalidate();
            }
            if (this.mProfileThird != null && this.mThreeSmallProfileImages && this.mProfileImagesVisible && !this.mOnlyMainProfileImageVisible) {
                setImageOrPlaceholder(this.mProfileThirdView, this.mProfileThird.getIcon());
                this.mProfileThirdView.setTag(R.id.material_drawer_profile_header, this.mProfileThird);
                if (this.mProfileImagesClickable) {
                    this.mProfileThirdView.setOnClickListener(this.onProfileClickListener);
                    this.mProfileThirdView.setOnLongClickListener(this.onProfileLongClickListener);
                    this.mProfileThirdView.disableTouchFeedback(false);
                } else {
                    this.mProfileThirdView.disableTouchFeedback(true);
                }
                this.mProfileThirdView.setVisibility(0);
                this.mProfileThirdView.invalidate();
            }
        } else if (this.mProfiles != null && this.mProfiles.size() > 0) {
            this.mAccountHeaderTextSection.setTag(R.id.material_drawer_profile_header, (IProfile) this.mProfiles.get(0));
            this.mAccountHeaderTextSection.setVisibility(0);
            handleSelectionView(this.mCurrentProfile, true);
            this.mAccountSwitcherArrow.setVisibility(0);
            if (this.mCurrentProfile != null) {
                StringHolder.applyTo(this.mCurrentProfile.getName(), this.mCurrentProfileName);
                StringHolder.applyTo(this.mCurrentProfile.getEmail(), this.mCurrentProfileEmail);
            }
        }
        if (!this.mSelectionFirstLineShown) {
            this.mCurrentProfileName.setVisibility(8);
        }
        if (!TextUtils.isEmpty(this.mSelectionFirstLine)) {
            this.mCurrentProfileName.setText(this.mSelectionFirstLine);
            this.mAccountHeaderTextSection.setVisibility(0);
        }
        if (!this.mSelectionSecondLineShown) {
            this.mCurrentProfileEmail.setVisibility(8);
        }
        if (!TextUtils.isEmpty(this.mSelectionSecondLine)) {
            this.mCurrentProfileEmail.setText(this.mSelectionSecondLine);
            this.mAccountHeaderTextSection.setVisibility(0);
        }
        if (!this.mSelectionListEnabled || (!this.mSelectionListEnabledForSingleProfile && this.mProfileFirst == null && (this.mProfiles == null || this.mProfiles.size() == 1))) {
            this.mAccountSwitcherArrow.setVisibility(8);
            handleSelectionView(null, false);
            if (!this.mCompactStyle) {
                this.mAccountHeaderTextSection.setPadding(0, 0, (int) UIUtils.convertDpToPixel(16.0f, this.mAccountHeaderTextSection.getContext()), 0);
            }
        }
        if (this.mOnAccountHeaderSelectionViewClickListener != null) {
            handleSelectionView(this.mCurrentProfile, true);
        }
    }

    private void setImageOrPlaceholder(ImageView iv, ImageHolder imageHolder) {
        DrawerImageLoader.getInstance().cancelImage(iv);
        iv.setImageDrawable(DrawerImageLoader.getInstance().getImageLoader().placeholder(iv.getContext(), Tags.PROFILE.name()));
        com.mikepenz.materialize.holder.ImageHolder.applyTo(imageHolder, iv, Tags.PROFILE.name());
    }

    private void onProfileImageClick(View v, boolean current) {
        IProfile profile = (IProfile) v.getTag(R.id.material_drawer_profile_header);
        boolean consumed = false;
        if (this.mOnAccountHeaderProfileImageListener != null) {
            consumed = this.mOnAccountHeaderProfileImageListener.onProfileImageClick(v, profile, current);
        }
        if (!consumed) {
            onProfileClick(v, current);
        }
    }

    protected void onProfileClick(View v, boolean current) {
        IProfile profile = (IProfile) v.getTag(R.id.material_drawer_profile_header);
        switchProfiles(profile);
        resetDrawerContent(v.getContext());
        if (!(this.mDrawer == null || this.mDrawer.getDrawerBuilder() == null || this.mDrawer.getDrawerBuilder().mMiniDrawer == null)) {
            this.mDrawer.getDrawerBuilder().mMiniDrawer.onProfileClick();
        }
        boolean consumed = false;
        if (this.mOnAccountHeaderListener != null) {
            consumed = this.mOnAccountHeaderListener.onProfileChanged(v, profile, current);
        }
        if (!consumed) {
            if (this.mOnProfileClickDrawerCloseDelay > 0) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (AccountHeaderBuilder.this.mDrawer != null) {
                            AccountHeaderBuilder.this.mDrawer.closeDrawer();
                        }
                    }
                }, (long) this.mOnProfileClickDrawerCloseDelay);
            } else if (this.mDrawer != null) {
                this.mDrawer.closeDrawer();
            }
        }
    }

    protected int getCurrentSelection() {
        if (!(this.mCurrentProfile == null || this.mProfiles == null)) {
            int i = 0;
            for (IProfile profile : this.mProfiles) {
                if (profile == this.mCurrentProfile) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    protected void toggleSelectionList(Context ctx) {
        if (this.mDrawer == null) {
            return;
        }
        if (this.mDrawer.switchedDrawerContent()) {
            resetDrawerContent(ctx);
            this.mSelectionListShown = false;
            return;
        }
        buildDrawerSelectionList();
        this.mAccountSwitcherArrow.clearAnimation();
        ViewCompat.animate(this.mAccountSwitcherArrow).rotation(180.0f).start();
        this.mSelectionListShown = true;
    }

    protected void buildDrawerSelectionList() {
        int selectedPosition = -1;
        int position = 0;
        ArrayList<IDrawerItem> profileDrawerItems = new ArrayList();
        if (this.mProfiles != null) {
            for (IProfile profile : this.mProfiles) {
                if (profile == this.mCurrentProfile) {
                    if (!this.mCurrentHiddenInList) {
                        selectedPosition = this.mDrawer.mDrawerBuilder.getItemAdapter().getGlobalPosition(position);
                    }
                }
                if (profile instanceof IDrawerItem) {
                    ((IDrawerItem) profile).withSetSelected(false);
                    profileDrawerItems.add((IDrawerItem) profile);
                }
                position++;
            }
        }
        this.mDrawer.switchDrawerContent(this.onDrawerItemClickListener, this.onDrawerItemLongClickListener, profileDrawerItems, selectedPosition);
    }

    private void resetDrawerContent(Context ctx) {
        if (this.mDrawer != null) {
            this.mDrawer.resetDrawerContent();
        }
        this.mAccountSwitcherArrow.clearAnimation();
        ViewCompat.animate(this.mAccountSwitcherArrow).rotation(0.0f).start();
    }

    protected void updateHeaderAndList() {
        calculateProfiles();
        buildProfiles();
        if (this.mSelectionListShown) {
            buildDrawerSelectionList();
        }
    }
}
