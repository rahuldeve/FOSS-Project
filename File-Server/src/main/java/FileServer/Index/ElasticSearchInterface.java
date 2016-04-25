package FileServer.Index;


import FileServer.DataWrappers.MetadataWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by rahul on 16/12/15.
 */


@Service("elasticSearchBean")
public class ElasticSearchInterface implements IndexInterface {

    public String fileIndexURL = "http://localhost:9200/fossuploads/files/";

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    public ElasticSearchInterface() {

        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        //TODO: create a mapping for elastic search for date if the index is not yet created

    }

    @Override
    public ResponseEntity<String> indexFile(MetadataWrapper metadataWrapper) {

        String url = fileIndexURL + metadataWrapper.getFileId();
        HttpResponse<String> putResponse = null;

        try {
            putResponse = Unirest.put(url).header("Content-type", "application/json").body(metadataWrapper).asString();
            Logger.getLogger("ElasticSearchInterface").log(Level.INFO, putResponse.getStatusText() + ":" + putResponse.getBody());

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        //TODO handle null pointer exception
        return new ResponseEntity<>(putResponse.getBody(), HttpStatus.valueOf(putResponse.getStatus()));

    }


}
