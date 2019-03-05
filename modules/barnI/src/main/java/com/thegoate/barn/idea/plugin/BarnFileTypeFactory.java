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
package com.thegoate.barn.idea.plugin;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Eric Angeli on 8/17/2018.
 */
public class BarnFileTypeFactory extends FileTypeFactory implements ApplicationComponent {

    public static BarnFileTypeFactory getInstance() {
        Application application = ApplicationManager.getApplication();
        return application.getComponent(BarnFileTypeFactory.class);
    }

    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(BarnFileType.INSTANCE);
    }

    public boolean isBarn(VirtualFile file){
        return file.getExtension().equalsIgnoreCase("barn");
    }

    public FileType getBarnFileType(){
        return BarnFileType.INSTANCE;
    }
}
