package com.google.firebase.messaging;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import me.zhanghai.android.materialprogressbar.R;

public class zzc implements Creator<RemoteMessage> {
    static void zza(RemoteMessage remoteMessage, Parcel parcel, int i) {
        int zzcm = zzb.zzcm(parcel);
        zzb.zzc(parcel, 1, remoteMessage.mVersionCode);
        zzb.zza(parcel, 2, remoteMessage.bM, false);
        zzb.zzaj(parcel, zzcm);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzvd(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzadu(i);
    }

    public RemoteMessage[] zzadu(int i) {
        return new RemoteMessage[i];
    }

    public RemoteMessage zzvd(Parcel parcel) {
        int zzcl = zza.zzcl(parcel);
        int i = 0;
        Bundle bundle = null;
        while (parcel.dataPosition() < zzcl) {
            int zzck = zza.zzck(parcel);
            switch (zza.zzgi(zzck)) {
                case R.styleable.View_android_focusable /*1*/:
                    i = zza.zzg(parcel, zzck);
                    break;
                case R.styleable.View_paddingStart /*2*/:
                    bundle = zza.zzs(parcel, zzck);
                    break;
                default:
                    zza.zzb(parcel, zzck);
                    break;
            }
        }
        if (parcel.dataPosition() == zzcl) {
            return new RemoteMessage(i, bundle);
        }
        throw new zza.zza("Overread allowed size end=" + zzcl, parcel);
    }
}
