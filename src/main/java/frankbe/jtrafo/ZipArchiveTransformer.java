package frankbe.jtrafo;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bert on 02.06.14.
 */
public class ZipArchiveTransformer extends DirectoryTransformer {

    public ZipArchiveTransformer(IOTransformer<Path, Path> fileTransformer) {
        super(fileTransformer);
    }

    @Override
    public void transform(Path input, Path output) throws IOException {
        if (!Files.exists(input)) throw new IllegalArgumentException("[input: " + input + "] does not exist");
        if (Files.exists(output)) Files.delete(output);
        //Files.createFile(output);
        try(FileSystem inputFs = createZipFileSystem(input, false);
            FileSystem outputFs = createZipFileSystem(output, true)) {
            super.transform(inputFs.getPath("/"), outputFs.getPath("/"));
        }
    }

    private static FileSystem createZipFileSystem(Path path, boolean createFile) throws IOException {
        final Map<String, String> env = new HashMap<>();
        if (createFile) { env.put("create", "true"); }
        final URI uri = URI.create("jar:file:" + path.toUri().getPath());
        return FileSystems.newFileSystem(uri, env);
    }
}
