package frankbe.xtemple;

import java.io.Reader;
import java.io.Writer;
import java.util.zip.ZipEntry;

/**
 * Created by frankber on 28.05.14.
 */
public class DocumentTransformers  {

    private static ZipFileTransformer newDocumentTransformer(IOTransformer<Reader, Writer> contentTransformer,
                                                              final String filePattern) {
        return new ZipFileTransformer(new ZipEntryTransformer(contentTransformer) {
            @Override protected boolean skipEntryTransformation(ZipEntry entry) {
                return !filePattern.matches(entry.getName());
            }
        });
    }

    public static ZipFileTransformer newDocxTransformer(IOTransformer<Reader, Writer> contentTransformer) {
        return newDocumentTransformer(contentTransformer, "word/document.xml");//TODO check
    }

    public static ZipFileTransformer newOdtTransformer(IOTransformer<Reader, Writer> contentTransformer) {
        return newDocumentTransformer(contentTransformer, "content.xml");//TODO check
    }

    public static ZipFileTransformer newMustacheDocxTransformer(Object scopeObject) {
        return newDocxTransformer(new MustacheStringReplacer(scopeObject));
    }

    public static ZipFileTransformer newMustacheOdtTransformer(Object scopeObject) {
        return newOdtTransformer(new MustacheStringReplacer(scopeObject));
    }

}
