package de.toastcode.screener.layouts;

public class Company {
    private String mCompany;
    private String mThumbId;

    public Company(String company, String thumbId) {
        if (company != null && thumbId != null) {
            this.mCompany = company;
            this.mThumbId = thumbId;
        }
    }

    public String getmCompany() {
        return this.mCompany;
    }

    public String getmThumbId() {
        return this.mThumbId;
    }
}
