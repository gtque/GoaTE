/*
 * Copyright (c) 2023. Eric Angeli
 *
 *  Permission is hereby granted, free of charge,
 *  to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission
 *  notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 *  AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */
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

    public EutSettings configured() {
        this.configured = true;
        return this;
    }

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
