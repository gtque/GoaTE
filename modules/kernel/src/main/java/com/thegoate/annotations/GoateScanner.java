package com.thegoate.annotations;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.thegoate.DNA.dna;

public class GoateScanner {

    public static final List<String> mappedClasses = new ArrayList<>();
    public static final List<Class<?>> annotationsForScanning = new ArrayList<>();
    private static final StringBuilder errors = new StringBuilder();
    private static final AtomicBoolean errorsRetrieved = new AtomicBoolean(false);
    //this is here for testing purposes.
    private static final AtomicBoolean notFailed = new AtomicBoolean(false);

    public static String getErrors() {
        synchronized (errorsRetrieved) {
            if (!errorsRetrieved.get()) {
                errorsRetrieved.set(true);
                return errors.toString();
            }
            return null;
        }
    }

    private static void log(String message) {
        if (dna.annotations != null && dna.annotations.debug) {
            System.out.println("[GoateScanner] " + message);
        }
    }

    public static List<Class<?>> getClasses(URL url, String thePackage) {
        // Use the thread context class loader so that custom loaders (e.g. Spring Boot) can resolve classes.
        log("Scanning URL: " + url + " for package: " + thePackage);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            URI uri = url.toURI();
            if (uri.getScheme().equals("jar")) {
                FileSystem fileSystem = null;
                try {
                    try {
                        fileSystem = FileSystems.getFileSystem(uri);
                    } catch (FileSystemNotFoundException fsnfe) {
                        fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                    }
                    String resourcePath = url.getPath().substring(url.getPath().lastIndexOf("!") + 2);
                    return findClassesInDirectory(fileSystem.getPath(resourcePath), thePackage, classLoader);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (fileSystem != null) {
                        fileSystem.close();
                    }
                }
            } else {
                return findClassesInDirectory(Paths.get(uri), thePackage, classLoader);
            }
        } catch (URISyntaxException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Class<?>> findClassesInDirectory(Path directory, String packageName, ClassLoader classLoader) throws ClassNotFoundException, IOException {
        log("Scanning directory: " + directory + " for package: " + packageName);
        List<Class<?>> classes = new ArrayList<>();
        if (directory.getName(directory.getNameCount() - 1).toString().endsWith(".class")) {
            String className = packageName + '.' + directory.getName(directory.getNameCount() - 1).toString().substring(0, directory.getName(directory.getNameCount() - 1).toString().length() - 6);
            classes.add(Class.forName(className, false, classLoader));
        } else {
            try (Stream<Path> walk = Files.walk(directory, 1028)) {
                for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
                    Path currentPath = it.next();
                    if (Files.isDirectory(currentPath) && !currentPath.equals(directory)) {
                        String packaging = packaging(directory, currentPath, 1);
                        classes.addAll(findClassesInDirectory(currentPath, packageName + packaging, classLoader));
                    } else if (currentPath.getName(currentPath.getNameCount() - 1).toString().endsWith(".class")) {
                        String packaging = packaging(directory, currentPath, 2);
                        String className = packageName + packaging + '.' + currentPath.getName(currentPath.getNameCount() - 1).toString().substring(0, currentPath.getName(currentPath.getNameCount() - 1).toString().length() - 6);
                        try {
                            if (!mappedClasses.contains(className)) {
                                mappedClasses.add(className);
                                Class<?> klass = Class.forName(className, false, classLoader);
                                if (klass.isAnnotation()) {
                                    Scan annotation = klass.getAnnotation(Scan.class);
                                    log("Found annotation: " + klass.getName() + " marked for scan: " + (annotation != null));
                                    if (annotation != null) {
                                        annotationsForScanning.add(klass);
                                    }
                                }
                                classes.add(klass);
                                if (notFailed.get()) {
                                    notFailed.set(false);
                                    try {
                                        Class.forName("com.thegoate.asdf4tyv.IDoNotExist", false, classLoader);
                                    } catch (ClassNotFoundException ignored) {
                                        // expected test hook; ignore
                                    }
                                }
                            }
                        } catch (ExceptionInInitializerError | ClassNotFoundException e) {
                            if (!errors.isEmpty()) {
                                errors.append("\n");
                            }
                            errors.append(e.getClass().getName()).append(":").append(e.getMessage());
                        }
                    }
                }
            }
        }
        return classes;
    }

    public static void checkIfNeedToScan(Class<?> klass) {
        if (klass.isAnnotation()) {
            Scan annotation = klass.getAnnotation(Scan.class);
            log("Found annotation: " + klass.getName() + " marked for scan: " + (annotation != null));
            if (annotation != null) {
                annotationsForScanning.add(klass);
            }
        }
    }

    private static String packaging(Path directory, Path currentPath, int packageOffset) {
//        int packageOffset = 2;
        int nameCount = directory.getNameCount();
        int currentNameCount = currentPath.getNameCount();
        String packaging = "";
        while (packageOffset < currentNameCount && !directory.getName(nameCount - 1).toString().equals(currentPath.getName(currentNameCount - packageOffset).toString())) {
            packaging = new StringBuilder(".").append(currentPath.getName(currentNameCount - packageOffset)).append(packaging).toString();
            packageOffset++;
        }
        return packaging;
    }
}
