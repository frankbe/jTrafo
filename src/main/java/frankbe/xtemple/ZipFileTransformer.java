package frankbe.xtemple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by frankber on 28.05.14.
 */
public class ZipFileTransformer implements IOTransformer<File, File> {

    private final IOTransformer<ZipEntryWrapper, ZipOutputStream> contentTransformer;

    public ZipFileTransformer(IOTransformer<ZipEntryWrapper, ZipOutputStream> contentTransformer) {
        this.contentTransformer = contentTransformer;
    }

    @Override
    public void transform(File source, File target) throws IOException {
        if (!source.exists()) throw new IllegalArgumentException("[source: " + source + "] does not exist");
        if (!source.isFile()) throw new IllegalArgumentException("[source: " + source + "] is not a file");
        try (ZipFile zipInputFile = new ZipFile(source);
             ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(target))) {
            for (ZipEntry entry: Collections.list(zipInputFile.entries())) {
                contentTransformer.transform(new ZipEntryWrapper(zipInputFile, entry), zipOutputStream);
            }
       }
    }

}
