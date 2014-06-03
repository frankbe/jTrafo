package frankbe.jtrafo;

import com.sun.nio.zipfs.ZipFileAttributes;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.EnumSet;
import java.util.zip.ZipEntry;

/**
 * Created by bert on 02.06.14.
 */
public class FileTransformer implements IOTransformer<Path, Path> {

    private Charset charset = Charset.defaultCharset(); // TODO change

    class CustomizedBufferedReader extends BufferedReader {
        public CustomizedBufferedReader(Reader in) { super(in);}
        // Workaround: Mustache java closes the stream always while processing, which causes an error,
        // but the Stream will be closed anyway at the end of the zip file, so...
        @Override public void close() throws IOException { }     //TODO check/refactor
    }

    private final IOTransformer<Reader, Writer> contentTransformer;

    public FileTransformer(IOTransformer<Reader, Writer> contentTransformer) {
        this.contentTransformer = contentTransformer;
    }

    @Override
    public void transform(Path input, Path output) throws IOException {
        if (!Files.exists(input)) throw new IllegalArgumentException("[input: " + input + "] does not exist");
        if (Files.isDirectory(input)) throw new IllegalArgumentException("[input: " + input + "] cannot to be a directory");
        if (Files.isDirectory(output)) throw new IllegalArgumentException("[output: " + output + "] cannot to be a directory");
        if (!skipFile(input)) {
            //Files.createFile(output);
            //new CustomizedBufferedReader(new InputStreamReader(inputStream));
            if (skipEntryTransformation(input)) {
                Files.copy(input, output);
            } else try(Reader reader = Files.newBufferedReader(input, charset);
                       Writer writer = Files.newBufferedWriter(output, charset)) {
                contentTransformer.transform(reader, writer);
                writer.flush();
            }
        }
    }

    protected boolean skipEntryTransformation(Path input) { return false; }

    protected boolean skipFile(Path input) { return false; }
}
