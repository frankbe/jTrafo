package frankbe.xtemple;

import java.io.IOException;

/**
 * Created by frankber on 28.05.14.
 */
public interface IOTransformer<S, T> {
    public void transform(S source, T target) throws IOException;
}
