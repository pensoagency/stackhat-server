package penso.stackhat.server.model;

import java.util.ArrayList;
import java.util.Date;

public class AuditResponse {
    public String id = "";
    public String[] urls;
    public String title = "";
    public Boolean isReady = false;
    public Boolean isError = false;
    public Date created = new Date(System.currentTimeMillis());
    public ArrayList<String> log = new ArrayList<String>();

    public void setIsReady(Boolean isReady) { this.isReady=isReady; }      
    public void setIsError(Boolean isError) { this.isReady=isError; }    

    public AuditResponse() {
        
    }    
    public AuditResponse(String id, String[] urls, String title, Boolean isReady, Boolean isError) {
        this.id = id;
        this.urls = urls;
        this.title = title;
        this.isReady = isReady;
        this.isError = isError;
    }
}
