package net.dirtcraft.update.bootstrapper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Main {
    public static void main(String[] args) throws Throwable{
        System.out.println("Update bootstrap initializing...");
        String jvm = args[0];
        File jar = new File(args[1]);
        File temp = new File(args[1] + ".temp");
        URL url = new URL(args[2]);
        args = removeFirstEntries(args, 3);

        copyUrlToFile(url, temp);
        replaceSingleFile(temp, jar);
        launch(jvm, jar, args);
    }

    public static void replaceSingleFile(File src, File dest){
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

    private static void copyUrlToFile(URL url, File file) throws Throwable{
        System.out.println("Downloading new update, Please wait...");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        FileUtils.copyInputStreamToFile(con.getInputStream(), file);
    }

    private static String[] removeFirstEntries(String[] args, int i){
        int length = args.length - i;
        String[] reduced = new String[length];
        System.arraycopy(args, i, reduced, 0, length);
        return reduced;
    }
}
