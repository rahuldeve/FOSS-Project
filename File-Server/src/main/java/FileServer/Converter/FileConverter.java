package FileServer.Converter;

import FileServer.DataWrappers.MetadataWrapper;

import java.io.InputStream;
import java.util.List;

/**
 * Created by rahul on 17/12/15.
 *
 * An interface used for creating new converters. Subclasses must implement the function extractContent().
 */
public interface FileConverter {

    /**
     * Parses the MultiPartFile object and extracts its contents as a string. The method then wraps all the necessary
     * in a FileMetadataWrapper object and return it.
     *
     * @param stream        The InputStream of the MultiPartFile received from the request
     * @param filename      The name of the file
     * @param mimeType      The MIME type of the file
     * @param tags          The list of tags the file has been associated with
     * @param from          Name of the sender of the file
     * @param to            Name of the recipient of the file
     * @return              A MetadataWrapper object containing details of the processed file
     */
    public List<MetadataWrapper> extractContent(InputStream stream, String filename, String mimeType, List<String> tags, String from, String to, boolean recursive);

    public String getType();
}
