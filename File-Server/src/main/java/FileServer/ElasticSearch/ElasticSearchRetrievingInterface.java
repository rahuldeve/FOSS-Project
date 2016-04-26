/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileServer.ElasticSearch;

import FileServer.Model.Query.QueryWrapper;
import FileServer.Model.Result.DocumentResultEntity;
import FileServer.Model.Result.ResultsWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author rahul
 */

@Service("elasticSearchRetrievingBean")
public class ElasticSearchRetrievingInterface {

    public static String fileQueryURL = "http://localhost:9200/fossuploads/files/_search";
    public String fileServerURL = "http://localhost:8080/getFile/";


    public ResultsWrapper<DocumentResultEntity> queryDocument(QueryWrapper queryWrapper) {
        
        ObjectMapper mapper = new ObjectMapper();

        String url = fileQueryURL;
        String query = QueryStringBuilder.buildDocumentQuery(queryWrapper);

        System.out.println(query);

        ResultsWrapper<DocumentResultEntity> resultList = new ResultsWrapper<>();
        List<DocumentResultEntity> retrievedResults = new ArrayList<>();

        try {

            HttpResponse<String> res = Unirest.post(url).body(query).asString();

            JsonNode root = mapper.readTree(res.getBody());

            System.out.println("*****************************************************************");
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
            Logger.getGlobal().log(Level.INFO, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));

            resultList.setQuery(queryWrapper.extractedContentFeild);
            resultList.setSearchTime(root.get("took").toString());
            resultList.setResultNum(root.get("hits").get("total").toString());

            JsonNode jsonResultList = root.get("hits").get("hits");

            if (jsonResultList.isArray()) {
                for (JsonNode result : jsonResultList) {

                    JsonNode content = result.get("_source");

                    DocumentResultEntity resultEntity = new DocumentResultEntity();
                    resultEntity.fileId = content.get("fileId").asText();
                    resultEntity.filename = content.get("filename").asText();
                    resultEntity.from = content.get("from").asText();
                    resultEntity.to = content.get("to").asText();
                    resultEntity.url = fileServerURL + content.get("filename").asText();

                    JsonNode highlights = result.get("highlight");

                    System.out.println(resultEntity.url);

                    if (highlights != null) {
                        StringBuilder builder = new StringBuilder();
                        for (JsonNode snippet : highlights.get("extractedContent")) {
                            builder.append(snippet.asText() + ".....");
                            //fs.extractedContent += snippet.toString() + "...";
                        }

                        resultEntity.extractedContent = builder.toString();
                    }

                    retrievedResults.add(resultEntity);
                }
            }

            resultList.setResultList(retrievedResults);

        } catch (UnirestException ex) {
            Logger.getLogger(ElasticSearchRetrievingInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ElasticSearchRetrievingInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultList;

    }

}
