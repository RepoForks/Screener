package com.google.firebase.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import me.zhanghai.android.materialprogressbar.R;

public class zza implements Creator<UserProfileChangeRequest> {
    static void zza(UserProfileChangeRequest userProfileChangeRequest, Parcel parcel, int i) {
        int zzcm = zzb.zzcm(parcel);
        zzb.zzc(parcel, 1, userProfileChangeRequest.mVersionCode);
        zzb.zza(parcel, 2, userProfileChangeRequest.getDisplayName(), false);
        zzb.zza(parcel, 3, userProfileChangeRequest.zzckv(), false);
        zzb.zza(parcel, 4, userProfileChangeRequest.zzckw());
        zzb.zza(parcel, 5, userProfileChangeRequest.zzckx());
        zzb.zzaj(parcel, zzcm);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzup(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzacw(i);
    }

    public UserProfileChangeRequest[] zzacw(int i) {
        return new UserProfileChangeRequest[i];
    }

    public UserProfileChangeRequest zzup(Parcel parcel) {
        String str = null;
        boolean z = false;
        int zzcl = com.google.android.gms.common.internal.safeparcel.zza.zzcl(parcel);
        boolean z2 = false;
        String str2 = null;
        int i = 0;
        while (parcel.dataPosition() < zzcl) {
            int zzck = com.google.android.gms.common.internal.safeparcel.zza.zzck(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzgi(zzck)) {
                case R.styleable.View_android_focusable /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzck);
                    break;
                case R.styleable.View_paddingStart /*2*/:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzck);
                    break;
                case R.styleable.View_paddingEnd /*3*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzck);
                    break;
                case R.styleable.View_theme /*4*/:
                    z2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzck);
                    break;
                case R.styleable.Toolbar_contentInsetStart /*5*/:
                    z = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzck);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzck);
                    break;
            }
        }
        if (parcel.dataPosition() == zzcl) {
            return new UserProfileChangeRequest(i, str2, str, z2, z);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzcl, parcel);
    }
}
