package com.google.firebase.messaging;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceIdInternalReceiver;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.zzb;
import java.util.Iterator;
import me.zhanghai.android.materialprogressbar.R;

public class FirebaseMessagingService extends zzb {
    static void zzab(Bundle bundle) {
        Iterator it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str != null && str.startsWith("google.c.")) {
                it.remove();
            }
        }
    }

    private void zzad(Intent intent) {
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("pending_intent");
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (CanceledException e) {
                Log.e("FirebaseMessaging", "Notification pending intent canceled");
            }
        }
        if (zzav(intent.getExtras())) {
            zzb.zzm(this, intent);
        }
    }

    private String zzae(Intent intent) {
        String stringExtra = intent.getStringExtra("google.message_id");
        return stringExtra == null ? intent.getStringExtra("message_id") : stringExtra;
    }

    static boolean zzav(Bundle bundle) {
        return "1".equals(bundle.getString("google.c.a.e"));
    }

    private void zzn(Intent intent) {
        String stringExtra = intent.getStringExtra("message_type");
        if (stringExtra == null) {
            stringExtra = "gcm";
        }
        Object obj = -1;
        switch (stringExtra.hashCode()) {
            case -2062414158:
                if (stringExtra.equals("deleted_messages")) {
                    obj = 1;
                    break;
                }
                break;
            case 102161:
                if (stringExtra.equals("gcm")) {
                    obj = null;
                    break;
                }
                break;
            case 814694033:
                if (stringExtra.equals("send_error")) {
                    obj = 3;
                    break;
                }
                break;
            case 814800675:
                if (stringExtra.equals("send_event")) {
                    obj = 2;
                    break;
                }
                break;
        }
        switch (obj) {
            case R.styleable.View_android_theme /*0*/:
                if (zzav(intent.getExtras())) {
                    zzb.zzl(this, intent);
                }
                zzo(intent);
                return;
            case R.styleable.View_android_focusable /*1*/:
                onDeletedMessages();
                return;
            case R.styleable.View_paddingStart /*2*/:
                onMessageSent(intent.getStringExtra("google.message_id"));
                return;
            case R.styleable.View_paddingEnd /*3*/:
                onSendError(zzae(intent), new SendException(intent.getStringExtra(Extra.ERROR)));
                return;
            default:
                String str = "FirebaseMessaging";
                String str2 = "Received message with unknown type: ";
                stringExtra = String.valueOf(stringExtra);
                Log.w(str, stringExtra.length() != 0 ? str2.concat(stringExtra) : new String(str2));
                return;
        }
    }

    private void zzo(Intent intent) {
        Bundle extras = intent.getExtras();
        extras.remove("android.support.content.wakelockid");
        if (zza.zzac(extras)) {
            if (!zza.zzdc(this)) {
                zza.zzeo(this).zzas(extras);
                return;
            } else if (zzav(intent.getExtras())) {
                zzb.zzo(this, intent);
            }
        }
        onMessageReceived(new RemoteMessage(extras));
    }

    @WorkerThread
    public void onDeletedMessages() {
    }

    @WorkerThread
    public void onMessageReceived(RemoteMessage remoteMessage) {
    }

    @WorkerThread
    public void onMessageSent(String str) {
    }

    @WorkerThread
    public void onSendError(String str, Exception exception) {
    }

    protected int zzaa(Intent intent) {
        if (!"com.google.firebase.messaging.NOTIFICATION_OPEN".equals(intent.getAction())) {
            return super.zzaa(intent);
        }
        zzad(intent);
        zzbmb();
        FirebaseInstanceIdReceiver.completeWakefulIntent(intent);
        return 3;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void zzm(android.content.Intent r5) {
        /*
        r4 = this;
        r1 = r5.getAction();	 Catch:{ all -> 0x0047 }
        r0 = -1;
        r2 = r1.hashCode();	 Catch:{ all -> 0x0047 }
        switch(r2) {
            case 75300319: goto L_0x0039;
            case 366519424: goto L_0x002f;
            default: goto L_0x000c;
        };	 Catch:{ all -> 0x0047 }
    L_0x000c:
        switch(r0) {
            case 0: goto L_0x0043;
            case 1: goto L_0x004c;
            default: goto L_0x000f;
        };	 Catch:{ all -> 0x0047 }
    L_0x000f:
        r1 = "FirebaseMessaging";
        r2 = "Unknown intent action: ";
        r0 = r5.getAction();	 Catch:{ all -> 0x0047 }
        r0 = java.lang.String.valueOf(r0);	 Catch:{ all -> 0x0047 }
        r3 = r0.length();	 Catch:{ all -> 0x0047 }
        if (r3 == 0) goto L_0x005a;
    L_0x0021:
        r0 = r2.concat(r0);	 Catch:{ all -> 0x0047 }
    L_0x0025:
        android.util.Log.d(r1, r0);	 Catch:{ all -> 0x0047 }
    L_0x0028:
        r4.zzbmb();	 Catch:{ all -> 0x0047 }
        com.google.firebase.iid.FirebaseInstanceIdReceiver.completeWakefulIntent(r5);
        return;
    L_0x002f:
        r2 = "com.google.android.c2dm.intent.RECEIVE";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0047 }
        if (r1 == 0) goto L_0x000c;
    L_0x0037:
        r0 = 0;
        goto L_0x000c;
    L_0x0039:
        r2 = "com.google.firebase.messaging.NOTIFICATION_DISMISS";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0047 }
        if (r1 == 0) goto L_0x000c;
    L_0x0041:
        r0 = 1;
        goto L_0x000c;
    L_0x0043:
        r4.zzn(r5);	 Catch:{ all -> 0x0047 }
        goto L_0x0028;
    L_0x0047:
        r0 = move-exception;
        com.google.firebase.iid.FirebaseInstanceIdReceiver.completeWakefulIntent(r5);
        throw r0;
    L_0x004c:
        r0 = r5.getExtras();	 Catch:{ all -> 0x0047 }
        r0 = zzav(r0);	 Catch:{ all -> 0x0047 }
        if (r0 == 0) goto L_0x0028;
    L_0x0056:
        com.google.firebase.messaging.zzb.zzn(r4, r5);	 Catch:{ all -> 0x0047 }
        goto L_0x0028;
    L_0x005a:
        r0 = new java.lang.String;	 Catch:{ all -> 0x0047 }
        r0.<init>(r2);	 Catch:{ all -> 0x0047 }
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.messaging.FirebaseMessagingService.zzm(android.content.Intent):void");
    }

    protected Intent zzz(Intent intent) {
        return FirebaseInstanceIdInternalReceiver.zzcwz();
    }
}
