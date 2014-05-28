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
    public void transform(ZipEntryWrapper inputZipEntryWrapper,
                                     ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry inputZipEntry = inputZipEntryWrapper.zipEntry;
        if (!skipEntry(inputZipEntryWrapper.zipEntry)) {
            ZipEntry outputZipEntry = new ZipEntry(inputZipEntry.getName());
            InputStream inputStream = inputZipEntryWrapper.getInputStream(inputZipEntry);
            zipOutputStream.putNextEntry(outputZipEntry);
            Reader reader = new CustomizedBufferedReader(new InputStreamReader(inputStream));
            Writer writer = new BufferedWriter(new OutputStreamWriter(zipOutputStream));
            if (skipEntryTransformation(inputZipEntry)) {
                IOUtils.copy(reader, writer);
            } else {
                contentTransformer.transform(reader, writer);
            }
            writer.flush();
            zipOutputStream.closeEntry();
        }
    }
}
