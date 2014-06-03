import frankbe.jtrafo.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by frankber on 28.05.14.
 */
@RunWith(JUnit4.class)
public class SimpleTest {

    private Path getInputPath(String fileName) throws URISyntaxException {
        return Paths.get(SimpleTest.class.getResource("/" + fileName).toURI());
    }

    private Path getOutputPath(String fileName) throws URISyntaxException {
        return Paths.get("./target/" + fileName);
    }

    @Test
    public void sample1() throws IOException, URISyntaxException {
        Path inputPath = getInputPath("sample1-templ.docx");
        Path outputPath = getOutputPath("sample1.docx");
        //Path outputPath = Paths.get("/home/bert/projects/_external/jTrafo/target/sample1.zip");
        Map scope = new HashMap<String, String>();
        scope.put("animal", "duck");
        scope.put("food", "worm");
        IOTransformer trafo = DocumentTransformers.newMustacheDocxTransformer(scope);
        trafo.transform(inputPath, outputPath);
        assertTrue(outputPath.toFile().exists());
        //mock transformer to check the text of the output file
        DocumentTransformers.newDocxTransformer(new IOTransformer<Reader, Writer>() {
            @Override public void transform(Reader input, Writer output) throws IOException {
                String text = IOUtils.readAll(input);
                assertTrue(text.contains("duck") && text.contains("worm"));
            }
        }).transform(outputPath, Files.createTempFile("jTrafo_", null));
    }


    @Test
    public void sample0() throws IOException, URISyntaxException {
        // no transformation, just like copy...
        Path inputPath = getInputPath("sample0-templ.odt");
        Path outputPath = getOutputPath("sample0.odt");
        IOTransformer trafo = DocumentTransformers.newMustacheDocxTransformer(new Object());
        trafo.transform(inputPath, outputPath);
        assertTrue(outputPath.toFile().exists());
    }


    @Test
    public void testCreateZip() throws Exception {
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        // locate file system by using the syntax
        // defined in java.net.JarURLConnection
        URI uri = URI.create("jar:file:/tmp/test.zip");

        try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {
            Path externalTxtFile = Paths.get("/home/bert/work/test.c");
            Path pathInZipfile = zipfs.getPath("/test.c");
            // copy a file into the zip file
            Files.copy(externalTxtFile, pathInZipfile,
                    StandardCopyOption.REPLACE_EXISTING);

        }
    }

}
