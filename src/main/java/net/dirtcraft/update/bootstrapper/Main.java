package net.dirtcraft.update.bootstrapper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Main {
    public static void main(String[] args) throws Throwable{
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
        dest.delete();
        src.renameTo(dest);
    }

    private static void launch(String jvm, File jar, String[] args) throws Throwable{
        String launchCode =  jvm + " -jar " + jar + " -postUpdate " + Arrays.stream(args).map(s-> "\"" + s + "\"").collect(Collectors.joining(" "));
        Runtime.getRuntime().exec(launchCode);
    }

    private static void copyUrlToFile(URL url, File file) throws Throwable{
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
