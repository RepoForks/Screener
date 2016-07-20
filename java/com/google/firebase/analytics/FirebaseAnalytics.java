package com.google.firebase.analytics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.measurement.internal.zzx;

public final class FirebaseAnalytics {
    private final zzx aja;

    public static class Event {
        public static final String ADD_PAYMENT_INFO = "add_payment_info";
        public static final String ADD_TO_CART = "add_to_cart";
        public static final String ADD_TO_WISHLIST = "add_to_wishlist";
        public static final String APP_OPEN = "app_open";
        public static final String BEGIN_CHECKOUT = "begin_checkout";
        public static final String ECOMMERCE_PURCHASE = "ecommerce_purchase";
        public static final String GENERATE_LEAD = "generate_lead";
        public static final String JOIN_GROUP = "join_group";
        public static final String LEVEL_UP = "level_up";
        public static final String LOGIN = "login";
        public static final String POST_SCORE = "post_score";
        public static final String PRESENT_OFFER = "present_offer";
        public static final String PURCHASE_REFUND = "purchase_refund";
        public static final String SEARCH = "search";
        public static final String SELECT_CONTENT = "select_content";
        public static final String SHARE = "share";
        public static final String SIGN_UP = "sign_up";
        public static final String SPEND_VIRTUAL_CURRENCY = "spend_virtual_currency";
        public static final String TUTORIAL_BEGIN = "tutorial_begin";
        public static final String TUTORIAL_COMPLETE = "tutorial_complete";
        public static final String UNLOCK_ACHIEVEMENT = "unlock_achievement";
        public static final String VIEW_ITEM = "view_item";
        public static final String VIEW_ITEM_LIST = "view_item_list";
        public static final String VIEW_SEARCH_RESULTS = "view_search_results";

        protected Event() {
        }
    }

    public static class Param {
        public static final String ACHIEVEMENT_ID = "achievement_id";
        public static final String CHARACTER = "character";
        public static final String CONTENT_TYPE = "content_type";
        public static final String COUPON = "coupon";
        public static final String CURRENCY = "currency";
        public static final String DESTINATION = "destination";
        public static final String END_DATE = "end_date";
        public static final String FLIGHT_NUMBER = "flight_number";
        public static final String GROUP_ID = "group_id";
        public static final String ITEM_CATEGORY = "item_category";
        public static final String ITEM_ID = "item_id";
        public static final String ITEM_LOCATION_ID = "item_location_id";
        public static final String ITEM_NAME = "item_name";
        public static final String LEVEL = "level";
        public static final String LOCATION = "location";
        public static final String NUMBER_OF_NIGHTS = "number_of_nights";
        public static final String NUMBER_OF_PASSENGERS = "number_of_passengers";
        public static final String NUMBER_OF_ROOMS = "number_of_rooms";
        public static final String ORIGIN = "origin";
        public static final String PRICE = "price";
        public static final String PRODUCT_CATEGORY = "product_category";
        public static final String PRODUCT_ID = "product_id";
        public static final String PRODUCT_NAME = "product_name";
        public static final String QUANTITY = "quantity";
        public static final String SCORE = "score";
        public static final String SEARCH_TERM = "search_term";
        public static final String SHIPPING = "shipping";
        public static final String SIGN_UP_METHOD = "sign_up_method";
        public static final String START_DATE = "start_date";
        public static final String TAX = "tax";
        public static final String TRANSACTION_ID = "transaction_id";
        public static final String TRAVEL_CLASS = "travel_class";
        public static final String VALUE = "value";
        public static final String VIRTUAL_CURRENCY_NAME = "virtual_currency_name";

        protected Param() {
        }
    }

    public static class UserProperty {
        public static final String SIGN_UP_METHOD = "sign_up_method";

        protected UserProperty() {
        }
    }

    public FirebaseAnalytics(zzx com_google_android_gms_measurement_internal_zzx) {
        zzab.zzaa(com_google_android_gms_measurement_internal_zzx);
        this.aja = com_google_android_gms_measurement_internal_zzx;
    }

    public static FirebaseAnalytics getInstance(Context context) {
        return zzx.zzdo(context).amE;
    }

    public void logEvent(@NonNull @Size(max = 32, min = 1) String str, Bundle bundle) {
        this.aja.zzbun().logEvent(str, bundle);
    }

    public void setAnalyticsCollectionEnabled(boolean z) {
        this.aja.zzbun().setMeasurementEnabled(z);
    }

    public void setMinimumSessionDuration(long j) {
        this.aja.zzbun().setMinimumSessionDuration(j);
    }

    public void setSessionTimeoutDuration(long j) {
        this.aja.zzbun().setSessionTimeoutDuration(j);
    }

    public void setUserId(String str) {
        this.aja.zzbun().setUserId(str);
    }

    public void setUserProperty(@NonNull @Size(max = 24, min = 1) String str, @Size(max = 36) @Nullable String str2) {
        this.aja.zzbun().setUserProperty(str, str2);
    }
}
