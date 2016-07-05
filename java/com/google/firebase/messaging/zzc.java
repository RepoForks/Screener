package com.google.firebase.messaging;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import me.zhanghai.android.materialprogressbar.R;

public class zzc implements Creator<RemoteMessage> {
    static void zza(RemoteMessage remoteMessage, Parcel parcel, int i) {
        int zzar = zzb.zzar(parcel);
        zzb.zzc(parcel, 1, remoteMessage.mVersionCode);
        zzb.zza(parcel, 2, remoteMessage.zzaay, false);
        zzb.zzJ(parcel, zzar);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzjy(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zznI(i);
    }

    public RemoteMessage zzjy(Parcel parcel) {
        int zzaq = zza.zzaq(parcel);
        int i = 0;
        Bundle bundle = null;
        while (parcel.dataPosition() < zzaq) {
            int zzap = zza.zzap(parcel);
            switch (zza.zzcj(zzap)) {
                case R.styleable.View_android_focusable /*1*/:
                    i = zza.zzg(parcel, zzap);
                    break;
                case R.styleable.View_paddingStart /*2*/:
                    bundle = zza.zzs(parcel, zzap);
                    break;
                default:
                    zza.zzb(parcel, zzap);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaq) {
            return new RemoteMessage(i, bundle);
        }
        throw new zza.zza("Overread allowed size end=" + zzaq, parcel);
    }

    public RemoteMessage[] zznI(int i) {
        return new RemoteMessage[i];
    }
}
