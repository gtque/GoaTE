package com.thegoate.annotations;

import com.thegoate.utils.GoateUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
//        Map<String, List<Class>> annotations = new HashMap<>();
        List<String> packageNamesFinal = new ArrayList<>();
        final List<String> includeInScan = new ArrayList<>();
        try {
            Enumeration<URL> resources = ClassLoader.getSystemResources("");
            List<String> packageNames = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File file = new File(resource.getPath());

                if (file.isDirectory()) {
                    packageNames.addAll(scanDirectory(file, ""));
                } else if (file.getName().endsWith(".jar")) {
                    packageNames.addAll(scanJarFile(file));
                }
            }
            resources = AnnotationScanner.class.getClassLoader().getResources("");
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File file = new File(resource.getPath());

                if (file.isDirectory()) {
                    packageNames.addAll(scanDirectory(file, ""));
                } else if (file.getName().endsWith(".jar")) {
                    packageNames.addAll(scanJarFile(file));
                }
            }
            if(dna.annotations != null) {
                includeInScan.addAll(dna.annotations.listOfPackagesToIncludeInTheScan());
            }
            Package[] packages = ClassLoader.getSystemClassLoader().getDefinedPackages();
            Arrays.stream(packages)
                    .filter(aPackage -> scan(aPackage.getName(), includeInScan))
                    .forEach(aPackage -> packageNames.add(aPackage.getName()));
            packageNamesFinal = packageNames.stream()
                    .distinct().toList();
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
        if (!annotationCache.isEmpty()) {
            try {
                Class.forName(AnnotationFactory.class.getName(), true, ClassLoader.getSystemClassLoader());
                Class.forName(GoateUtils.class.getName(), true, ClassLoader.getSystemClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            klasses.stream().distinct().toList().forEach(klass -> {
                try {
                    Class.forName(klass.getName(), true, ClassLoader.getSystemClassLoader());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static boolean scan(String aPackage, List<String> includeInScan) {
        if(aPackage.startsWith("com.thegoate")) {
            return true;
        }
        if(includeInScan!=null&& !includeInScan.isEmpty()){
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
        List<String> packageNames = new ArrayList<>();
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory() && !entry.getName().endsWith("META-INF/")) {
                    String packageName = entry.getName().replace("/", ".").replaceAll("\\.$", "");
                    if (!packageName.isEmpty()) {
                        boolean packageFound = false;
                        Enumeration<JarEntry> innerEntries = jarFile.entries();
                        while (innerEntries.hasMoreElements()) {
                            JarEntry innerEntry = innerEntries.nextElement();
                            if (innerEntry.getName().startsWith(entry.getName()) && innerEntry.getName().endsWith(".class")) {
                                packageFound = true;
                                break;
                            }
                        }
                        if (packageFound) {
                            packageNames.add(packageName);
                        }

                    }
                }
            }
        }
        return packageNames;
    }

    private static List<Class<?>> findClasses(String packageName, List<String> includeInScan) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<Class<?>> classes = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if(scan(packageName, includeInScan)) {
                classes.addAll(GoateScanner.getClasses(resource, packageName));
            }
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
