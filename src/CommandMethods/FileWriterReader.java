/**
 * Класс для чтения и записи текста в файл.
 */
package CommandMethods;

import java.io.*;

public class FileWriterReader {
    public static void write(String data, String path) throws IOException {
        File file = new File(path);

        if (file.exists() && !file.canWrite()) {
            throw new IOException("Нет доступа на запись к файлу: " + path);
        }

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            bos.write(data.getBytes());
            bos.flush();
        } catch (SecurityException e) {
            throw new IOException("Доступ к файлу запрещён: " + path, e);
        }
    }

    public static String read(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException("Файл не найден: " + path);
        }

        if (!file.canRead()) {
            throw new IOException("Нет доступа на чтение файла: " + path);
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            StringBuilder data = new StringBuilder();
            int i;
            while ((i = bis.read()) != -1) {
                data.append((char) i);
            }
            return data.toString();
        } catch (SecurityException e) {
            throw new IOException("Доступ к файлу запрещён: " + path, e);
        }
    }
}
