package net.dirtcraft.update.bootstrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Update bootstrap initializing...");
        String jvm = args[0];
        File jar = new File(args[1]);
        File temp = new File(args[2]);
        while (!jar.delete()) Thread.sleep(50);
        System.out.println("Copying Files...");
        temp.renameTo(jar);
        args = slice(args, 3, -1);
        launch(jvm, jar, args);
    }

    private static void launch(String jvm, File jar, String[] addArgs) throws Exception{
        System.out.println("Launching!");
        List<String> args = Arrays.asList(jvm, "-jar", jar.toString(), "-postUpdate");
        args = new ArrayList<>(args);
        args.addAll(Arrays.asList(addArgs));
        new ProcessBuilder(args)
                .inheritIO()
                .start();
    }

    private static String[] slice(String[] args, int start, int end){
        int length = args.length - start;
        String[] reduced = new String[length];
        System.arraycopy(args, start, reduced, 0, end == -1? length : end);
        return reduced;
    }
}
