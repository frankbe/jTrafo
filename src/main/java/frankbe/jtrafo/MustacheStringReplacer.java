package frankbe.jtrafo;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by frankber on 28.05.14.
 */
public class MustacheStringReplacer implements IOTransformer<Reader, Writer> {

    private final MustacheFactory mustacheFactory;
    private final Object scopeObject;

    public MustacheStringReplacer(Object scopeObject) {
        this(scopeObject, new DefaultMustacheFactory());
    }

    public MustacheStringReplacer(Object scopeObject, MustacheFactory mustacheFactory) {
        this.scopeObject = scopeObject;
        this.mustacheFactory = mustacheFactory;
    }

    @Override
    public void transform(Reader input, Writer output) throws IOException {
        Mustache mustache = mustacheFactory.compile(input, "main");  //TODO check - why 'main'?
        mustache.execute(output, scopeObject);
    }
}
