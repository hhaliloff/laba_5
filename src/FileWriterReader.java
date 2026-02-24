import java.io.*;

public class FileWriterReader {
    public static void write(String data, String path) throws IOException {
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(data.getBytes());
        bos.flush();
        bos.close();
    }

    public static String read (String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        int i;
        String data = "";
        while ((i = bis.read()) != -1) {
            data = data + (char) i;
        }
        return data;
    }
}
