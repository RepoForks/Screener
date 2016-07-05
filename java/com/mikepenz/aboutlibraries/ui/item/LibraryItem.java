package com.mikepenz.aboutlibraries.ui.item;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.LibsConfiguration;
import com.mikepenz.aboutlibraries.R;
import com.mikepenz.aboutlibraries.entity.Library;
import com.mikepenz.aboutlibraries.util.RippleForegroundListener;
import com.mikepenz.aboutlibraries.util.UIUtils;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class LibraryItem extends AbstractItem<LibraryItem, ViewHolder> {
    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();
    public Library library;
    public LibsBuilder libsBuilder;
    private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.rippleForegroundListenerView);

    protected static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        protected ItemFactory() {
        }

        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        CardView card;
        View libraryBottomContainer;
        View libraryBottomDivider;
        TextView libraryCreator;
        TextView libraryDescription;
        View libraryDescriptionDivider;
        TextView libraryLicense;
        TextView libraryName;
        TextView libraryVersion;

        public ViewHolder(View itemView) {
            super(itemView);
            this.card = (CardView) itemView;
            this.card.setCardBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_card, R.color.about_libraries_card));
            this.libraryName = (TextView) itemView.findViewById(R.id.libraryName);
            this.libraryName.setTextColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_title_openSource, R.color.about_libraries_title_openSource));
            this.libraryCreator = (TextView) itemView.findViewById(R.id.libraryCreator);
            this.libraryCreator.setTextColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_text_openSource, R.color.about_libraries_text_openSource));
            this.libraryDescriptionDivider = itemView.findViewById(R.id.libraryDescriptionDivider);
            this.libraryDescriptionDivider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_dividerLight_openSource, R.color.about_libraries_dividerLight_openSource));
            this.libraryDescription = (TextView) itemView.findViewById(R.id.libraryDescription);
            this.libraryDescription.setTextColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_text_openSource, R.color.about_libraries_text_openSource));
            this.libraryBottomDivider = itemView.findViewById(R.id.libraryBottomDivider);
            this.libraryBottomDivider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_dividerLight_openSource, R.color.about_libraries_dividerLight_openSource));
            this.libraryBottomContainer = itemView.findViewById(R.id.libraryBottomContainer);
            this.libraryVersion = (TextView) itemView.findViewById(R.id.libraryVersion);
            this.libraryVersion.setTextColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_text_openSource, R.color.about_libraries_text_openSource));
            this.libraryLicense = (TextView) itemView.findViewById(R.id.libraryLicense);
            this.libraryLicense.setTextColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_text_openSource, R.color.about_libraries_text_openSource));
        }
    }

    public LibraryItem withLibrary(Library library) {
        this.library = library;
        return this;
    }

    public LibraryItem withLibsBuilder(LibsBuilder libsBuilder) {
        this.libsBuilder = libsBuilder;
        return this;
    }

    public int getType() {
        return R.id.library_item_id;
    }

    public int getLayoutRes() {
        return R.layout.listitem_opensource;
    }

    public boolean isSelectable() {
        return false;
    }

    public void bindView(ViewHolder holder) {
        super.bindView(holder);
        final Context ctx = holder.itemView.getContext();
        holder.libraryName.setText(this.library.getLibraryName());
        holder.libraryCreator.setText(this.library.getAuthor());
        if (TextUtils.isEmpty(this.library.getLibraryDescription())) {
            holder.libraryDescription.setText(this.library.getLibraryDescription());
        } else {
            holder.libraryDescription.setText(Html.fromHtml(this.library.getLibraryDescription()));
        }
        if (!(TextUtils.isEmpty(this.library.getLibraryVersion()) && this.library.getLicense() != null && TextUtils.isEmpty(this.library.getLicense().getLicenseName())) && (this.libsBuilder.showVersion.booleanValue() || this.libsBuilder.showLicense.booleanValue())) {
            holder.libraryBottomDivider.setVisibility(0);
            holder.libraryBottomContainer.setVisibility(0);
            if (TextUtils.isEmpty(this.library.getLibraryVersion()) || !this.libsBuilder.showVersion.booleanValue()) {
                holder.libraryVersion.setText(BuildConfig.FLAVOR);
            } else {
                holder.libraryVersion.setText(this.library.getLibraryVersion());
            }
            if (this.library.getLicense() == null || TextUtils.isEmpty(this.library.getLicense().getLicenseName()) || !this.libsBuilder.showLicense.booleanValue()) {
                holder.libraryLicense.setText(BuildConfig.FLAVOR);
            } else {
                holder.libraryLicense.setText(this.library.getLicense().getLicenseName());
            }
        } else {
            holder.libraryBottomDivider.setVisibility(8);
            holder.libraryBottomContainer.setVisibility(8);
        }
        if (TextUtils.isEmpty(this.library.getAuthorWebsite())) {
            holder.libraryCreator.setOnTouchListener(null);
            holder.libraryCreator.setOnClickListener(null);
            holder.libraryCreator.setOnLongClickListener(null);
        } else {
            holder.libraryCreator.setOnTouchListener(this.rippleForegroundListener);
            holder.libraryCreator.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    boolean consumed = false;
                    if (LibsConfiguration.getInstance().getListener() != null) {
                        consumed = LibsConfiguration.getInstance().getListener().onLibraryAuthorClicked(view, LibraryItem.this.library);
                    }
                    if (!consumed) {
                        LibraryItem.this.openAuthorWebsite(ctx, LibraryItem.this.library.getAuthorWebsite());
                    }
                }
            });
            holder.libraryCreator.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    boolean consumed = false;
                    if (LibsConfiguration.getInstance().getListener() != null) {
                        consumed = LibsConfiguration.getInstance().getListener().onLibraryAuthorLongClicked(v, LibraryItem.this.library);
                    }
                    if (consumed) {
                        return consumed;
                    }
                    LibraryItem.this.openAuthorWebsite(ctx, LibraryItem.this.library.getAuthorWebsite());
                    return true;
                }
            });
        }
        if (TextUtils.isEmpty(this.library.getLibraryWebsite()) && TextUtils.isEmpty(this.library.getRepositoryLink())) {
            holder.libraryDescription.setOnTouchListener(null);
            holder.libraryDescription.setOnClickListener(null);
            holder.libraryDescription.setOnLongClickListener(null);
        } else {
            holder.libraryDescription.setOnTouchListener(this.rippleForegroundListener);
            holder.libraryDescription.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    boolean consumed = false;
                    if (LibsConfiguration.getInstance().getListener() != null) {
                        consumed = LibsConfiguration.getInstance().getListener().onLibraryContentClicked(v, LibraryItem.this.library);
                    }
                    if (!consumed) {
                        LibraryItem.this.openLibraryWebsite(ctx, LibraryItem.this.library.getLibraryWebsite() != null ? LibraryItem.this.library.getLibraryWebsite() : LibraryItem.this.library.getRepositoryLink());
                    }
                }
            });
            holder.libraryDescription.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    boolean consumed = false;
                    if (LibsConfiguration.getInstance().getListener() != null) {
                        consumed = LibsConfiguration.getInstance().getListener().onLibraryContentLongClicked(v, LibraryItem.this.library);
                    }
                    if (consumed) {
                        return consumed;
                    }
                    LibraryItem.this.openLibraryWebsite(ctx, LibraryItem.this.library.getLibraryWebsite() != null ? LibraryItem.this.library.getLibraryWebsite() : LibraryItem.this.library.getRepositoryLink());
                    return true;
                }
            });
        }
        if (this.library.getLicense() == null || TextUtils.isEmpty(this.library.getLicense().getLicenseWebsite())) {
            holder.libraryBottomContainer.setOnTouchListener(null);
            holder.libraryBottomContainer.setOnClickListener(null);
            holder.libraryBottomContainer.setOnLongClickListener(null);
        } else {
            holder.libraryBottomContainer.setOnTouchListener(this.rippleForegroundListener);
            holder.libraryBottomContainer.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    boolean consumed = false;
                    if (LibsConfiguration.getInstance().getListener() != null) {
                        consumed = LibsConfiguration.getInstance().getListener().onLibraryBottomClicked(view, LibraryItem.this.library);
                    }
                    if (!consumed) {
                        LibraryItem.this.openLicense(ctx, LibraryItem.this.libsBuilder, LibraryItem.this.library);
                    }
                }
            });
            holder.libraryBottomContainer.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    boolean consumed = false;
                    if (LibsConfiguration.getInstance().getListener() != null) {
                        consumed = LibsConfiguration.getInstance().getListener().onLibraryBottomLongClicked(v, LibraryItem.this.library);
                    }
                    if (consumed) {
                        return consumed;
                    }
                    LibraryItem.this.openLicense(ctx, LibraryItem.this.libsBuilder, LibraryItem.this.library);
                    return true;
                }
            });
        }
        if (LibsConfiguration.getInstance().getLibsRecyclerViewListener() != null) {
            LibsConfiguration.getInstance().getLibsRecyclerViewListener().onBindViewHolder(holder);
        }
    }

    private void openAuthorWebsite(Context ctx, String authorWebsite) {
        try {
            ctx.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(authorWebsite)));
        } catch (Exception e) {
        }
    }

    private void openLibraryWebsite(Context ctx, String libraryWebsite) {
        try {
            ctx.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(libraryWebsite)));
        } catch (Exception e) {
        }
    }

    private void openLicense(Context ctx, LibsBuilder libsBuilder, Library library) {
        try {
            if (!libsBuilder.showLicenseDialog.booleanValue() || TextUtils.isEmpty(library.getLicense().getLicenseDescription())) {
                ctx.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(library.getLicense().getLicenseWebsite())));
                return;
            }
            Builder builder = new Builder(ctx);
            builder.setMessage(Html.fromHtml(library.getLicense().getLicenseDescription()));
            builder.create().show();
        } catch (Exception e) {
        }
    }

    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }
}
