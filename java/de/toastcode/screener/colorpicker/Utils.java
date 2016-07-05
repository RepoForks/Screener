package de.toastcode.screener.colorpicker;

import android.content.Context;
import android.graphics.Color;
import de.toastcode.screener.R;

public class Utils {

    public static class ColorUtils {
        public static int[] colorChoice(Context context) {
            int[] mColorChoices = null;
            String[] color_array = context.getResources().getStringArray(R.array.default_color_choice_values);
            if (color_array != null && color_array.length > 0) {
                mColorChoices = new int[color_array.length];
                for (int i = 0; i < color_array.length; i++) {
                    mColorChoices[i] = Color.parseColor(color_array[i]);
                }
            }
            return mColorChoices;
        }

        public static int parseWhiteColor() {
            return Color.parseColor("#FFFFFF");
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }
}
