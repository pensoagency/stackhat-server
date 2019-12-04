package penso.stackhat.server.model;

public class AuditResponse {
    public String id = "";
    public String[] urls;
    public String title = "";
    public Boolean isReady = false;
    public Boolean isError = false;

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
