package penso.stackhat.server.model;

public class NewTechnologyRequest {
    public String category = ""; // the category
    public String name = ""; // the PENSO name
    public String technology = ""; // the builtwith name

    public NewTechnologyRequest() {
        
    }    

    public String getCategory() { return category; }
    public void setCategory(String newCategory) { this.category=newCategory; }

    public String getName() { return name; }
    public void setName(String newName) { this.name=newName; }
    
    public String getTechnology() { return technology; }
    public void setTechnology(String newTechnology) { this.technology=newTechnology; }    
}
