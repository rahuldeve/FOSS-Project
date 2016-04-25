package FileServer.StorageInterface;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by rahul on 17/12/15.
 */
public interface FileStorageInterface {

    public ResponseEntity<String> storeFile(MultipartFile file, String filename);

    public byte[] retrieveFile(String filename);
}
