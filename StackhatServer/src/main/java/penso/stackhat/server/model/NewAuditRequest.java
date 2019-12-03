package penso.stackhat.server.model;

public class NewAuditRequest {
    public String[] urls = new String[0];

    public NewAuditRequest() {
        
    }    

    public String[] getUrls() { return urls; }
    public void setUrls(String[] urls) { this.urls=urls; }
}
