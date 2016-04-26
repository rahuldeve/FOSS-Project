package FileServer;

import FileServer.Converter.FileConverter;
import FileServer.Converter.MIMEResolver;
import FileServer.DataWrappers.MetadataWrapper;
import FileServer.ElasticSearch.ElasticSearchRetrievingInterface;
import FileServer.Index.ElasticSearchStoringInterface;
import FileServer.Model.Query.QueryWrapper;
import FileServer.Model.Result.DocumentResultEntity;
import FileServer.Model.Result.ResultsWrapper;
import FileServer.StorageInterface.FileStorageInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by rahul on 4/26/16.
 */
@Controller
public class WebController {

    @Resource(name = "elasticSearchStoringBean")
    ElasticSearchStoringInterface elasticSearchStoringInterface;

    @Resource(name = "elasticSearchRetrievingBean")
    ElasticSearchRetrievingInterface elasticSearchRetrievingInterface;

    @Resource(name = "localStorageBean")
    FileStorageInterface fileStorageInterface;


    @Autowired
    MIMEResolver mimeResolver;


    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @RequestMapping(value = "/query")
    public String queryDocument(@RequestParam(required = false) String query, Model model){

        if(query == null){
            return "results";
        }

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.extractedContentFeild = query;
        ResultsWrapper<DocumentResultEntity> results = elasticSearchRetrievingInterface.queryDocument(wrapper);


        model.addAttribute("results", results);
        return "results";
    }

    @RequestMapping("/upload")
    public String gen(){
        return "upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
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
                        .map(elasticSearchStoringInterface::indexFile)
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
