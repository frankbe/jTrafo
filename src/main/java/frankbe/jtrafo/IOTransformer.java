package frankbe.jtrafo;

import java.io.IOException;

/**
 * Created by frankber on 28.05.14.
 */
public interface IOTransformer<I, O> {
    public void transform(I input, O output) throws IOException;
}
