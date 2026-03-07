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
    private static final List<String> goateJarIdentifiers = List.of("kernel", "goate", "barn", "gradle", "json", "mock",
            "performance", "rest", "restassured", "restassured-utils", "retrofit", "retrofit-utils", "selenium",
            "spreadsheets", "spring", "testng", "xml");
    public static Map<String, List<Class<?>>> getAnnotations() {
        synchronized (processing) {
            processing.set(true);
            if (annotationCache.isEmpty()) {
                scanClasses();
            }
            processing.set(false);
        }
        log("Annotation cache populated with " + annotationCache.size() + " annotation types.");
        return annotationCache;
    }

    private static void log(String message) {
        if (dna.annotations != null && dna.annotations.debug) {
            System.out.println("[AnnotationScanner] " + message);
        }
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
            log("Classpath entries to scan: " + Arrays.toString(classpathEntries));
            log("Packages to include in scan: " + includeInScan);
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
                log("Scanning package '" + p + "' for classes...");
                List<Class<?>> currentKlasses = findClasses(p, includeInScan);
                klasses.addAll(currentKlasses);
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        log("Total classes found in scanned packages: " + klasses.size());
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

    private static boolean scanJar(String aPackage) {
        String aJar = aPackage;
        if (goateJarIdentifiers.stream().anyMatch(aJar::contains)) {
            return true;
        }
        List<String> includeInScan = dna.annotations != null ? dna.annotations.listOfJarsToIncludeInTheScan() : null;
        if (includeInScan != null && !includeInScan.isEmpty()) {
            return includeInScan.stream().anyMatch(aJar::contains);
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
        log("Scanned directory '" + directory.getAbsolutePath() + "' and found packages: " + packageNames);
        List<String> stripNestedPackages = dna.annotations != null ? dna.annotations.listOfPackagesToStripFromNestedJars() : List.of("BOOT-INF.classes.");
        return packageNames.stream().map(packageNameC -> {
            if (stripNestedPackages != null) {
                for (String strip : stripNestedPackages) {
                    if (packageNameC.startsWith(strip)) {
                        packageNameC = packageNameC.substring(strip.length());
                    }
                }
            }
            return packageNameC;
        }).toList();
    }

    private static List<String> scanJarFile(File file) throws IOException, ClassNotFoundException {
        // Use a set to deduplicate package names discovered in the jar and any nested jars
        Set<String> packageNames = new HashSet<>();
        List<String> stripNestedPackages = dna.annotations != null ? dna.annotations.listOfPackagesToStripFromNestedJars() : List.of("BOOT-INF.classes.");
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
                    if (scanJar(name)) {
                        log("Found nested jar entry '" + name + "' in '" + file.getName() + "'. Attempting to scan it for classes...");
                        try (InputStream nestedIs = jarFile.getInputStream(entry);
                             JarInputStream jis = new JarInputStream(nestedIs)) {
                            JarEntry nEntry;
                            while ((nEntry = jis.getNextJarEntry()) != null) {
                                String nName = nEntry.getName();
                                if (!nEntry.isDirectory() && nName.endsWith(".class")) {
                                    log("Found class entry '" + nName + "' in nested jar '" + name + "'. Adding its package to the list...");
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
                            log("Failed to scan nested jar entry '" + name + "' in '" + file.getName() + "': " + ioe.getMessage());
                        }
                    }
                }
            }
        }
        return packageNames.stream().map(packageName -> {
            if (stripNestedPackages != null) {
                for (String strip : stripNestedPackages) {
                    if (packageName.startsWith(strip)) {
                        packageName = packageName.substring(strip.length());
                    }
                }
            }
            return packageName;
        }).toList();
    }

    private static List<Class<?>> findClasses(String packageName, List<String> includeInScan) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<Class<?>> classes = new ArrayList<>();
        log("Looking for resources for package '" + packageName + "' with path '" + path + "'.");
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            log("Found resource '" + resource + "' for package '" + packageName + "'. Attempting to load classes from it...");
            List<Class<?>> classes1 = GoateScanner.getClasses(resource, packageName);
            log("Loaded " + classes1.size() + " classes from resource '" + resource + "' for package '" + packageName + "'.");
            classes.addAll(classes1);
        }

        // If nothing was found via the classloader resource enumeration, attempt a safe fallback.
        // The fallback derives class names from classpath entries (jars and directories) and asks
        // the existing classloader to load them via Class.forName(..., false, classLoader).
        // This lets custom classloaders (for example Spring Boot's LaunchedURLClassLoader) resolve
        // nested-jar classes using their own logic without constructing custom URLs or classloaders here.
        if (classes.isEmpty()) {
            log("No resources found for package '" + packageName + "' via ClassLoader.getResources(). Falling back to classpath scan...");
            Set<String> seen = new HashSet<>();
            String classpath = System.getProperty("java.class.path");
            String[] classpathEntries = classpath.split(System.getProperty("path.separator"));

            List<String> stripNestedPackages = dna.annotations != null ? dna.annotations.listOfPackagesToStripFromNestedJars() : List.of("BOOT-INF.classes.");

            for (String entry : classpathEntries) {
                File file = new File(entry);
                if (file.isDirectory()) {
                    // look under the directory for the package path
                    File packageDir = new File(file, path);
                    if (packageDir.exists() && packageDir.isDirectory()) {
                        // recursive collection of .class files
                        List<File> classFiles = new ArrayList<>();
                        collectClassFiles(packageDir, classFiles);
                        for (File cf : classFiles) {
                            try {
                                String rel = cf.getAbsolutePath().substring(file.getAbsolutePath().length() + 1);
                                rel = rel.replace(File.separatorChar, '/');
                                String dotted = rel.replace('/', '.');
                                if (dotted.endsWith(".class")) {
                                    String fqcn = dotted.substring(0, dotted.length() - 6);
                                    // normalize any BOOT-INF.classes. prefix if present
                                    for (String strip : stripNestedPackages) {
                                        if (fqcn.startsWith(strip)) {
                                            fqcn = fqcn.substring(strip.length());
                                        }
                                    }
                                    if (!seen.contains(fqcn)) {
                                        String pkg = fqcn.contains(".") ? fqcn.substring(0, fqcn.lastIndexOf('.')) : "";
                                        if (scan(pkg, includeInScan)) {
                                            try {
                                                Class<?> c = Class.forName(fqcn, false, classLoader);
                                                if(!seen.contains(fqcn)) {
                                                    log("not seen: Checking if class '" + fqcn + "' needs to be scanned for annotations...");
                                                    GoateScanner.checkIfNeedToScan(c);
                                                    classes.add(c);
                                                    seen.add(fqcn);
                                                }
                                                log("Loaded class via fallback: " + fqcn);
                                            } catch (ClassNotFoundException | LinkageError e) {
                                                log("Failed to load class '" + fqcn + "' via fallback: " + e.getMessage());
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                log("Error processing class file '" + cf.getAbsolutePath() + "' : " + e.getMessage());
                            }
                        }
                    }
                } else if (file.getName().endsWith(".jar")) {
                    // open jar and look for entries matching the package path, including nested jars
                    try (JarFile jarFile = new JarFile(file)) {
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry je = entries.nextElement();
                            String name = je.getName();
                            if (!je.isDirectory() && name.endsWith(".class")) {
                                // convert to dotted form and strip any nested prefixes
                                String dotted = name.replace('/', '.');
                                if (dotted.endsWith(".class")) {
                                    String fqcn = dotted.substring(0, dotted.length() - 6);
                                    for (String strip : stripNestedPackages) {
                                        if (fqcn.startsWith(strip)) {
                                            fqcn = fqcn.substring(strip.length());
                                        }
                                    }
                                    if (fqcn.startsWith(packageName + ".") || fqcn.equals(packageName)) {
                                        if (!seen.contains(fqcn)) {
                                            String pkg = fqcn.contains(".") ? fqcn.substring(0, fqcn.lastIndexOf('.')) : "";
                                            if (scan(pkg, includeInScan)) {
                                                try {
                                                    Class<?> c = Class.forName(fqcn, false, classLoader);
                                                    if(!seen.contains(fqcn)) {
                                                        log("not seen: Checking if class '" + fqcn + "' needs to be scanned for annotations...");
                                                        GoateScanner.checkIfNeedToScan(c);
                                                        classes.add(c);
                                                        seen.add(fqcn);
                                                    }
                                                    log("Loaded class via fallback from jar: " + fqcn);
                                                } catch (ClassNotFoundException | LinkageError e) {
                                                    log("Failed to load class '" + fqcn + "' via fallback from jar: " + e.getMessage());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            // if this is a nested jar entry (e.g., BOOT-INF/lib/*.jar) attempt to stream it and inspect
                            if (!je.isDirectory() && name.endsWith(".jar")) {
                                if (scanJar(name)) {
                                    log("Found nested jar entry '" + name + "' in '" + file.getName() + "'. Attempting to scan nested jar contents...");
                                    try (InputStream nestedIs = jarFile.getInputStream(je);
                                         JarInputStream jis = new JarInputStream(nestedIs)) {
                                        JarEntry nEntry;
                                        while ((nEntry = jis.getNextJarEntry()) != null) {
                                            String nName = nEntry.getName();
                                            if (!nEntry.isDirectory() && nName.endsWith(".class")) {
                                                String dotted = nName.replace('/', '.');
                                                if (dotted.endsWith(".class")) {
                                                    String fqcn = dotted.substring(0, dotted.length() - 6);
                                                    for (String strip : stripNestedPackages) {
                                                        if (fqcn.startsWith(strip)) {
                                                            fqcn = fqcn.substring(strip.length());
                                                        }
                                                    }
                                                    if (fqcn.startsWith(packageName + ".") || fqcn.equals(packageName)) {
                                                        if (!seen.contains(fqcn)) {
                                                            String pkg = fqcn.contains(".") ? fqcn.substring(0, fqcn.lastIndexOf('.')) : "";
                                                            if (scan(pkg, includeInScan)) {
                                                                try {
                                                                    Class<?> c = Class.forName(fqcn, false, classLoader);
                                                                    if(!seen.contains(fqcn)) {
                                                                        log("not seen: Checking if class '" + fqcn + "' needs to be scanned for annotations...");
                                                                        GoateScanner.checkIfNeedToScan(c);
                                                                        classes.add(c);
                                                                        seen.add(fqcn);
                                                                    }
                                                                    log("Loaded class via fallback from nested jar: " + fqcn);
                                                                } catch (ClassNotFoundException | LinkageError e) {
                                                                    log("Failed to load class '" + fqcn + "' via fallback from nested jar: " + e.getMessage());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (IOException ioe) {
                                        log("Failed to scan nested jar entry '" + name + "' in '" + file.getName() + "': " + ioe.getMessage());
                                    }
                                }
                            }
                        }
                    } catch (IOException ioe) {
                        log("Failed to open jar '" + file.getAbsolutePath() + "' during fallback scan: " + ioe.getMessage());
                    }
                }
            }
        }
        return classes;
    }

    private static void collectClassFiles(File dir, List<File> classFiles) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                collectClassFiles(f, classFiles);
            } else if (f.getName().endsWith(".class")) {
                classFiles.add(f);
            }
        }
    }

    private static void checkForGoateAnnotations(List<Class<?>> classes) {
        Set<Class<?>> uniqueClasses = new HashSet<>(classes);
        log("Checking " + uniqueClasses.size() + " unique classes for Goate annotations..." + classes.size());
        uniqueClasses.forEach(klass -> {
            List<Annotation> annotations = Arrays.asList(klass.getAnnotations());
            log("Checking class " + klass.getName() + " with annotations: " + annotations);
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
