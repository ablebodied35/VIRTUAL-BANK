import java.util.ArrayList;
import java.util.Calendar;

public class Recepit {

	private Ticket tick;
    private boolean flag;
    private String reason;
    private double preBalance;
    private double postBalance;
    private Calendar postMaturityDate;
    private Check chek;//Filled in special cases only    
    private ArrayList<Accounts> historyofAccts = new ArrayList<>();
    
 
    
   
    public Recepit(){
        tick = new Ticket();
        flag = true;
        reason= new String();
        preBalance = 0.0;
        postBalance=0.0;
        postMaturityDate=Calendar.getInstance();
    }
    
    
    public Recepit(Ticket ticket, boolean successflag, String reasonforfailure, double prebalance, double postbalance, Calendar date){
        tick = ticket;
        flag = successflag;
        reason = reasonforfailure;
        preBalance = prebalance;
        postBalance = postbalance;
        postMaturityDate = date;
    }
    
    //This is a special constructor made to retrieve account info in the form of an ArrayList
    public Recepit(Ticket ticket,ArrayList<Accounts> acc, boolean successflag, String reasonforfailure, double prebalance, double postbalance, Calendar date){
        tick = ticket;
        historyofAccts = acc;
        flag = successflag;
        reason = reasonforfailure;
        preBalance = prebalance;
        postBalance = postbalance;
        postMaturityDate = date;
    }
    
    
    //Copy Constructor
    public Recepit(Recepit rece,String acctInfo){//Extra parameter for this copy constructor to not confuse it with other copy constructor
       
        tick = rece.tick;
        historyofAccts = rece.historyofAccts;
        flag = rece.flag;
        reason = rece.reason;
        preBalance = rece.preBalance;
        postBalance = rece.postBalance;
        postMaturityDate = rece.postMaturityDate;
        
    }
    
    
    
    
    //Special constructor for the Check class
    public Recepit(Check ch, boolean successflag, String reasonforfailure, double prebalance, double postbalance, Calendar date){
        chek = ch;
        flag = successflag;
        reason = reasonforfailure;
        preBalance = prebalance;
        postBalance = postbalance;
        postMaturityDate = date;
    }
    
    
    
    //Copy Constructor
    public Recepit(Recepit rece){
       
        tick = rece.tick;
        flag = rece.flag;
        reason = rece.reason;
        preBalance = rece.preBalance;
        postBalance = rece.postBalance;
        postMaturityDate = rece.postMaturityDate;
        
    }
    
    
    //Getters
    public ArrayList<Accounts> gethistoryofAccts(){
        return historyofAccts;
    }
   
    
    public Ticket getticket(){
        Ticket copy = new Ticket(tick);
    	return copy;
    }
    
    public boolean getflag(){
        return flag;
    }
    
    public String getreason(){
        return reason;
    }
    
    public double getpreBalance(){
        return preBalance;
    }
   
    public double getpostBalance(){
        return postBalance;
    }
    public Calendar getMaturityDate(){
        return postMaturityDate;
    }
    
    public Check getCheck(){
        Check copy = new Check(chek);
        return copy;
    }
    //Setters
    
    public void setticket(Ticket Ttick) {
    	tick = Ttick;
    }
    
    public void setflag(boolean f) {
    	flag = f;
    }
    
    public void setreason(String ReasonforFailure) {
    	reason = ReasonforFailure;
    }
    
    public void setpreBalance(double pre) {
    	preBalance = pre;
    }
   
    public void setpostBalance(double post) {
    	postBalance = post;
    }
    
    public void setMaturityDate(Calendar date) {
    	postMaturityDate = date;
    }
    
    
    
    public boolean equals(Recepit other){
        if(tick.equals(other.tick) && flag==other.flag && reason.equals(other.reason) && preBalance==other.preBalance
               && postBalance==other.postBalance && chek.equals(other.chek) && historyofAccts.equals(other.historyofAccts)){
            return true;
        } 
        return false;
    }
       
    public String toString(){
        String str = String.format("%s \r\n%s \r\nPre-Balance:$%-6.2f \r\nPost-Balance:$%-6.2f",tick,reason,preBalance,postBalance);
        
        return str;
    }
     
    //Special toString for printing for clearCheck method
    public String toString(Check ch){
        String str = String.format("\r%s \r\nPre-Balance:$%-6.2f \r\nPost-Balance:$%-6.2f",reason,preBalance,postBalance);
        
        return str;
    }
}
