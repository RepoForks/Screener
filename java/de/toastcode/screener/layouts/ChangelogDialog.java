package de.toastcode.screener.layouts;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.Theme;
import de.toastcode.screener.R;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChangelogDialog extends DialogFragment {
    public static ChangelogDialog create(boolean darkTheme, int accentColor) {
        ChangelogDialog dialog = new ChangelogDialog();
        Bundle args = new Bundle();
        args.putBoolean("dark_theme", darkTheme);
        args.putInt("accent_color", accentColor);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            View customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_webview, null);
            MaterialDialog dialog = new Builder(getActivity()).theme(getArguments().getBoolean("dark_theme") ? Theme.DARK : Theme.LIGHT).title((int) R.string.changelog).customView(customView, false).positiveText(17039370).build();
            WebView webView = (WebView) customView.findViewById(R.id.webview);
            try {
                StringBuilder buf = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("changelog.html"), "UTF-8"));
                while (true) {
                    String str = in.readLine();
                    if (str == null) {
                        break;
                    }
                    buf.append(str);
                }
                in.close();
                int accentColor = getArguments().getInt("accent_color");
                webView.loadData(buf.toString().replace("{style-placeholder}", getArguments().getBoolean("dark_theme") ? "body { background-color: #444444; color: #fff; }" : "body { background-color: #EDEDED; color: #000; }").replace("{link-color}", colorToHex(shiftColor(accentColor, true))).replace("{link-color-active}", colorToHex(accentColor)), "text/html", "UTF-8");
            } catch (Throwable e) {
                webView.loadData("<h1>Unable to load</h1><p>" + e.getLocalizedMessage() + "</p>", "text/html", "UTF-8");
            }
            return dialog;
        } catch (InflateException e2) {
            throw new IllegalStateException("This device does not support Web Views.");
        }
    }

    private String colorToHex(int color) {
        return Integer.toHexString(color).substring(2);
    }

    private int shiftColor(int color, boolean up) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = (up ? 1.1f : 0.9f) * hsv[2];
        return Color.HSVToColor(hsv);
    }
}
