/*
 * Copyright (c) 2017. Eric Angeli
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
package com.thegoate.gradle.test.plugin;

import com.thegoate.gradle.BaseGoatePlugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.testing.Test;

/**
 * Define the test closure.
 * Created by Eric Angeli on 5/17/2017.
 */
public class TestPlugin extends BaseGoatePlugin {
    @Override
    public void apply(Project project) {
        dependsOn(project, "java");
        dependsOn(project, "jacoco");
        ((Test) project.getTasks().getByName("test")).configure({
            //def testRemote = project.hasProperty("remoteTest")?project.remoteTest.equals("true"):false;
            def testGroups = project.hasProperty("testGroups") ? (project.testGroups.equals("all") ? "unit,integration,api" : project.testGroups) : 'unit'
            def excludedGroups = project.hasProperty("excludeGroups") ? project.excludeGroup : ''
            def testSuite = project.hasProperty("testSuite") ? project.testSuite : ''
            def forks = project.hasProperty("forks") ? (project.forks.isEmpty() ? "1" : project.forks) : '1'
            def forksEvery = project.hasProperty("forksEvery") ? (project.forksEvery.isEmpty() ? "" : project.forksEvery) : ''
            def threads = project.hasProperty("threads") ? (project.threads.isEmpty() ? "1" : project.threads) : '1'
            exclude "**/GradleExcludeTrue1*"
            exclude "**/GradleExcludeTrue2*", "**/GradleExcludeTrue3*"
            systemProperties System.getProperties();
            systemProperty "spring.freemarker.checkTemplateLocation", "false";
            systemProperty "spring.velocity.checkTemplateLocation", "false";
            systemProperty "user.dir", project.buildscript.sourceFile.parentFile.path
            useTestNG() {
                listeners << "org.testng.reporters.XMLReporter"
                if (testSuite.equals('')) {
                    maxParallelForks = Long.parseLong("${forks}")
                    if (!forksEvery.isEmpty()) {
                        forkEvery = Long.parseLong("${forksEvery}")
                    }
                    parallel = "tests"
                    threadCount = Long.parseLong("${threads}")
                    if (!testGroups.equals('')) {
                        includeGroups "${testGroups}"
                    }
                    if (!excludedGroups.equals('')) {
                        excludeGroups "${excludedGroups}"
                    }
                } else {
                    suites "${testSuite}"
                }
            }
        });
        project.getTasks().getByName("test").finalizedBy(project.getTasks().getByName("jacocoTestReport"));
    }
}
