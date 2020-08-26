import java.util.Calendar;

public class Ticket {

		private Calendar dateofTransaction;
	    private String typeofTransaction;
	    private double amountofTransaction;
	    private int terms;
	    
	    
	   
	    
	    public Ticket(){
	        dateofTransaction = Calendar.getInstance();
	        typeofTransaction = new String();
	        amountofTransaction = 0.0;
	        terms =0;
	    }
	    
	    public Ticket(Calendar date, String type, double amount, int termsofCD){
	     
	    	dateofTransaction = date;
	        typeofTransaction = type;
	        amountofTransaction = amount;
	        terms = termsofCD;
	    }
	    
	    
	    
	    //Copy Constructor
	    public Ticket(Ticket tick){
	        dateofTransaction = tick.dateofTransaction;
	        typeofTransaction = tick.typeofTransaction;
	        amountofTransaction = tick.amountofTransaction;
	        terms = tick.terms;
	    }
	    
	    
	    
	    //setters
	    public void setdateofTransaction(Calendar date) {
	    	dateofTransaction = date;
	    }
	    
	    public void settypeofTransaction(String type) {
	    	typeofTransaction = type;
	    }

	    public void setamountofTransaction(double amount) {
	    	amountofTransaction = amount;
	    }
	    
	    public void setTermsofCD(int termsofCD) {
	    	terms = termsofCD;
	    }
	    
	    
	    //Getters 
	    public Calendar getdateofTransaction(){
	        return dateofTransaction;
	    }
	    
	    public String gettypeofTransaction(){
	        return typeofTransaction;
	    }
	    
	    public double getamountofTransaction(){
	        return amountofTransaction;
	    }
	    
	    public int getTermofCD(){
	        return terms;
	    }
	    
	    public String toString(){
	        String str = String.format("DATE:%2d/%2d/%2d \r\n%-15s \r\nAmount of Transaction:$%6.2f",dateofTransaction.get(Calendar.MONTH),
	                                                                  dateofTransaction.get(Calendar.DATE),
	                                                                  dateofTransaction.get(Calendar.YEAR),
	                                                                  typeofTransaction,amountofTransaction );
	    
	        return str;
	    
	    }
	    
	    //equals
	     public boolean equals(Ticket other){
	        if(dateofTransaction.equals(other.dateofTransaction) && typeofTransaction.equals(other.typeofTransaction) 
	                && amountofTransaction==other.amountofTransaction && terms==other.terms)
	            return true;
	        
	        return false;
	        
	    }
}
