package FileServer.Model.Result;

import java.util.List;

/**
 *
 * @author rahul
 * @param <T>
 */
public class ResultsWrapper<T> {
    
    String query;
    String searchTime;
    String resultNum;
    List<T> resultList;
    
    

    public String getResultNum() {
        return resultNum;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public String getSearchTime() {
        return searchTime;
    }
    
    public String getQuery() {
        return query;
    }  

    public void setResultNum(String resultNum) {
        this.resultNum = resultNum;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
        

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

        
    
    
    
    
}
