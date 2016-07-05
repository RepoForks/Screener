package com.mikepenz.materialdrawer.icons;

import android.content.Context;
import android.graphics.Typeface;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.ITypeface;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class MaterialDrawerFont implements ITypeface {
    private static final String TTF_FILE = "materialdrawerfont-font-v5.0.0.ttf";
    private static HashMap<String, Character> mChars;
    private static Typeface typeface = null;

    public enum Icon implements IIcon {
        mdf_arrow_drop_down('\ue5c5'),
        mdf_arrow_drop_up('\ue5c7'),
        mdf_expand_less('\ue5ce'),
        mdf_expand_more('\ue5cf'),
        mdf_person('\ue7fd');
        
        private static ITypeface typeface;
        char character;

        private Icon(char character) {
            this.character = character;
        }

        public String getFormattedName() {
            return "{" + name() + "}";
        }

        public char getCharacter() {
            return this.character;
        }

        public String getName() {
            return name();
        }

        public ITypeface getTypeface() {
            if (typeface == null) {
                typeface = new MaterialDrawerFont();
            }
            return typeface;
        }
    }

    public IIcon getIcon(String key) {
        return Icon.valueOf(key);
    }

    public HashMap<String, Character> getCharacters() {
        if (mChars == null) {
            HashMap<String, Character> aChars = new HashMap();
            for (Icon v : Icon.values()) {
                aChars.put(v.name(), Character.valueOf(v.character));
            }
            mChars = aChars;
        }
        return mChars;
    }

    public String getMappingPrefix() {
        return "mdf";
    }

    public String getFontName() {
        return "MaterialDrawerFont";
    }

    public String getVersion() {
        return "5.0.0";
    }

    public int getIconCount() {
        return mChars.size();
    }

    public Collection<String> getIcons() {
        Collection<String> icons = new LinkedList();
        for (Icon value : Icon.values()) {
            icons.add(value.name());
        }
        return icons;
    }

    public String getAuthor() {
        return BuildConfig.FLAVOR;
    }

    public String getUrl() {
        return BuildConfig.FLAVOR;
    }

    public String getDescription() {
        return BuildConfig.FLAVOR;
    }

    public String getLicense() {
        return BuildConfig.FLAVOR;
    }

    public String getLicenseUrl() {
        return BuildConfig.FLAVOR;
    }

    public Typeface getTypeface(Context context) {
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/materialdrawerfont-font-v5.0.0.ttf");
            } catch (Exception e) {
                return null;
            }
        }
        return typeface;
    }
}
