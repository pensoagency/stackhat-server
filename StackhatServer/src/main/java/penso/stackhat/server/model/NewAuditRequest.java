package penso.stackhat.server.model;

public class NewAuditRequest {
    public String[] urls = new String[0];
    public String name = "";

    public NewAuditRequest() {
        
    }    

    public String[] getUrls() { return urls; }
    public void setUrls(String[] urls) { this.urls=urls; }

    public String getName() { return name; }
    public void setName(String name) { this.name=name; }    
}
