import java.util.Calendar;

public class Check {

	

    private int AcctNumber;
    private double checkAmount;
    private Calendar dateOfCheck;
    
    
    
    
    //No-Arg constructor
    public Check(){
        AcctNumber =0;
        checkAmount = 0.0;
        dateOfCheck = Calendar.getInstance();
    }
    
    //Argumentative constructor
    public Check(int acctnum, double checkamount, Calendar dateofcheck) {
       
    	AcctNumber = acctnum;
        checkAmount= checkamount;
        dateOfCheck = dateofcheck;
       
    }
    
    //Copy Contstructor
    public Check(Check ch){
        AcctNumber = ch.AcctNumber;
        checkAmount = ch.checkAmount;
        dateOfCheck = ch.dateOfCheck;
    }
    
    //Getter methods
    public int getAcctNumber(){
        return AcctNumber;
    }
    
    public double getcheckAmount(){
        return checkAmount;
    }
    
    public Calendar getdateOfCheck(){
        return dateOfCheck;
    }  
    
    public String toString(){
        String str = String.format("\rAccount Number: %-6s \r\nAmount of Transaction: $%-5.2f \r\nDate of Check:%2d/%2d/%4d",AcctNumber
                                                                ,checkAmount
                                                                ,dateOfCheck.get(Calendar.MONTH)
                                                                ,dateOfCheck.get(Calendar.DATE)
                                                                ,dateOfCheck.get(Calendar.YEAR));
        return str;
    }
    
    //equals
    public boolean equals(Check other){
        if(AcctNumber == other.AcctNumber && checkAmount == other.checkAmount && dateOfCheck.equals(other.dateOfCheck))
            return true;
       
        return false;
    }
}
