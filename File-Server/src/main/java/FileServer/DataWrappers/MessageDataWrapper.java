package FileServer.DataWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rahul on 16/12/15.
 *
 * A wrapper class used to encapsulate details of a message. Has @JsonCreator and @JsonProperty annotations for automatic
 * marshalling from json to object using jackson
 */
public class MessageDataWrapper {

    private String messageId;
    private String from;
    private String to;
    private String date;
    private String time;
    private String body;

    private String body_suggest;
    private List<String> name_suggest;


    @JsonCreator
    public MessageDataWrapper(@JsonProperty("from") String from, @JsonProperty("to") String to, @JsonProperty("date") String date, @JsonProperty("time") String time, @JsonProperty("body") String body) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        this.body = body;

        this.messageId = Integer.toString(this.hashCode());     //TODO : use some other hash function on the string
        this.body_suggest = body;
        this.name_suggest = Arrays.asList(from, to);
    }


    public String getId() {
        return messageId;
    }

    public void setId(String id) {
        this.messageId = id;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody_suggest() {
        return body_suggest;
    }

    public void setBody_suggest(String body_suggest) {
        this.body_suggest = body_suggest;
    }

    public List<String> getName_suggest() {
        return name_suggest;
    }

    public void setName_suggest(List<String> name_suggest) {
        this.name_suggest = name_suggest;
    }
}
