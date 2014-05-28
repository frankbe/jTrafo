import frankbe.jtrafo.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by frankber on 28.05.14.
 */
@RunWith(JUnit4.class)
public class SimpleTest {

    @Test
    public void oneAndOnly() throws IOException, URISyntaxException {
        Path inputPath = Paths.get(SimpleTest.class.getResource("/sample1-templ.docx").toURI());
        Path outputPath = Paths.get("./target/sample1.docx");
        Map<String, String> scope= new HashMap();
        scope.put("animal", "duck");
        scope.put("food", "worm");
        ZipFileTransformer trafo = DocumentTransformers.newMustacheDocxTransformer(scope);
        trafo.transform(inputPath.toFile(), outputPath.toFile());
        assertTrue(outputPath.toFile().exists());
        //mock transformer to check the text of the output file
        DocumentTransformers.newDocxTransformer(new IOTransformer<Reader, Writer>() {
            @Override public void transform(Reader source, Writer target) throws IOException {
                String text = IOUtils.readAll(source);
                assertTrue(text.contains("duck") && text.contains("worm"));
            }
        }).transform(outputPath.toFile(), Files.createTempFile("jTrafo_", null).toFile());
    }

}
