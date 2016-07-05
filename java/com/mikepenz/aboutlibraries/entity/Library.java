package com.mikepenz.aboutlibraries.entity;

import me.zhanghai.android.materialprogressbar.BuildConfig;

public class Library implements Comparable<Library> {
    private String author = BuildConfig.FLAVOR;
    private String authorWebsite = BuildConfig.FLAVOR;
    private String classPath = BuildConfig.FLAVOR;
    private String definedName = BuildConfig.FLAVOR;
    private boolean internal = false;
    private boolean isOpenSource = true;
    private String libraryDescription = BuildConfig.FLAVOR;
    private String libraryName = BuildConfig.FLAVOR;
    private String libraryVersion = BuildConfig.FLAVOR;
    private String libraryWebsite = BuildConfig.FLAVOR;
    private License license;
    private String repositoryLink = BuildConfig.FLAVOR;

    public Library(String author, String libraryName, String libraryDescription) {
        this.author = author;
        this.libraryName = libraryName;
        this.libraryDescription = libraryDescription;
    }

    public Library(String author, String libraryName, String libraryDescription, String libraryVersion) {
        this.author = author;
        this.libraryName = libraryName;
        this.libraryDescription = libraryDescription;
        this.libraryVersion = libraryVersion;
    }

    public String getDefinedName() {
        return this.definedName;
    }

    public void setDefinedName(String definedName) {
        this.definedName = definedName;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorWebsite() {
        return this.authorWebsite;
    }

    public void setAuthorWebsite(String authorWebsite) {
        this.authorWebsite = authorWebsite;
    }

    public String getLibraryName() {
        return this.libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryDescription() {
        return this.libraryDescription;
    }

    public void setLibraryDescription(String libraryDescription) {
        this.libraryDescription = libraryDescription;
    }

    public String getLibraryVersion() {
        return this.libraryVersion;
    }

    public void setLibraryVersion(String libraryVersion) {
        this.libraryVersion = libraryVersion;
    }

    public String getLibraryWebsite() {
        return this.libraryWebsite;
    }

    public void setLibraryWebsite(String libraryWebsite) {
        this.libraryWebsite = libraryWebsite;
    }

    public License getLicense() {
        return this.license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public boolean isOpenSource() {
        return this.isOpenSource;
    }

    public void setOpenSource(boolean isOpenSource) {
        this.isOpenSource = isOpenSource;
    }

    public String getRepositoryLink() {
        return this.repositoryLink;
    }

    public void setRepositoryLink(String repositoryLink) {
        this.repositoryLink = repositoryLink;
    }

    public boolean isInternal() {
        return this.internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public String getClassPath() {
        return this.classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public int compareTo(Library another) {
        return getLibraryName().compareToIgnoreCase(another.getLibraryName());
    }
}
