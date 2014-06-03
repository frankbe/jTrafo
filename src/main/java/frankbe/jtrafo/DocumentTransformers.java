package frankbe.jtrafo;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

/**
 * Created by frankber on 28.05.14.
 */
public class DocumentTransformers  {

    private static ZipArchiveTransformer newDocumentTransformer(IOTransformer<Reader, Writer> contentTransformer,
                                                              final String filePattern) {
        IOTransformer<Path, Path> fileTransformer = new FileTransformer(contentTransformer) {
            @Override protected boolean skipEntryTransformation(Path path) {
                return !filePattern.matches(path.toString());
            }
        };
        ZipArchiveTransformer archiveTransformer = new ZipArchiveTransformer(fileTransformer);
        return archiveTransformer;
    }

    public static ZipArchiveTransformer newDocxTransformer(IOTransformer<Reader, Writer> contentTransformer) {
        return newDocumentTransformer(contentTransformer, "/word/document.xml");//TODO check
    }

    public static ZipArchiveTransformer newOdtTransformer(IOTransformer<Reader, Writer> contentTransformer) {
        return newDocumentTransformer(contentTransformer, "/content.xml");//TODO check
    }

    public static ZipArchiveTransformer newMustacheDocxTransformer(Object scopeObject) {
        return newDocxTransformer(new MustacheStringReplacer(scopeObject));
    }

    public static ZipArchiveTransformer newMustacheOdtTransformer(Object scopeObject) {
        return newOdtTransformer(new MustacheStringReplacer(scopeObject));
    }

}
