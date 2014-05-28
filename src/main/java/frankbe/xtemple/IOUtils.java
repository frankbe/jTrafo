package frankbe.xtemple;

import java.io.*;

/**
 * Created by frankber on 28.05.14.
 */
public class IOUtils {

    public static int EOF = -1;

    public static Integer copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count;
        byte[] buffer = new byte[8192];
        while ((count = inputStream.read(buffer)) > 0)
            outputStream.write(buffer, 0, count);
        return count;
    }

    public static Integer copy(Reader reader, Writer writer) throws IOException {
        int count;
        char[] buffer = new char[8192];
        while ((count = reader.read(buffer)) > 0)
            writer.write(buffer, 0, count);
        return count;
    }


    public static String readAll(Reader reader) throws IOException {
        int chr;
        StringBuilder sb = new StringBuilder();
        while ((chr = reader.read()) != EOF)
            sb.append((char)chr);
        return sb.toString();
    }

}

