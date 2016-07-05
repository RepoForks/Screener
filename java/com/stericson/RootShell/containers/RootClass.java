package com.stericson.RootShell.containers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.zhanghai.android.materialprogressbar.BuildConfig;
import me.zhanghai.android.materialprogressbar.R;

public class RootClass {
    static String PATH_TO_DX = "/Users/Chris/Projects/android-sdk-macosx/build-tools/18.0.1/dx";

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$stericson$RootShell$containers$RootClass$READ_STATE = new int[READ_STATE.values().length];

        static {
            try {
                $SwitchMap$com$stericson$RootShell$containers$RootClass$READ_STATE[READ_STATE.STARTING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$stericson$RootShell$containers$RootClass$READ_STATE[READ_STATE.FOUND_ANNOTATION.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public static class AnnotationsFinder {
        private final String AVOIDDIRPATH = ("stericson" + File.separator + "RootShell" + File.separator);
        private List<File> classFiles;

        public AnnotationsFinder() throws IOException {
            System.out.println("Discovering root class annotations...");
            this.classFiles = new ArrayList();
            lookup(new File("src"), this.classFiles);
            System.out.println("Done discovering annotations. Building jar file.");
            File builtPath = getBuiltPath();
            if (builtPath != null) {
                String[] cmd;
                String rc1 = "com" + File.separator + "stericson" + File.separator + "RootShell" + File.separator + "containers" + File.separator + "RootClass.class";
                String rc2 = "com" + File.separator + "stericson" + File.separator + "RootShell" + File.separator + "containers" + File.separator + "RootClass$RootArgs.class";
                String rc3 = "com" + File.separator + "stericson" + File.separator + "RootShell" + File.separator + "containers" + File.separator + "RootClass$AnnotationsFinder.class";
                String rc4 = "com" + File.separator + "stericson" + File.separator + "RootShell" + File.separator + "containers" + File.separator + "RootClass$AnnotationsFinder$1.class";
                String rc5 = "com" + File.separator + "stericson" + File.separator + "RootShell" + File.separator + "containers" + File.separator + "RootClass$AnnotationsFinder$2.class";
                boolean onWindows = -1 != System.getProperty("os.name").toLowerCase().indexOf("win");
                if (onWindows) {
                    StringBuilder sb = new StringBuilder(" " + rc1 + " " + rc2 + " " + rc3 + " " + rc4 + " " + rc5);
                    for (File file : this.classFiles) {
                        sb.append(" " + file.getPath());
                    }
                    cmd = new String[]{"cmd", "/C", "jar cvf anbuild.jar" + sb.toString()};
                } else {
                    ArrayList<String> al = new ArrayList();
                    al.add("jar");
                    al.add("cf");
                    al.add("anbuild.jar");
                    al.add(rc1);
                    al.add(rc2);
                    al.add(rc3);
                    al.add(rc4);
                    al.add(rc5);
                    for (File file2 : this.classFiles) {
                        al.add(file2.getPath());
                    }
                    cmd = (String[]) al.toArray(new String[al.size()]);
                }
                ProcessBuilder jarBuilder = new ProcessBuilder(cmd);
                jarBuilder.directory(builtPath);
                try {
                    jarBuilder.start().waitFor();
                } catch (IOException e) {
                } catch (InterruptedException e2) {
                }
                File rawFolder = new File("res/raw");
                if (!rawFolder.exists()) {
                    rawFolder.mkdirs();
                }
                System.out.println("Done building jar file. Creating dex file.");
                try {
                    new ProcessBuilder(onWindows ? new String[]{"cmd", "/C", "dx --dex --output=res/raw/anbuild.dex " + builtPath + File.separator + "anbuild.jar"} : new String[]{getPathToDx(), "--dex", "--output=res/raw/anbuild.dex", builtPath + File.separator + "anbuild.jar"}).start().waitFor();
                } catch (IOException e3) {
                } catch (InterruptedException e4) {
                }
            }
            System.out.println("All done. ::: anbuild.dex should now be in your project's res/raw/ folder :::");
        }

        protected void lookup(File path, List<File> fileList) {
            String desourcedPath = path.toString().replace("src/", BuildConfig.FLAVOR);
            for (File file : path.listFiles()) {
                if (file.isDirectory()) {
                    if (-1 == file.getAbsolutePath().indexOf(this.AVOIDDIRPATH)) {
                        lookup(file, fileList);
                    }
                } else if (file.getName().endsWith(".java") && hasClassAnnotation(file)) {
                    final String fileNamePrefix = file.getName().replace(".java", BuildConfig.FLAVOR);
                    for (File matchingFile : new File(getBuiltPath().toString() + File.separator + desourcedPath).listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String filename) {
                            return filename.startsWith(fileNamePrefix);
                        }
                    })) {
                        fileList.add(new File(desourcedPath + File.separator + matchingFile.getName()));
                    }
                }
            }
        }

        protected boolean hasClassAnnotation(File file) {
            READ_STATE readState = READ_STATE.STARTING;
            Pattern p = Pattern.compile(" class ([A-Za-z0-9_]+)");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while (true) {
                    String line = reader.readLine();
                    if (line != null) {
                        switch (AnonymousClass1.$SwitchMap$com$stericson$RootShell$containers$RootClass$READ_STATE[readState.ordinal()]) {
                            case R.styleable.View_android_focusable /*1*/:
                                if (-1 >= line.indexOf("@RootClass.Candidate")) {
                                    break;
                                }
                                readState = READ_STATE.FOUND_ANNOTATION;
                                break;
                            case R.styleable.View_paddingStart /*2*/:
                                Matcher m = p.matcher(line);
                                if (!m.find()) {
                                    System.err.println("Error: unmatched annotation in " + file.getAbsolutePath());
                                    readState = READ_STATE.STARTING;
                                    break;
                                }
                                System.out.println(" Found annotated class: " + m.group(0));
                                return true;
                            default:
                                break;
                        }
                    }
                    return false;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e2) {
                e2.printStackTrace();
                return false;
            }
        }

        protected String getPathToDx() throws IOException {
            String androidHome = System.getenv("ANDROID_HOME");
            if (androidHome == null) {
                throw new IOException("Error: you need to set $ANDROID_HOME globally");
            }
            String dxPath = null;
            int recentSdkVersion = 0;
            for (File file : new File(androidHome + File.separator + "build-tools").listFiles()) {
                String fileName;
                if (file.getName().contains("-")) {
                    String[] splitFileName = file.getName().split("-");
                    if (splitFileName[1].contains("W")) {
                        fileName = String.valueOf(splitFileName[1].toCharArray()[0]);
                    } else {
                        fileName = splitFileName[1];
                    }
                } else {
                    fileName = file.getName();
                }
                String[] sdkVersionBits = fileName.split("[.]");
                int sdkVersion = Integer.parseInt(sdkVersionBits[0]) * 10000;
                if (sdkVersionBits.length > 1) {
                    sdkVersion += Integer.parseInt(sdkVersionBits[1]) * 100;
                    if (sdkVersionBits.length > 2) {
                        sdkVersion += Integer.parseInt(sdkVersionBits[2]);
                    }
                }
                if (sdkVersion > recentSdkVersion) {
                    String tentativePath = file.getAbsolutePath() + File.separator + "dx";
                    if (new File(tentativePath).exists()) {
                        recentSdkVersion = sdkVersion;
                        dxPath = tentativePath;
                    }
                }
            }
            if (dxPath != null) {
                return dxPath;
            }
            throw new IOException("Error: unable to find dx binary in $ANDROID_HOME");
        }

        protected File getBuiltPath() {
            File foundPath = null;
            File ideaPath = new File("out" + File.separator + de.toastcode.screener.BuildConfig.FLAVOR);
            if (ideaPath.isDirectory()) {
                File[] children = ideaPath.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        return pathname.isDirectory();
                    }
                });
                if (children.length > 0) {
                    foundPath = new File(ideaPath.getAbsolutePath() + File.separator + children[0].getName());
                }
            }
            if (foundPath != null) {
                return foundPath;
            }
            File eclipsePath = new File("bin" + File.separator + "classes");
            if (eclipsePath.isDirectory()) {
                return eclipsePath;
            }
            return foundPath;
        }
    }

    public @interface Candidate {
    }

    enum READ_STATE {
        STARTING,
        FOUND_ANNOTATION
    }

    public class RootArgs {
        public String[] args;
    }

    public RootClass(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = args[0];
        RootArgs actualArgs = new RootArgs();
        actualArgs.args = new String[(args.length - 1)];
        System.arraycopy(args, 1, actualArgs.args, 0, args.length - 1);
        Class.forName(className).getConstructor(new Class[]{RootArgs.class}).newInstance(new Object[]{actualArgs});
    }

    static void displayError(Exception e) {
        System.out.println("##ERR##" + e.getMessage() + "##");
        e.printStackTrace();
    }

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                AnnotationsFinder annotationsFinder = new AnnotationsFinder();
            } else {
                RootClass rootClass = new RootClass(args);
            }
        } catch (Exception e) {
            displayError(e);
        }
    }
}
