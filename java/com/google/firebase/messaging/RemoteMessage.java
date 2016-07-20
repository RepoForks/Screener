package com.google.firebase.messaging;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.util.Map;
import java.util.Map.Entry;

public final class RemoteMessage extends AbstractSafeParcelable {
    public static final Creator<RemoteMessage> CREATOR = new zzc();
    Bundle bM;
    private Notification bbb;
    final int mVersionCode;
    private Map<String, String> zzctg;

    public static class Builder {
        private final Bundle bM = new Bundle();
        private final Map<String, String> zzctg = new ArrayMap();

        public Builder(String str) {
            if (TextUtils.isEmpty(str)) {
                String str2 = "Invalid to: ";
                String valueOf = String.valueOf(str);
                throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            }
            this.bM.putString("google.to", str);
        }

        public Builder addData(String str, String str2) {
            this.zzctg.put(str, str2);
            return this;
        }

        public RemoteMessage build() {
            Bundle bundle = new Bundle();
            for (Entry entry : this.zzctg.entrySet()) {
                bundle.putString((String) entry.getKey(), (String) entry.getValue());
            }
            bundle.putAll(this.bM);
            return new RemoteMessage(bundle);
        }

        public Builder clearData() {
            this.zzctg.clear();
            return this;
        }

        public Builder setCollapseKey(String str) {
            this.bM.putString("collapse_key", str);
            return this;
        }

        public Builder setData(Map<String, String> map) {
            this.zzctg.clear();
            this.zzctg.putAll(map);
            return this;
        }

        public Builder setMessageId(String str) {
            this.bM.putString("google.message_id", str);
            return this;
        }

        public Builder setMessageType(String str) {
            this.bM.putString("message_type", str);
            return this;
        }

        public Builder setTtl(int i) {
            this.bM.putString("google.ttl", String.valueOf(i));
            return this;
        }
    }

    public static class Notification {
        private final String Fx;
        private final String Lc;
        private final String bbc;
        private final String[] bbd;
        private final String bbe;
        private final String[] bbf;
        private final String bbg;
        private final String bbh;
        private final String bbi;
        private final String mTag;
        private final String zzbfm;

        private Notification(Bundle bundle) {
            this.Fx = zza.zzf(bundle, "gcm.n.title");
            this.bbc = zza.zzh(bundle, "gcm.n.title");
            this.bbd = zzj(bundle, "gcm.n.title");
            this.zzbfm = zza.zzf(bundle, "gcm.n.body");
            this.bbe = zza.zzh(bundle, "gcm.n.body");
            this.bbf = zzj(bundle, "gcm.n.body");
            this.bbg = zza.zzf(bundle, "gcm.n.icon");
            this.bbh = zza.zzat(bundle);
            this.mTag = zza.zzf(bundle, "gcm.n.tag");
            this.Lc = zza.zzf(bundle, "gcm.n.color");
            this.bbi = zza.zzf(bundle, "gcm.n.click_action");
        }

        private String[] zzj(Bundle bundle, String str) {
            Object[] zzi = zza.zzi(bundle, str);
            if (zzi == null) {
                return null;
            }
            String[] strArr = new String[zzi.length];
            for (int i = 0; i < zzi.length; i++) {
                strArr[i] = String.valueOf(zzi[i]);
            }
            return strArr;
        }

        public String getBody() {
            return this.zzbfm;
        }

        public String[] getBodyLocalizationArgs() {
            return this.bbf;
        }

        public String getBodyLocalizationKey() {
            return this.bbe;
        }

        public String getClickAction() {
            return this.bbi;
        }

        public String getColor() {
            return this.Lc;
        }

        public String getIcon() {
            return this.bbg;
        }

        public String getSound() {
            return this.bbh;
        }

        public String getTag() {
            return this.mTag;
        }

        public String getTitle() {
            return this.Fx;
        }

        public String[] getTitleLocalizationArgs() {
            return this.bbd;
        }

        public String getTitleLocalizationKey() {
            return this.bbc;
        }
    }

    RemoteMessage(int i, Bundle bundle) {
        this.mVersionCode = i;
        this.bM = bundle;
    }

    RemoteMessage(Bundle bundle) {
        this(1, bundle);
    }

    public String getCollapseKey() {
        return this.bM.getString("collapse_key");
    }

    public Map<String, String> getData() {
        if (this.zzctg == null) {
            this.zzctg = new ArrayMap();
            for (String str : this.bM.keySet()) {
                Object obj = this.bM.get(str);
                if (obj instanceof String) {
                    String str2 = (String) obj;
                    if (!(str.startsWith("google.") || str.startsWith("gcm.") || str.equals("from") || str.equals("message_type") || str.equals("collapse_key"))) {
                        this.zzctg.put(str, str2);
                    }
                }
            }
        }
        return this.zzctg;
    }

    public String getFrom() {
        return this.bM.getString("from");
    }

    public String getMessageId() {
        String string = this.bM.getString("google.message_id");
        return string == null ? this.bM.getString("message_id") : string;
    }

    public String getMessageType() {
        return this.bM.getString("message_type");
    }

    public Notification getNotification() {
        if (this.bbb == null && zza.zzac(this.bM)) {
            this.bbb = new Notification(this.bM);
        }
        return this.bbb;
    }

    public long getSentTime() {
        return this.bM.getLong("google.sent_time");
    }

    public String getTo() {
        return this.bM.getString("google.to");
    }

    public int getTtl() {
        Object obj = this.bM.get("google.ttl");
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                String valueOf = String.valueOf(obj);
                Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(valueOf).length() + 13).append("Invalid TTL: ").append(valueOf).toString());
            }
        }
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }

    void zzaf(Intent intent) {
        intent.putExtras(this.bM);
    }
}
