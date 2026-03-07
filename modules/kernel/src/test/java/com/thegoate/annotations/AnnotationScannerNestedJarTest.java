package com.thegoate.annotations;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class AnnotationScannerNestedJarTest {

    @Test(groups = {"unit"})
    public void testNestedJarPackageDiscovery() throws Exception {
        File tempDir = Files.createTempDirectory("annscan-test").toFile();
        tempDir.deleteOnExit();

        // Create a simple nested jar containing a dummy class entry
        File nestedJar = new File(tempDir, "nested.jar");
        try (FileOutputStream fos = new FileOutputStream(nestedJar);
             JarOutputStream jos = new JarOutputStream(fos)) {
            JarEntry e = new JarEntry("com/nested/InnerClass.class");
            jos.putNextEntry(e);
            // write a few bytes; content doesn't need to be a valid .class for package discovery
            jos.write(new byte[]{0x0});
            jos.closeEntry();
        }

        // Read nested jar bytes
        byte[] nestedBytes;
        try (FileInputStream fis = new FileInputStream(nestedJar);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int r;
            while ((r = fis.read(buf)) != -1) {
                bos.write(buf, 0, r);
            }
            nestedBytes = bos.toByteArray();
        }

        // Create outer jar and embed nested jar under BOOT-INF/lib/, also add a class under BOOT-INF/classes/
        File outerJar = new File(tempDir, "outer.jar");
        try (FileOutputStream fos = new FileOutputStream(outerJar);
             JarOutputStream jos = new JarOutputStream(fos)) {
            // add nested jar as an entry
            JarEntry nestedEntry = new JarEntry("BOOT-INF/lib/nested.jar");
            jos.putNextEntry(nestedEntry);
            jos.write(nestedBytes);
            jos.closeEntry();

            // add a class under BOOT-INF/classes
            JarEntry classEntry = new JarEntry("BOOT-INF/classes/com/outer/OuterClass.class");
            jos.putNextEntry(classEntry);
            jos.write(new byte[]{0x0});
            jos.closeEntry();
        }

        // invoke private scanJarFile(File) via reflection
        Class<?> scannerClass = AnnotationScanner.class;
        Method m = scannerClass.getDeclaredMethod("scanJarFile", File.class);
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> packages = (List<String>) m.invoke(null, outerJar);

        // verify packages discovered include both nested and outer packages
        Assert.assertTrue(packages.contains("com.nested"), "nested package com.nested should be discovered");
        Assert.assertTrue(packages.contains("com.outer"), "outer package com.outer should be discovered");

        // cleanup
        nestedJar.delete();
        outerJar.delete();
        tempDir.delete();
    }
}
