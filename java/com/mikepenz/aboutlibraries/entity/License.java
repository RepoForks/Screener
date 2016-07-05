package com.mikepenz.aboutlibraries.entity;

public class License {
    private String definedName;
    private String licenseDescription;
    private String licenseName;
    private String licenseShortDescription;
    private String licenseWebsite;

    public License(String licenseName, String licenseWebsite, String licenseShortDescription, String licenseDescription) {
        this.licenseName = licenseName;
        this.licenseWebsite = licenseWebsite;
        this.licenseShortDescription = licenseShortDescription;
        this.licenseDescription = licenseDescription;
    }

    public License copy() {
        return new License(this.licenseName, this.licenseWebsite, this.licenseShortDescription, this.licenseDescription);
    }

    public String getDefinedName() {
        return this.definedName;
    }

    public void setDefinedName(String definedName) {
        this.definedName = definedName;
    }

    public String getLicenseName() {
        return this.licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public String getLicenseWebsite() {
        return this.licenseWebsite;
    }

    public void setLicenseWebsite(String licenseWebsite) {
        this.licenseWebsite = licenseWebsite;
    }

    public String getLicenseShortDescription() {
        return this.licenseShortDescription;
    }

    public void setLicenseShortDescription(String licenseShortDescription) {
        this.licenseShortDescription = licenseShortDescription;
    }

    public String getLicenseDescription() {
        return this.licenseDescription;
    }

    public void setLicenseDescription(String licenseDescription) {
        this.licenseDescription = licenseDescription;
    }
}
