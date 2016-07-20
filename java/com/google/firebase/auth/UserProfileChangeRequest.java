package com.google.firebase.auth;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

public class UserProfileChangeRequest extends AbstractSafeParcelable {
    public static final Creator<UserProfileChangeRequest> CREATOR = new zza();
    private String Ld;
    private boolean aNu;
    private boolean aNv;
    private Uri aNw;
    private String dH;
    public final int mVersionCode;

    public static class Builder {
        private boolean aNu;
        private boolean aNv;
        private Uri aNw;
        private String dH;

        public UserProfileChangeRequest build() {
            return new UserProfileChangeRequest(1, this.dH, this.aNw == null ? null : this.aNw.toString(), this.aNu, this.aNv);
        }

        public Builder setDisplayName(@Nullable String str) {
            if (str == null) {
                this.aNu = true;
            } else {
                this.dH = str;
            }
            return this;
        }

        public Builder setPhotoUri(@Nullable Uri uri) {
            if (uri == null) {
                this.aNv = true;
            } else {
                this.aNw = uri;
            }
            return this;
        }
    }

    UserProfileChangeRequest(int i, String str, String str2, boolean z, boolean z2) {
        this.mVersionCode = i;
        this.dH = str;
        this.Ld = str2;
        this.aNu = z;
        this.aNv = z2;
        this.aNw = TextUtils.isEmpty(str2) ? null : Uri.parse(str2);
    }

    @Nullable
    public String getDisplayName() {
        return this.dH;
    }

    @Nullable
    public Uri getPhotoUri() {
        return this.aNw;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }

    public String zzckv() {
        return this.Ld;
    }

    public boolean zzckw() {
        return this.aNu;
    }

    public boolean zzckx() {
        return this.aNv;
    }
}
