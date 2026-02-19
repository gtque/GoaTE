package com.thegoate.annotations;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import static com.thegoate.DNA.dna;

public class AnnotationScanner {

    private static final Map<String, List<Class<?>>> annotationCache = new ConcurrentHashMap<>();
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    public static Map<String, List<Class<?>>> getAnnotations() {
        synchronized (processing) {
            processing.set(true);
            if (annotationCache.isEmpty()) {
                scanClasses();
            }
            processing.set(false);
        }
        return annotationCache;
    }

    private static void scanClasses() {
        List<String> packageNamesFinal = new ArrayList<>();
        final List<String> includeInScan = new ArrayList<>();
        try {
            String classpath = System.getProperty("java.class.path");
            String[] classpathEntries = classpath.split(System.getProperty("path.separator"));
            List<String> packageNames = new ArrayList<>();
            if (dna.annotations != null) {
                includeInScan.addAll(dna.annotations.listOfPackagesToIncludeInTheScan());
            }
            for(String resource: classpathEntries) {
                File file = new File(resource);

                if (file.isDirectory()) {
                    scanDirectory(file, "").forEach(aPackage -> {
                        if(scan(aPackage, includeInScan)) {
                            packageNames.add(aPackage);
                        }
                    });
                } else if (file.getName().endsWith(".jar")) {
                    scanJarFile(file).forEach(aPackage -> {
                        if(scan(aPackage, includeInScan)) {
                            packageNames.add(aPackage);
                        }
                    });
                }
            }

            Package[] packages = ClassLoader.getSystemClassLoader().getDefinedPackages();
            Arrays.stream(packages)
                    .filter(aPackage -> scan(aPackage.getName(), includeInScan))
                    .forEach(aPackage -> packageNames.add(aPackage.getName()));
            packageNamesFinal = packageNames.stream()
                    .distinct()
                    .toList();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<Class<?>> klasses = new ArrayList<>();
        packageNamesFinal.forEach(p -> {
            try {
                List<Class<?>> currentKlasses = findClasses(p, includeInScan);
                klasses.addAll(currentKlasses);
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        checkForGoateAnnotations(klasses);
    }

    private static boolean scan(String aPackage, List<String> includeInScan) {
        if (aPackage.startsWith("com.thegoate")) {
            return true;
        }
        if (includeInScan != null && !includeInScan.isEmpty()) {
            return includeInScan.stream().anyMatch(aPackage::startsWith);
        }
        return false;
    }

    private static List<String> scanDirectory(File directory, String packageName) throws ClassNotFoundException {
        List<String> packageNames = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            boolean packageFound = false;
            for (File file : files) {
                if (file.isDirectory()) {
                    packageNames.addAll(scanDirectory(file, packageName + file.getName() + "."));
                } else if (file.getName().endsWith(".class")) {
                    packageFound = true;
                }
            }
            if (packageFound) {
                packageNames.add(packageName.replaceAll("\\.$", ""));
            }
        }
        return packageNames;
    }

    private static List<String> scanJarFile(File file) throws IOException, ClassNotFoundException {
        // Use a set to deduplicate package names discovered in the jar and any nested jars
        Set<String> packageNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                // If this is a class file, derive its package and add it
                if (!entry.isDirectory() && name.endsWith(".class")) {
                    int idx = name.lastIndexOf('/');
                    if (idx > 0) {
                        String packageName = name.substring(0, idx).replace('/', '.');
                        if (!packageName.isEmpty()) {
                            packageNames.add(packageName);
                        }
                    }
                }
                // If this is a nested jar (e.g., Spring Boot's BOOT-INF/lib/foo.jar), stream it and inspect its entries
                if (!entry.isDirectory() && name.endsWith(".jar")) {
                    try (InputStream nestedIs = jarFile.getInputStream(entry);
                         JarInputStream jis = new JarInputStream(nestedIs)) {
                        JarEntry nEntry;
                        while ((nEntry = jis.getNextJarEntry()) != null) {
                            String nName = nEntry.getName();
                            if (!nEntry.isDirectory() && nName.endsWith(".class")) {
                                int nidx = nName.lastIndexOf('/');
                                if (nidx > 0) {
                                    String packageName = nName.substring(0, nidx).replace('/', '.');
                                    if (!packageName.isEmpty()) {
                                        packageNames.add(packageName);
                                    }
                                }
                            }
                        }
                    } catch (IOException ioe) {
                        // If nested stream processing fails, continue; scanning outer jar entries still helps
                        // Do not fail the whole scan due to one nested jar issue
                    }
                }
            }
        }
        return new ArrayList<>(packageNames);
    }

    private static List<Class<?>> findClasses(String packageName, List<String> includeInScan) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<Class<?>> classes = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            classes.addAll(GoateScanner.getClasses(resource, packageName));
        }
        return classes;
    }

    private static void checkForGoateAnnotations(List<Class<?>> classes) {
        classes.forEach(klass -> {
            List<Annotation> annotations = Arrays.asList(klass.getAnnotations());
            annotations.stream().filter(annotation -> GoateScanner.annotationsForScanning.contains(annotation.annotationType()))
                    .forEach(annotation -> {
                        String name = annotation.annotationType().getName();
                        List<Class<?>> annotationClasses = new ArrayList<>();
                        if (annotationCache.containsKey(name)) {
                            annotationClasses = annotationCache.get(name);
                        } else {
                            annotationCache.put(name, annotationClasses);
                        }
                        annotationClasses.add(klass);
                    });
        });
    }
}
