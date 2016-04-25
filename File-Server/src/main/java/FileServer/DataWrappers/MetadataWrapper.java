package FileServer.DataWrappers;

import java.util.List;

/**
 * Created by rahul on 18/01/16.
 *
 * A wrapper class used to encapsulate the metadata extracted from a file. This class is used for containing minimal
 * information for any type of file. UExtend this class to handle for other specialised files.
 */
public class MetadataWrapper {

    String fileId;
    String filename;
    String mimeType;
    List<String> tags;
    String from;
    String to;


    public MetadataWrapper(String filename, String mimeType, List<String> tags, String from, String to) {

        this.fileId = Integer.toString(this.hashCode());        //TODO : use md5 hash
        this.filename = filename;
        this.mimeType = mimeType;
        this.tags = tags;
        this.from = from;
        this.to = to;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
