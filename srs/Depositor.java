
public class Depositor {
	
		private Name name;
	    private String SSN;
	    
	    
	    
	    //No-Arg constructor 
	    public Depositor(){
	        name = new Name();
	        SSN = new String();
	    }
	   
	    
	    
	    //Argumentative constructor for social security and Name 
	    public Depositor(Name myname,String Social){
	        SSN = Social;
	        name = myname;
	    }
	    
	    //Copy Constructor
	    public Depositor(Depositor dep){
	        name = dep.name;
	        SSN = dep.SSN;
	    }
	    
	   //Getter methods for name and SSN
	    public String getSSN(){
	        return SSN;
	    }
	    
	    public Name getName(){
	       Name copy = new Name(name);
	       return copy;
	    }
	    
	    public void setSSN(String social){
	        SSN = social;
	    }
	    
	    public void setName(Name myname){
	        name = myname;
	    }
	    
	    public String toString(){
	        String str =String.format("%-17s %-9s",name,SSN);
	        return str;
	    }
	    
	    //equals
	    public boolean equals(Depositor other){
	        if(name.equals(other.name) && SSN.equals(other.SSN)){
	            return true;
	        }
	        return false;
	    }
	
}
