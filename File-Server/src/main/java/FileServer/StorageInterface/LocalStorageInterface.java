package FileServer.StorageInterface;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rahul on 28/12/15.
 */

@Service("localStorageBean")
public class LocalStorageInterface implements FileStorageInterface {

    @Value("${uncompressed}")
    public String uncompressedLocation;

    @Value("${compressed}")
    public String compressedLocation;



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
    public ResponseEntity<String> storeCompressedFile(MultipartFile file, String filename) {

        File zipFile = new File(compressedLocation + filename + ".zip");
        GZIPInterface.compressFile(file, filename, zipFile);
        Logger.getLogger("LocalStorageInterface").log(Level.INFO, "Storing file : " + filename);

        return new ResponseEntity<>("File Stored:" + filename, HttpStatus.CREATED);

    }




    @Override
    public byte[] retrieveFile(String filename) {

        //TODO : fill this later
        return null;
    }

    @Override
    public byte[] retrieveCompressedFile(String filename) {

        //TODO : See if you can improve this
        //TODO : put some logging here
        byte[] bytes = GZIPInterface.decompressFile(new File(compressedLocation + filename + ".zip"), filename);
        return bytes;

    }


}
