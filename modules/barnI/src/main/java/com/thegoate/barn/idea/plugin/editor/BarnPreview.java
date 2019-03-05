package com.thegoate.barn.idea.plugin.editor;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;

import java.io.IOException;

/**
 * Created by Eric Angeli on 9/26/2018.
 */
public class BarnPreview implements VirtualFileListener {
    Project project;
    VirtualFile file;
    PreviewUI preview;
    String contents = "";

    BarnPreview(Project project, VirtualFile file){
        this.project = project;
        this.file = file;
        this.preview = new PreviewUI(this);
        setValue(file);
    }

    private void setValue(VirtualFile file){
        try {
            contents = new String(file.contentsToByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            contents = e.getMessage();
        }
    }

    public String contents(){
        return contents;
    }
}
