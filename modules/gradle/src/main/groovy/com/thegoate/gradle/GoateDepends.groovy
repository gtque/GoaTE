/**
 Copyright (c) 2017 Eric Angeli

 Permission is hereby granted, free of charge,
 to any person obtaining a copy of this software
 and associated documentation files (the "Software"),
 to deal in the Software without restriction,
 including without limitation the rights to use, copy,
 modify, merge, publish, distribute, sublicense,
 and/or sell copies of the Software, and to permit
 persons to whom the Software is furnished to do so,
 subject to the following conditions:

 The above copyright notice and this permission
 notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 DEALINGS IN THE SOFTWARE.
 */
package com.thegoate.gradle;

import org.gradle.api.Project

/**
 * @since 1.0
 */
public class GoateDepends {
    Project prj;
    String javaVersion = "";
    String group = "goate";

    public GoateDepends(Project project){
        this.prj = project;
        this.javaVersion = project.hasProperty("javaVersion")?project.javaVersion:"";
    }

    public GoateDepends(Project project, String group){
        this.prj = project;
        this.group = group;
    }
    public GoateDepends(Project project, String group, int javaVersion) {
        this.prj = project;
        this.javaVersion = ""+javaVersion;
        this.group = group;
    }
    public GoateDepends(Project project, int javaVersion) {
        this.prj = project;
        this.javaVersion = ""+javaVersion;
    }

    public Object depends(String dependency){
        return depends(dependency, "");
    }

    public Object depends(String dependency, String baseVer) {
        return depends(group, dependency, baseVer, false);
    }

    public Object depends(String dependency, String baseVer, boolean override) {
        return depends(group, dependency, baseVer, override);
    }

    public Object depends(String group, String dependency, String baseVer) {
        return depends(group, dependency, baseVer, false);
    }

    public Object depends(String group, String dependency, String baseVer, boolean override) {
        Object d;
        if (!override && (!prj.hasProperty("oneOff") || prj.oneOff.isEmpty())) {
            d = prj.rootProject.findProject((!dependency.startsWith(":")?":":"")+dependency);
        } else {
            if(prj.hasProperty("projectJavaLabel") && Boolean.parseBoolean("" + prj.projectJavaLabel)) {
                dependency += javaVersion;
            }
            d = group + (!dependency.startsWith(":")?":":"") + dependency + ":" + (baseVer != null && !baseVer.isEmpty() ? baseVer : "") + "+";
        }
        return d;
    }
}
