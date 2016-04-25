package FileServer.DataWrappers;

import java.util.List;

/**
 * Created by rahul on 17/12/15.
 *
 * This wrapper class is used to contain very minimal information from any text based files like PDF, Word Documents etc.
 * This class stores the entire text extracted from the file as a string.
 */
public class FileMetadataWrapper extends MetadataWrapper {

    String content;

    public FileMetadataWrapper(String filename, String mimeType, String extractedContent, List<String> tags, String from, String to) {
        super(filename, mimeType, tags, from, to);
        this.content = extractedContent;
    }


    public String getExtractedContent() {
        return content;
    }

    public void setExtractedContent(String extractedContent) {
        this.content = extractedContent;
    }
}
