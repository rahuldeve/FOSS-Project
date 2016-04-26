/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileServer.ElasticSearch;


import FileServer.Model.Query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 *
 * @author rahul
 */
public class QueryStringBuilder {



    

    public static String buildDocumentQuery(QueryWrapper queryWrapper){

        ObjectMapper mapper = new ObjectMapper();
        JsonNodeFactory nodeFactory = mapper.getNodeFactory();

        ObjectNode root = nodeFactory.objectNode();
        root.set("query", nodeFactory.objectNode().set("filtered", nodeFactory.objectNode()));

        ObjectNode queryNode = (ObjectNode) root.with("query").with("filtered").set("query", nodeFactory.objectNode());
        ObjectNode filterNode = (ObjectNode) root.with("query").with("filtered").set("filter", nodeFactory.objectNode());

        //----------------------------------------------------------------------
        // Query Section

        ArrayNode mustList = nodeFactory.arrayNode();
        ArrayNode shouldList = nodeFactory.arrayNode();

        if(queryWrapper.fromFeild != null){
            ObjectNode matchNode = nodeFactory.objectNode();
            matchNode.set("match", nodeFactory.objectNode().set("from", nodeFactory.objectNode()));
            matchNode.with("match").with("from").put("query", queryWrapper.fromFeild);
            matchNode.with("match").with("from").put("operator", "or");
            mustList.add(matchNode);
        }

        if(queryWrapper.toFeild != null){
            ObjectNode matchNode = nodeFactory.objectNode();
            matchNode.set("match", nodeFactory.objectNode().set("to", nodeFactory.objectNode()));
            matchNode.with("match").with("to").put("query", queryWrapper.toFeild);
            matchNode.with("match").with("to").put("operator", "or");
            mustList.add(matchNode);
        }

        if(queryWrapper.extractedContentFeild != null){
            ObjectNode matchNode = nodeFactory.objectNode();
            matchNode.set("match", nodeFactory.objectNode().set("extractedContent", nodeFactory.objectNode()));
            matchNode.with("match").with("extractedContent").put("query", queryWrapper.extractedContentFeild);
            matchNode.with("match").with("extractedContent").put("operator", "or");
            mustList.add(matchNode);
        }

        if(queryWrapper.tags != null){
            ObjectNode matchNode = nodeFactory.objectNode();
            matchNode.set("fuzzy", nodeFactory.objectNode().put("tags", queryWrapper.tags));
            shouldList.add(matchNode);
        }


        queryNode.with("query").set("bool", nodeFactory.objectNode().set("should", shouldList));
        queryNode.with("query").with("bool").set("must", mustList);
        //queryNode.with("query").with("bool").put("minimum_should_match", 1);

        //----------------------------------------------------------------------



        //----------------------------------------------------------------------
        // Filter Section

        if( queryWrapper.mimeType != null){

            ObjectNode termNode = nodeFactory.objectNode();
            termNode.put("mimeType", queryWrapper.mimeType);
            filterNode.with("filter").set("term", termNode);

        }

        //----------------------------------------------------------------------


        ObjectNode highlightNode = (ObjectNode) root.set("highlight", nodeFactory.objectNode());
        highlightNode.with("highlight").set("fields", nodeFactory.objectNode().set("extractedContent", nodeFactory.objectNode().put("type", "plain")));

        return root.toString();
    }
}
