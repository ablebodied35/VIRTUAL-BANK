
public class Name {
	private String first;
    private String last;
    
    
    //Constructor for Name
    public Name(){
        first = "";
        last = "";
     }
    
    
    
    //Arg-Constructor for setting Name 
    public  Name(String strfirst, String strlast){
        first = strfirst;
        last = strlast;
     }
   
    //Copy Constructor
    public Name(Name name){
        first = name.first;
        last = name.last;
    }

    
    //Getter methods for first and last name
    public String getfirstname(){
         return first;
    }
    
    public String getlastname(){
        return last;
        
    }
    
    public void setfirstname(String strfirst){
        first = strfirst;
    }
    
    public void setlastname(String strLast){
        last = strLast;
    }
    
   
    //toString
    public String toString(){
        
        String str = String.format("%-9s %-8s",first,last);
        return str;
    }
    
    //Equals method
    public boolean equals(Name other){
        if (first.equals(other.first) && last.equals(other.last)){
            return true;
        }
        return false;
        
    }
}
