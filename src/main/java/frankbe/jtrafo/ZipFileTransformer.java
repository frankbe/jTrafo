package frankbe.jtrafo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by frankber on 28.05.14.
 */
public class ZipFileTransformer implements IOTransformer<Path, Path> {

    private final IOTransformer<ZipEntryWrapper, ZipOutputStream> contentTransformer;

    public ZipFileTransformer(IOTransformer<ZipEntryWrapper, ZipOutputStream> contentTransformer) {
        this.contentTransformer = contentTransformer;
    }

    @Override
    public void transform(Path input, Path output) throws IOException {
        if (!Files.exists(input)) throw new IllegalArgumentException("[source: " + input + "] does not exist");
        if (Files.isDirectory(input)) throw new IllegalArgumentException("[source: " + input + "] is a directory");
        try (ZipFile zipInputFile = new ZipFile(input.toFile());
             ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(output.toFile()))) {
            for (ZipEntry entry: Collections.list(zipInputFile.entries())) {
                contentTransformer.transform(new ZipEntryWrapper(zipInputFile, entry), zipOutputStream);
            }
            zipOutputStream.finish();
       }
    }

}
