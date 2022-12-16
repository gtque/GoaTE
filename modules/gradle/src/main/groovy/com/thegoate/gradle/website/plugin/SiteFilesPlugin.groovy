package com.thegoate.gradle.website.plugin

import com.thegoate.gradle.BaseGoatePlugin
import org.gradle.api.Project

class SiteFilesPlugin extends BaseGoatePlugin {


    @Override
    public void apply(Project project) {
        def extension = project.extensions.create('siteFiles', SiteFilesExtension)
        poject.task('publishSiteFiles'){
        }
    }
}
