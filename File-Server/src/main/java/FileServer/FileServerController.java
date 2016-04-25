package FileServer;

import FileStorage.FileStorageInterface;
import org.apache.tika.Tika;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Created by rahul on 25/01/16.
 */
@RestController
public class FileServerController {

    @Resource(name = "localStorageBean")
    FileStorageInterface fileStorageInterface;

    @RequestMapping(value = "/getFile/{filename:.+}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> getFile(@PathVariable("filename") String filename) {


        Tika tika = new Tika();
        byte bytes[] = fileStorageInterface.retrieveCompressedFile(filename);
        String mimeType = tika.detect(bytes, filename);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(mimeType));
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/storeFile", method = RequestMethod.POST)
    public ResponseEntity<String> storeFile(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename){

        return fileStorageInterface.storeCompressedFile(file, filename);

    }
}
