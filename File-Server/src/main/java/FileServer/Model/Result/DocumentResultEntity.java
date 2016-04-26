package FileServer.Model.Result;

/**
 *
 * @author rahul
 */
public class DocumentResultEntity extends ResultEntity {


    public String fileId;
    public String filename;
    public String extractedContent;
    public String url;


    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return fileId;
    }

    public String getExtractedContent() {
        return extractedContent;
    }

    public String getFilename() {
        return filename;
    }

    public void setExtractedContent(String extractedContent) {
        this.extractedContent = extractedContent;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
