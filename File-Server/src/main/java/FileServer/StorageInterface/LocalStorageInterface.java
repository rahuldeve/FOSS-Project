package FileServer.StorageInterface;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rahul on 28/12/15.
 */

@Service("localStorageBean")
public class LocalStorageInterface implements FileStorageInterface {

    public String uncompressedLocation = "/foss/uploads/";


    @Override
    public ResponseEntity<String> storeFile(MultipartFile file, String filename) {

        try {

            file.transferTo(new File(uncompressedLocation + filename));
            Logger.getLogger("LocalStorageInterface").log(Level.INFO, "Storing file : " + filename);

        } catch (IOException e) {

            Logger.getLogger("LocalStorageInterface").log(Level.SEVERE, e.toString());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("File Stored", HttpStatus.CREATED);

    }


    @Override
    public byte[] retrieveFile(String filename) {

        File file = new File(uncompressedLocation + filename);
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
