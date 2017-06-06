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

package com.thegoate.utils.file;

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.GoateUtils;


import java.io.*;
import java.net.URL;

/**
 * Created by gtque on 5/3/2017.
 */
public class Copy {

    private final BleatBox LOG = BleatFactory.getLogger(getClass());

    private String originalFile = null;
    private File file = null;
    private InputStream fileInputStream = null;

    public Copy() {
    }

    /**
     * Creates a new copy with the given original file name.
     *
     * @param originalFile The path to the original file.
     */
    public Copy(String originalFile) {
        file(originalFile);
    }

    /**
     * Creates a new copy with the given original file name.
     *
     * @param file The original file.
     */
    public Copy(File file) {
        file(file);
    }

    public Copy(InputStream inputStream) {
        file(inputStream);
    }

    public Copy file(InputStream inputStream) {
        this.fileInputStream = inputStream;
        this.originalFile = null;
        this.file = null;
        return this;
    }

    /**
     * Sets the path to the original  file.
     *
     * @param originalFile The path to the original file.
     * @return Returns this as Syntactic sugar for stringing "to" together.
     */
    public Copy file(String originalFile) {
        this.fileInputStream = null;
        this.file = null;
        this.originalFile = GoateUtils.getFilePath(originalFile);
        return this;
    }

    /**
     * Sets the path to the original  file.
     *
     * @param file The path to the original file.
     * @return Returns this as Syntactic sugar for stringing "to" together.
     */
    public Copy file(File file) {
        this.fileInputStream = null;
        this.originalFile = null;
        this.file = file;
        return this;
    }

    public Copy file(URL originalUrl) {
        try {
            file(originalUrl.openStream());
            this.originalFile = originalUrl.getPath();
        } catch (IOException e) {
            LOG.error("Problem opening a stream to the url: " + e.getMessage(), e);
        }
        return this;
    }

    /**
     * Copies the original file to the new file.<br>
     * Will overwrite the destination file if it already exists.
     *
     * @param copyFile The destination to copy to.
     * @return True if successful, false if failed.
     */
    public String to(String copyFile) {
        return to(copyFile, true);
    }

    public String toDir(String destinationDir) {
        return to(destinationDir + "/" + getSource().getName(), true);
    }

    protected File getSource() {
        File sourceFile = null;
        if (originalFile != null)
            sourceFile = new File(originalFile);
        if (file != null)
            sourceFile = file;
        if (fileInputStream != null) {
            LOG.debug("you gave me an input stream. Unless you gave me the url, I cannot determine the sourceFile.");
        }
        return sourceFile;
    }

    /**
     * Copies the original file to the new file.
     *
     * @param copyFile  The destination to which to copy.
     * @param overwrite Whether to overwrite the destination file if it exists. True to overwrite, false to not.
     * @return True if successful, false if failed.
     */
    public String to(String copyFile, boolean overwrite) {

        File sourceFile = getSource();
        File destFile = new File(copyFile);
        String status = destFile.getAbsolutePath();
        if (destFile.exists()) {
            if (overwrite) {
                try {
                    if (destFile.delete())
                        LOG.debug("File deleted");
                    else {
                        LOG.debug("File could not be deleted");
                        status = null;
                    }
                } catch (Exception e) {
                    LOG.error("Copy File", e);
                    status = null;
                }
            } else {
                status = null;
            }
        }
        if (status != null) {
            try {
                LOG.debug("file: " + destFile.getAbsolutePath());
                if (destFile.getParentFile().mkdirs())
                    LOG.debug("directory did not exist, had to make it.");
                if (destFile.createNewFile()) {
                    LOG.debug("File created");
                    InputStream source = null;
                    FileOutputStream destination = null;

                    try {
                        if (sourceFile != null) {
                            if (fileInputStream == null) {
                                source = new FileInputStream(sourceFile);
                            } else {
                                source = fileInputStream;
                            }
                        } else if (fileInputStream != null) {
                            source = fileInputStream;
                        }
                        if (source != null) {
                            destination = new FileOutputStream(destFile);
                            byte[] buffer = new byte[4096];
                            int bytesRead = source.read(buffer);
                            while (bytesRead != -1) {
                                destination.write(buffer, 0, bytesRead);
                                bytesRead = source.read(buffer);
                            }
                        }
                    } finally {
                        if (source != null) {
                            source.close();
                        }
                        if (destination != null) {
                            destination.close();
                        }
                    }
                } else {
                    LOG.debug("File not created");
                    status = null;
                }
            } catch (Exception | Error e) {
                LOG.error("Copy failed" + e.getMessage(), e);
                status = null;
            }
        }
        return status;
    }
}
