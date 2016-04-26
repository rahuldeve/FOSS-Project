package FileServer.Model.Result;

/**
 * Created by rahul on 2/10/16.
 */
public abstract class ResultEntity {

    public String from;
    public String to;
    public String date;


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
}
