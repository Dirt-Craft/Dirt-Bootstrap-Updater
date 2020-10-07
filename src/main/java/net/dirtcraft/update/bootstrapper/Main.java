package net.dirtcraft.update.bootstrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Main {
    public static void main(String[] args) throws Throwable{
        System.out.println("Update bootstrap initializing...");
        String jvm = args[0];
        File jar = new File(args[1]);
        File temp = new File(args[2]);
        args = removeFirstEntries(args, 3);

        replaceSingleFile(jar, temp);
        launch(jvm, jar, args);
    }

    public static void replaceSingleFile(File dest, File src){
        System.out.println("Copying data...");
        dest.delete();
        src.renameTo(dest);
    }

    private static void launch(String jvm, File jar, String[] addArgs) throws Throwable{
        System.out.println("Launching!");
        List<String> args = Arrays.asList(jvm, "-jar", jar.toString(), "-postUpdate");
        args = new ArrayList<>(args);
        args.addAll(Arrays.asList(addArgs));
        new ProcessBuilder(args)
                .inheritIO()
                .start();
    }

    private static String[] removeFirstEntries(String[] args, int i){
        int length = args.length - i;
        String[] reduced = new String[length];
        System.arraycopy(args, i, reduced, 0, length);
        return reduced;
    }
}
