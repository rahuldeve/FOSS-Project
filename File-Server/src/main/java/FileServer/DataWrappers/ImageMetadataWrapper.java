package FileServer.DataWrappers;

import java.util.List;

/**
 * Created by rahul on 18/01/16.
 *
 * A wrapper class exclusively for images. This class has provision to store extracted CEDD feature of an image as a
 * string.
 */
public class ImageMetadataWrapper extends MetadataWrapper {

    String CEDD;

    public ImageMetadataWrapper(String filename, String mimeType, String CEDD, List<String> tags, String from, String to) {
        super(filename, mimeType, tags, from, to);
        this.CEDD = CEDD;
    }

    public String getCEDD() {
        return CEDD;
    }

    public void setCEDD(String CEDD) {
        this.CEDD = CEDD;
    }
}
