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
package com.thegoate.gradle

/**
 * @since 1.0
 */
class GoateGradleHelper {

    public static void customPom(def pom, def configurations, def javaVersion, boolean oneOff=false, String rgroup="goate") {
        pom.withXml {
            System.out.println("looking for internal dependencies that need to be set.")
            for (Node n : asNode().children()) {
                if (n.name().toString().contains("dependencies")) {
                    System.out.println("found the list of dependencies.")
                    for (Node dependency : n.children()) {
                        if (dependency.get("groupId").size() > 0) {
                            String gid = dependency.get("groupId")[0].value()[0].toString()
                            if (!oneOff&&gid.equalsIgnoreCase(rgroup)) {
                                if (dependency.get("artifactId").size() > 0) {
                                    dependency.get("artifactId")[0].setValue(dependency.get("artifactId")[0].value()[0].toString() + javaVersion)
                                }
                            }
                            if (dependency.get("version").size() > 0) {
                                String version = dependency.get("version")[0].value()[0].toString()
                                if (version.contains("+")) {
                                    if (dependency.get("artifactId").size() > 0) {
                                        String aid = dependency.get("artifactId")[0].value()[0].toString()
                                        System.out.println("\t" + gid + ":" + aid + " will be set to most recent version available.")
                                        if (configurations.runtime.resolvedConfiguration.resolvedArtifacts == null) {
                                            System.out.println("allDependencies was null.")
                                        } else {
                                            configurations.runtime.resolvedConfiguration.resolvedArtifacts.each {
                                                if (it.moduleVersion.id.group.equals(gid)) {
                                                    if (it.moduleVersion.id.name.equals(aid)) {
                                                        version = it.moduleVersion.id.version
                                                        System.out.println("\t\t" + it.moduleVersion.id.group + ":" +
                                                                it.moduleVersion.id.name + ":" +
                                                                it.moduleVersion.id.version)
                                                    }
                                                }
                                            }
                                        }
                                        dependency.get("version")[0].setValue(version)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
