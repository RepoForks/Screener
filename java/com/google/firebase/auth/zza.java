package com.google.firebase.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import me.zhanghai.android.materialprogressbar.R;

public class zza implements Creator<UserProfileChangeRequest> {
    static void zza(UserProfileChangeRequest userProfileChangeRequest, Parcel parcel, int i) {
        int zzar = zzb.zzar(parcel);
        zzb.zzc(parcel, 1, userProfileChangeRequest.mVersionCode);
        zzb.zza(parcel, 2, userProfileChangeRequest.getDisplayName(), false);
        zzb.zza(parcel, 3, userProfileChangeRequest.zzOo(), false);
        zzb.zza(parcel, 4, userProfileChangeRequest.zzOp());
        zzb.zza(parcel, 5, userProfileChangeRequest.zzOq());
        zzb.zzJ(parcel, zzar);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzjk(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zznk(i);
    }

    public UserProfileChangeRequest zzjk(Parcel parcel) {
        String str = null;
        boolean z = false;
        int zzaq = com.google.android.gms.common.internal.safeparcel.zza.zzaq(parcel);
        boolean z2 = false;
        String str2 = null;
        int i = 0;
        while (parcel.dataPosition() < zzaq) {
            int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcj(zzap)) {
                case R.styleable.View_android_focusable /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzap);
                    break;
                case R.styleable.View_paddingStart /*2*/:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzap);
                    break;
                case R.styleable.View_paddingEnd /*3*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzap);
                    break;
                case R.styleable.View_theme /*4*/:
                    z2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzap);
                    break;
                case R.styleable.Toolbar_contentInsetStart /*5*/:
                    z = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzap);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzap);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaq) {
            return new UserProfileChangeRequest(i, str2, str, z2, z);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzaq, parcel);
    }

    public UserProfileChangeRequest[] zznk(int i) {
        return new UserProfileChangeRequest[i];
    }
}
