package FileServer.Converter.TikaConfig;

import org.apache.tika.extractor.EmbeddedResourceHandler;
import org.apache.tika.mime.MediaType;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahul on 1/28/16.
 */
public class CollectingEmbeddedResourceHandler implements EmbeddedResourceHandler {

    public Map<InputStream, String> embeddedFiles;

    public CollectingEmbeddedResourceHandler() {
        this.embeddedFiles = new HashMap<>();
    }

    @Override
    public void handle(String filename, MediaType mediaType, InputStream stream) {
        embeddedFiles.put(stream, mediaType.toString());
    }
}
