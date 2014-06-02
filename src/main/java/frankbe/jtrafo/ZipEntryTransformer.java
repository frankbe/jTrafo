package frankbe.jtrafo;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by frankber on 28.05.14.
 */
public class ZipEntryTransformer implements IOTransformer<ZipEntryWrapper, ZipOutputStream> {

    class CustomizedBufferedReader extends BufferedReader {
        public CustomizedBufferedReader(Reader in) { super(in);}
        // Workaround: Mustache java closes the stream always while processing, which causes an error,
        // but the Stream will be closed anyway at the end of the zip file, so...
        @Override public void close() throws IOException { }     //TODO check/refactor
    }

    private final IOTransformer<Reader, Writer> contentTransformer;

    public ZipEntryTransformer(IOTransformer<Reader, Writer> contentTransformer) {
        this.contentTransformer = contentTransformer;
    }

    protected boolean skipEntry(ZipEntry entry) { return false; }
    protected boolean skipEntryTransformation(ZipEntry entry) { return false; }

    @Override
    public void transform(ZipEntryWrapper input,
                                     ZipOutputStream output) throws IOException {
        ZipEntry inputZipEntry = input.zipEntry;
        if (!skipEntry(input.zipEntry)) {
            ZipEntry outputZipEntry = new ZipEntry(inputZipEntry.getName());
            outputZipEntry.setComment(inputZipEntry.getComment());
            outputZipEntry.setExtra(inputZipEntry.getExtra());
            outputZipEntry.setTime(inputZipEntry.getTime());
            InputStream inputStream = input.getInputStream(inputZipEntry);
            output.putNextEntry(outputZipEntry);
            Reader reader = new CustomizedBufferedReader(new InputStreamReader(inputStream));
            Writer writer = new BufferedWriter(new OutputStreamWriter(output));
            if (skipEntryTransformation(inputZipEntry)) {
                IOUtils.copy(inputStream, output);
            } else {
                contentTransformer.transform(reader, writer);
            }
            writer.flush();
            output.closeEntry();
        }
    }
}
