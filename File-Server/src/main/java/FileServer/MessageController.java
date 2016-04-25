package FileServer;

import logger.Converter.FileConverter;
import logger.Converter.MIMEResolver;
import logger.DataWrappers.MessageDataWrapper;
import logger.DataWrappers.MetadataWrapper;
import logger.Index.IndexInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Created by rahul on 16/12/15.
 */

@RestController
public class MessageController {

    @Resource(name = "elasticSearchBean")
    IndexInterface indexInterface;

    @Resource(name = "fileStorageBean")
    FileStorageInterface fileStorageInterface;


    @Autowired
    MIMEResolver mimeResolver;


    @RequestMapping(value = "/indexMessage", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> indexMessage(@RequestBody MessageDataWrapper messageDataWrapper) {

        //index message
        return indexInterface.indexMessage(messageDataWrapper);

    }

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
