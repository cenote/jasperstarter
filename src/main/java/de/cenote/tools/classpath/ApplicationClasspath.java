/*
 * Copyright 2012 Cenote GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cenote.tools.classpath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p>ApplicationClasspath class.</p>
 *
 * @author Volker Voßkämper
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public class ApplicationClasspath {

    /**
     * For calling .getDeclaredMethod("addURL", parameterTypes) An array of
     * Class objects that identify the method's formal parameter types, in
     * declared order.
     */
    private static final Class<?>[] parameterTypes = new Class[]{URL.class};
    /** Constant <code>FIND_FROM_CLASSPATH=2</code> */
    public static final int FIND_FROM_CLASSPATH = 2;
    /** Constant <code>FIND_FROM_THIS=1</code> */
    public static final int FIND_FROM_THIS = 1;
    private static int defaultBasedirMethod = FIND_FROM_THIS;

    /**
     * Adds a filename (absolute path) to the classpath.
     *
     * @param filename name of absolute path to (jar)file or dir
     * @throws java.io.IOException if any.
     */
    public static void add(String filename) throws IOException {
        File file = new File(filename);
        add(file);
    }

    /**
     * Adds a file (absolute path) to the classpath.
     *
     * @param file absolute path to (jar)file or dir
     * @throws java.io.IOException if any.
     */
    public static void add(File file) throws IOException {
        add(file.toURI().toURL());
    }

    /**
     * Adds an URL (absolute path) to the classpath.
     *
     * @param url absolute path to (jar)file or dir as URL
     * @throws java.io.IOException if any.
     */
    public static void add(URL url) throws IOException {
        URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?> urlClassLoaderClass = URLClassLoader.class;
        try {
            Method method = urlClassLoaderClass.getDeclaredMethod("addURL", parameterTypes);
            method.setAccessible(true);
            method.invoke(systemClassLoader, new Object[]{url});
        } catch (Exception e) {
            throw new IOException("Error, could not add URL to system classloader!");
        }
    }

    /**
     * Adds a relative filename to the classpath.
     *
     * @param relativeFilename name of (jar)file or dir relative to
     * getAppBaseDir()
     * @throws java.io.IOException if any.
     * @throws java.net.URISyntaxException if any.
     */
    public static void addRelative(String relativeFilename) throws IOException, URISyntaxException {
        File file = new File(getAppBaseDir(), relativeFilename);
        add(file);
    }

    /**
     * Adds a relative file to the classpath.
     *
     * @param relativeFile (jar)file or dir relative to getAppBaseDir()
     * @throws java.io.IOException if any.
     * @throws java.net.URISyntaxException if any.
     */
    public static void addRelative(File relativeFile) throws IOException, URISyntaxException {
        File file = new File(getAppBaseDir(), relativeFile.getPath());
        add(file);
    }

    /**
     * Adds all jar files in a directory (absolute path) to the classpath.
     *
     * @param dirName absolute path to directory
     * @throws java.io.IOException if any.
     */
    public static void addJars(String dirName) throws IOException {
        addJars(new File(dirName));
    }

    /**
     * Adds all jar files in a directory (absolute path) to the classpath.
     *
     * @param dir absolute path to directory
 if any.
     * @throws java.io.IOException if any.
     * @throws java.io.FileNotFoundException if any.
     */
    public static void addJars(File dir) throws IOException, FileNotFoundException {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.toString().toLowerCase().endsWith(".jar")) {
                    add(file);
                }
            }
        } else {
            throw new FileNotFoundException("Error, " + dir + " is not a directory!");
        }
    }

    /**
     * Adds all jar files in a directory to the classpath.
     *
     * @param dirName path as string to directory relative to getAppBaseDir()
 if any.
     * @throws java.io.IOException if any.
     * @throws java.net.URISyntaxException if any.
     */
    public static void addJarsRelative(String dirName) throws IOException, URISyntaxException {
        addJars(new File(getAppBaseDir(), dirName));
    }

    /**
     * Adds all jar files in a directory to the classpath.
     *
     * @param dir path to directory relative to getAppBaseDir()
 if any.
     * @throws java.io.IOException if any.
     * @throws java.net.URISyntaxException if any.
     */
    public static void addJarsRelative(File dir) throws IOException, URISyntaxException {
        addJars(new File(getAppBaseDir(), dir.getPath()));
    }

    /**
     * Returns the basedir of the application using the default method
     *
     * @see #getAppBaseDirFromThis()
     * @see #getAppBaseDirFromClasspath()
     * @see #getAppBaseDirFromThis()
     * @see #getAppBaseDirFromClasspath()
     * @throws java.net.URISyntaxException if any.
     * @return a {@link java.io.File} object.
     */
    public static File getAppBaseDir() throws URISyntaxException {
        return getAppBaseDir(defaultBasedirMethod);
    }

    /**
     * Returns the basedir of the application using the supplied method constant
     *
     * @see #getAppBaseDirFromThis()
     * @see #getAppBaseDirFromClasspath()
     * @see #getAppBaseDirFromThis()
     * @see #getAppBaseDirFromClasspath()
     * @throws java.net.URISyntaxException if any.
     * @param basedirMethod a int.
     * @return a {@link java.io.File} object.
     */
    public static File getAppBaseDir(int basedirMethod) throws URISyntaxException {
        File returnval = null;
        switch (basedirMethod) {
            case FIND_FROM_THIS:
                returnval = getAppBaseDirFromThis();
                break;
            case FIND_FROM_CLASSPATH:
                returnval = getAppBaseDirFromClasspath();
                break;
        }
        return returnval;
    }

    /**
     * Sets the default method witch is used by getAppBaseDir()
     *
     * @param newDefaultBasedirMethod constant f.e. this.FIND_FROM_THIS
     */
    public static void setDefaultBasedirMethod(int newDefaultBasedirMethod) {
        defaultBasedirMethod = newDefaultBasedirMethod;
    }

    /**
     * Returns the basedir where this class is located. It is the base of the
     * part of classpath for this class or containing directory of the jar file
     * this class is packed in.
     *
     * @throws java.net.URISyntaxException if any.
     * @return a {@link java.io.File} object.
     */
    public static File getAppBaseDirFromThis() throws URISyntaxException {
        URL baseUrl = ApplicationClasspath.class.getProtectionDomain().getCodeSource().getLocation();
        File baseDir;
        if (baseUrl.getAuthority() != null) {
            // workaroud Windows UNC path problems
            URI uri;
            uri = new URI(baseUrl.toURI().toString().replace("file://", "file:/"));
            baseDir = new File(File.separator + (new File(uri)).toString());
        } else {
            baseDir = new File(baseUrl.toURI());
        }
        if (!baseDir.isDirectory()) {
            baseDir = baseDir.getParentFile();
        }
        return baseDir;
    }

    /**
     * Returns the parent of the first part of the applications classpath which
     * is usually the directory containing the main jar file of the application.
     * If your application is not packed in a jar or you start your application
     * from an IDE, this may fail or returns a wrong dir!
     *
     * @return a {@link java.io.File} object.
     * @throws java.net.URISyntaxException if any.
     */
    public static File getAppBaseDirFromClasspath() throws URISyntaxException {
        URL baseUrl = ((URLClassLoader) ApplicationClasspath.class.getClassLoader()).getURLs()[0];
        File baseDir;
        if (baseUrl.getAuthority() != null) {
            // workaroud Windows UNC path problems
            URI uri;
            uri = new URI(baseUrl.toURI().toString().replace("file://", "file:/"));
            baseDir = new File(File.separator + (new File(uri)).toString());
        } else {
            baseDir = new File(baseUrl.toURI());
        }
        if (!baseDir.isDirectory()) {
            baseDir = baseDir.getParentFile();
        }
        return baseDir;
    }
}
