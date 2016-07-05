package de.toastcode.screener.layouts;

public class Devices {
    private boolean mDevice_downloaded;
    private String mFile_glare;
    private String mFile_name;
    private String mFile_shadow;
    private String mFile_thumb;
    private String mName;
    private int mThumb_id;

    public Devices(String name, int thumb_id, boolean device_downloaded, String file_name, String file_thumb, String file_glare, String file_shadow) {
        if (name != null && thumb_id != 0 && file_name != null && file_thumb != null && file_glare != null && file_shadow != null) {
            this.mName = name;
            this.mThumb_id = thumb_id;
            this.mDevice_downloaded = device_downloaded;
            this.mFile_name = file_name;
            this.mFile_thumb = file_thumb;
            this.mFile_glare = file_glare;
            this.mFile_shadow = file_shadow;
        }
    }

    public String getmName() {
        return this.mName;
    }

    public int getmThumb_id() {
        return this.mThumb_id;
    }

    public boolean getmDevice_downloaded() {
        return this.mDevice_downloaded;
    }

    public String getmFile_name() {
        return this.mFile_name;
    }

    public String getmFile_thumb() {
        return this.mFile_thumb;
    }

    public String getmFile_glare() {
        return this.mFile_glare;
    }

    public String getmFile_shadow() {
        return this.mFile_shadow;
    }
}
