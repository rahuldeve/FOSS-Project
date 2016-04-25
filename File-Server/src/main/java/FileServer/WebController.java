package FileServer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by rahul on 4/25/16.
 */
@RestController
public class WebController {

    @RequestMapping(value = "/indexFile", method = RequestMethod.POST)
    public ResponseEntity<String> indexFile(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename,
                                            @RequestParam(required = false) List<String> tags,
                                            @RequestParam(required = false) String from, @RequestParam(required = false) String to) {

        Logger.getLogger("MessageController -> indexFile").log(Level.INFO, "File: " + filename);
        if (!file.isEmpty()) {


            FileConverter fileConverter = mimeResolver.getFileConverter(file);
            List<MetadataWrapper> metadataWrappers = null;

            try {
                metadataWrappers = fileConverter.extractContent(file.getInputStream(), filename, file.getContentType(), tags, from, to, true);
            } catch (IOException e) {
                e.printStackTrace();
            }



            //index metadata
            //TODO handle null metadata
            if(metadataWrappers != null) {

                List<ResponseEntity<String>> failedEntries = metadataWrappers.stream()
                        .map(indexInterface::indexFile)
                        .filter(response -> response.getStatusCode() != HttpStatus.CREATED)
                        .collect(Collectors.toList());

                if (failedEntries.size() > 0)
                    return new ResponseEntity<>("Failed to parse parts of the file " + filename, HttpStatus.INTERNAL_SERVER_ERROR);
                else
                    return fileStorageInterface.storeFile(file, filename);


            }else{
                return new ResponseEntity<>("Failed to parse file " + filename, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return new ResponseEntity<>("file empty", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
