package frankbe.jtrafo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by frankber on 28.05.14.
 */
public class DirectoryTransformer implements IOTransformer<Path, Path> {

    private final IOTransformer<Path, Path> fileTransformer;

    public DirectoryTransformer(IOTransformer<Path, Path> fileTransformer) {
        this.fileTransformer = fileTransformer;
    }

    @Override
    public void transform(final Path input, final Path output) throws IOException {
        if (!Files.exists(input)) throw new IllegalArgumentException("[input: " + input + "] does not exist");
        if (!Files.isDirectory(input)) throw new IllegalArgumentException("[input: " + input + "] has to be a directory");
        if (!Files.isDirectory(output)) throw new IllegalArgumentException("[output: " + output + "] has to be a directory");
        Files.walkFileTree(input, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path newOutputFile = output.resolve(input.relativize(file));
                fileTransformer.transform(file, newOutputFile);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(output.resolve(input.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
