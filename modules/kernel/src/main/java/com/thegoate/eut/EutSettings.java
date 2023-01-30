package com.thegoate.eut;

import com.thegoate.utils.fill.serialize.Kid;

public class EutSettings extends Kid {

    private String location = "eut";
    private String pattern;
    private String extension;
    private String defaultFile;
    private String defaultProfile;
    private boolean useEutSettings = false;
    private boolean configured = false;

    public String getLocation() {
        return location;
    }

    public EutSettings setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getPattern() {
        return pattern;
    }

    public EutSettings setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public EutSettings setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public String getDefaultFile() {
        return defaultFile;
    }

    public EutSettings setDefaultFile(String defaultFile) {
        this.defaultFile = defaultFile;
        return this;
    }

    public String getDefaultProfile() {
        return defaultProfile;
    }

    public EutSettings setDefaultProfile(String defaultProfile) {
        this.defaultProfile = defaultProfile;
        return this;
    }

    public boolean isUseEutSettings() {
        return useEutSettings;
    }

    public EutSettings setUseEutSettings(boolean useEutSettings) {
        this.useEutSettings = useEutSettings;
        return this;
    }

    public boolean isConfigured() {
        return configured;
    }

    public EutSettings setConfigured(boolean configured) {
        this.configured = configured;
        return this;
    }
}
