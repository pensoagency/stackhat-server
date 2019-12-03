package penso.stackhat.builtwith.models;

import java.util.ArrayList;

public class Name {
    public String Title = "";
    public ArrayList<String> Technologies = new ArrayList<String>();

    public Name() {
        
    }    

    public String getTitle() { return Title; }
    public void setTitle(String newTitle) { this.Title = newTitle; }

    public ArrayList<String> getTechnologies() { return Technologies; }
    public void setTechnologies(ArrayList<String> newTechnologies) { this.Technologies = newTechnologies; }    
}
