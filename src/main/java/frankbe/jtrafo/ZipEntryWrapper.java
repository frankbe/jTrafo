package frankbe.jtrafo;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by frankber on 28.05.14.
 */
public class ZipEntryWrapper {

    public final ZipFile zipFile;
    public final ZipEntry zipEntry;

    public ZipEntryWrapper(ZipFile zipFile, ZipEntry zipEntry) {
        this.zipFile = zipFile;
        this.zipEntry = zipEntry;
    }

    public InputStream getInputStream(java.util.zip.ZipEntry entry) throws IOException {
        return zipFile.getInputStream(zipEntry);
    }
}
