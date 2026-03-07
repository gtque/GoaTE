package com.thegoate.annotations;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class AnnotationScannerBootJarIntegrationTest {

    @Test(groups = {"unit"})
    public void testBootStyleJarRunsScannerInForkedProcess() throws Exception {
        File tempDir = Files.createTempDirectory("annscan-boot-test").toFile();
        tempDir.deleteOnExit();

        // 1) Create nested jar with a dummy class entry (com/nested/InnerClass.class)
//        File classFile2 = new File(srcPkgDir, "com/thegoate/utils/compare/tools/CompareObjectNotEqualTo.class");
        byte[] nestedClassBytes;
        try (InputStream ais = AnnotationScanner.class.getResourceAsStream("/com/thegoate/utils/compare/tools/CompareObjectNotEqualTo.class");
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            if (ais == null) {
                throw new IllegalStateException("Could not locate AnnotationScanner.class resource to embed in test jar");
            }
            byte[] buf = new byte[4096];
            int r;
            while ((r = ais.read(buf)) != -1) bos.write(buf, 0, r);
            nestedClassBytes = bos.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read AnnotationScanner.class bytes", e);
        }
        File nestedJar = new File(tempDir, "nested.jar");
        try (FileOutputStream fos = new FileOutputStream(nestedJar);
             JarOutputStream jos = new JarOutputStream(fos)) {
            JarEntry e = new JarEntry("com/nested/InnerClass.class");
            jos.putNextEntry(e);
            jos.write(new byte[]{0x0});
            jos.closeEntry();

            JarEntry runnerEntry = new JarEntry("com/thegoate/utils/compare/tools/CompareObjectNotEqualTo.class");
            jos.putNextEntry(runnerEntry);
            jos.write(nestedClassBytes);
            jos.closeEntry();
        }

        // 2) Create a small TestRunner Java source that reflectively calls AnnotationScanner.scanJarFile and prints packages
        String runnerPkg = "com.thegoate.annotations";
        String runnerClass = "TestRunner";
        String runnerSource = "package " + runnerPkg + ";\n" +
                "public class " + runnerClass + " {\n" +
                "  public static void main(String[] args) {\n" +
                "    try {\n" +
                "      java.io.File jar = new java.io.File(System.getProperty(\"java.class.path\"));\n" +
                "      java.lang.reflect.Method m = com.thegoate.annotations.AnnotationScanner.class.getDeclaredMethod(\"getAnnotations\", new Class[0]);\n" +
                "      m.setAccessible(true);\n" +
                "      @SuppressWarnings(\"unchecked\")\n" +
                "      java.util.Map<String, java.util.List<Class<?>>> pkgs = (java.util.Map<String, java.util.List<Class<?>>>) m.invoke(null);\n" +
                "      for (String p : pkgs.keySet()) { " +
                "       String p2 = \"\"; " +
                "       for (Class<?> c : pkgs.get(p)) { " +
                "         p2 += c.getName() + \",\"; " +
                "       } " +
                "       System.out.println(p + \":\" + p2); " +
                "    }\n" +
                "    } catch (Throwable t) { t.printStackTrace(); System.exit(2); }\n" +
                "  }\n" +
                "}\n";

        // 3) Compile TestRunner source using the system Java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("No system Java compiler available. Please run tests with a JDK (ToolProvider.getSystemJavaCompiler() returned null).");
        }
        File srcDir = new File(tempDir, "src");
        File srcPkgDir = new File(srcDir, "com/thegoate/annotations");
        if (!srcPkgDir.mkdirs() && !srcPkgDir.exists()) {
            throw new IOException("Failed to create source package directory");
        }
        File sourceFile = new File(srcPkgDir, runnerClass + ".java");
        try (FileWriter fw = new FileWriter(sourceFile)) {
            fw.write(runnerSource);
        }
        // compile and capture diagnostics
        ByteArrayOutputStream compileOut = new ByteArrayOutputStream();
        int compileResult;
        try (PrintStream ps = new PrintStream(compileOut, true, "UTF-8")) {
            compileResult = compiler.run(null, ps, ps, sourceFile.getAbsolutePath());
        }
        if (compileResult != 0) {
            String diag = compileOut.toString("UTF-8");
            throw new IllegalStateException("Compilation of TestRunner failed with exit code: " + compileResult + "\n" + diag);
        }

        // Read compiled TestRunner.class bytes
        File classFile = new File(srcPkgDir, runnerClass + ".class");
        byte[] runnerClassBytes;
        try (FileInputStream fis = new FileInputStream(classFile);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int r;
            while ((r = fis.read(buf)) != -1) bos.write(buf, 0, r);
            runnerClassBytes = bos.toByteArray();
        }

        // Read nested jar bytes
        byte[] nestedBytes;
        try (FileInputStream fis = new FileInputStream(nestedJar);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int r;
            while ((r = fis.read(buf)) != -1) bos.write(buf, 0, r);
            nestedBytes = bos.toByteArray();
        }

//        import com.thegoate.annotations.AnnotationDNA;
//import com.thegoate.dsl.words.EutConfigDSL;
//import com.thegoate.reflection.GoateReflection;
//import com.thegoate.utils.fill.serialize.GoateSource;
        Map<String, byte[]> requiredClasses = new java.util.HashMap<>(Map.of(
                "com/thegoate/annotations/AnnotationScanner.class", new byte[0],
                "com/thegoate/annotations/AnnotationDNA.class", new byte[0],
                "com/thegoate/DNA.class", new byte[0],
                "com/thegoate/dsl/words/EutConfigDSL.class", new byte[0],
                "com/thegoate/reflection/GoateReflection.class", new byte[0],
                "com/thegoate/utils/fill/serialize/GoateSource.class", new byte[0],
                "com/thegoate/utils/fill/serialize/DefaultSource.class", new byte[0],
                "com/thegoate/utils/fill/serialize/GoateIgnore.class", new byte[0],
                "com/thegoate/annotations/Scan.class", new byte[0],
                "goate.yml", new byte[0]
        ));
        requiredClasses.putAll(new java.util.HashMap<>(Map.of(
                "com/thegoate/logging/volume/Diary.class", new byte[0],
                "com/thegoate/Goate.class", new byte[0],
                "com/thegoate/annotations/GoateDescription.class", new byte[0],
                "com/thegoate/dsl/GoateDSL.class", new byte[0],
                "com/thegoate/dsl/PrimitiveDSL.class", new byte[0],
                "com/thegoate/dsl/words/BooleanDSL.class", new byte[0],
                "com/thegoate/utils/compare/tools/CompareObject.class", new byte[0],
                "com/thegoate/utils/compare/tools/CompareObjectEqualTo.class", new byte[0],
                "com/thegoate/annotations/GoateScanner.class", new byte[0]
        )));
        requiredClasses.putAll(new java.util.HashMap<>(Map.of(
                "com/thegoate/HealthMonitor.class", new byte[0],
                "com/thegoate/utils/Utility.class", new byte[0],
                "com/thegoate/utils/UnknownUtilType.class", new byte[0],
                "com/thegoate/utils/compare/Compare.class", new byte[0],
                "com/thegoate/utils/compare/CompareUtil.class", new byte[0],
                "com/thegoate/utils/compare/CompareUtility.class", new byte[0],
                "com/thegoate/utils/compare/CompareTool.class", new byte[0],
                "com/thegoate/utils/compare/tools/CompareObject.class", new byte[0],
                "com/thegoate/utils/compare/tools/CompareObjectEqualTo.class", new byte[0]
        )));
        requiredClasses.keySet().forEach(key -> {
            // Read AnnotationScanner.class bytes from current runtime so forked process can load it
//            byte[] scannerClassBytes;
            try (InputStream ais = AnnotationScanner.class.getResourceAsStream("/"+key);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                if (ais == null) {
                    throw new IllegalStateException("Could not locate AnnotationScanner.class resource to embed in test jar");
                }
                byte[] buf = new byte[4096];
                int r;
                while ((r = ais.read(buf)) != -1) bos.write(buf, 0, r);
                requiredClasses.put(key, bos.toByteArray());
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read AnnotationScanner.class bytes", e);
            }
        });
        // 4) Create outer jar containing:
        //    - META-INF/MANIFEST.MF with Main-Class: com.thegoate.annotations.TestRunner
        //    - TestRunner.class at com/thegoate/annotations/TestRunner.class (so the runner is available)
        //    - AnnotationScanner.class at com/thegoate/annotations/AnnotationScanner.class (so the runner can reflectively call it)
        //    - BOOT-INF/lib/nested.jar (embedded nested jar)
        //    - BOOT-INF/classes/com/outer/OuterClass.class (dummy class)
        File outerJar = new File(tempDir, "boot-style-app.jar");
        try (FileOutputStream fos = new FileOutputStream(outerJar);
             JarOutputStream jos = new JarOutputStream(fos)) {
            // manifest
            JarEntry mf = new JarEntry("META-INF/MANIFEST.MF");
            jos.putNextEntry(mf);
            String manifest = "Manifest-Version: 1.0\nMain-Class: com.thegoate.annotations.TestRunner\n\n";
            jos.write(manifest.getBytes("UTF-8"));
            jos.closeEntry();

            // TestRunner.class
            JarEntry runnerEntry = new JarEntry("com/thegoate/annotations/" + runnerClass + ".class");
            jos.putNextEntry(runnerEntry);
            jos.write(runnerClassBytes);
            jos.closeEntry();

            // AnnotationScanner.class
            requiredClasses.forEach((key, bytes) -> {
            JarEntry scannerEntry = new JarEntry(key);
                try {
                    jos.putNextEntry(scannerEntry);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    jos.write(bytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    jos.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // nested jar under BOOT-INF/lib
            JarEntry nestedEntry = new JarEntry("BOOT-INF/lib/nested.jar");
            jos.putNextEntry(nestedEntry);
            jos.write(nestedBytes);
            jos.closeEntry();

            // add a dummy class under BOOT-INF/classes
            JarEntry outerClassEntry = new JarEntry("BOOT-INF/classes/com/outer/OuterClass.class");
            jos.putNextEntry(outerClassEntry);
            jos.write(new byte[]{0x0});
            jos.closeEntry();
        }

        // 5) Fork a new JVM process running the outer jar: `java -jar outerJar`
        String javaExec = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        ProcessBuilder pb = new ProcessBuilder(javaExec, "-jar", outerJar.getAbsolutePath());
        pb.redirectErrorStream(true);
        Process p = pb.start();

        // capture output
        StringBuilder out = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.append(line).append(System.lineSeparator());
            }
        }

        boolean exited = p.waitFor(20, TimeUnit.SECONDS);
        if (!exited) {
            p.destroyForcibly();
            throw new IllegalStateException("Forked java process did not exit within timeout");
        }
        int exitCode = p.exitValue();

        String output = out.toString();

        // cleanup temp files (best effort)
        nestedJar.delete();
        outerJar.delete();
        classFile.delete();
        sourceFile.delete();
        srcPkgDir.delete();
        new File(srcDir, "com/thegoate").delete();
        srcDir.delete();
        tempDir.delete();

        // verify process exited normally and output contains both package names
        Assert.assertEquals(exitCode, 0, "Forked java process exit code should be 0. Output:\n" + output);
//        Assert.assertTrue(output.contains("com.nested"), "Output should contain nested package 'com.nested'. Output:\n" + output);
        Assert.assertTrue(output.contains("com.thegoate.utils.compare.CompareUtil:com.thegoate.utils.compare.tools.CompareObjectEqualTo"), "Output should contain outer package 'com.outer'. Output:\n" + output);
//        Assert.assertFalse(output.contains("BOOT-INF"), "Output should not contain the stripped nested package prefix 'BOOT-INF'. Output:\n" + output);
    }
}
