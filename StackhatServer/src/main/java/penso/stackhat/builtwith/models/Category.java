package penso.stackhat.builtwith.models;

import java.util.ArrayList;

public class Category {
    public String Title = "";
    public ArrayList<Name> Names = new ArrayList<Name>();

    public Category() {
        
    }    

    public String getTitle() { return Title; }
    public void setTitle(String newTitle) { this.Title = newTitle; }

    public ArrayList<Name> getNames() { return Names; }
    public void setNames(ArrayList<Name> newNames) { this.Names = newNames; }    
}
