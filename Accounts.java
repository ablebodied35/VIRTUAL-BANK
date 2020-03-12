import java.util.ArrayList;
import java.util.Calendar;

public class Accounts  {
	
	
	private Depositor dep;
    private int acctnum;
    private String type;
    private boolean status;
    private double balance;
    private ArrayList<Recepit> recepits = new ArrayList<>();
    
    
   
  
    //No-Arg
    public Accounts(){
        dep = new Depositor();
        acctnum = 0;
        type = new String();
        status = true;
        balance = 0.0;
        recepits = new ArrayList();
    }
    
    public Accounts(Depositor depositor,int acct,String typeofacct,boolean acctstatus, double bal,ArrayList<Recepit> rece){
        dep = depositor;
        acctnum = acct;
        type = typeofacct;
        status = acctstatus;
        balance = bal;
        recepits = rece;
        
    }
    
       //Constructor to setup members
    public Accounts(Depositor depos, int accountnum, String accounttype, double bala, boolean acctstatus){
        dep= depos;
        acctnum = accountnum;
        type = accounttype;
        //maturityDate = Calendar.getInstance();
        status = acctstatus;
        /*if (accounttype.contains("CD")){
        maturityDate.clear();
        String[] tokens = accounttype.split("/");
        maturityDate.set(Integer.parseInt(tokens[3]),Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
        }*/
        balance = bala;
        recepits = new ArrayList<>();

    } 
    
    //Copy Constructor
    public Accounts(Accounts acc){
        dep = acc.dep;
        acctnum = acc.acctnum;
        type = acc.type;
        //maturityDate = acc.maturityDate;
        status = acc.status;
        balance = acc.balance;
        recepits = acc.recepits;
        
    }
    
    
    
    
    public Recepit getBalance(Ticket tick){
        
        //TransactionRecepit object and variables to fill out
        Recepit recepit = new Recepit();
        boolean flag;
        String reason;
        double pre=0;
        double post=0;
        Calendar date = tick.getdateofTransaction();


        //If account is closed, fills error messages out

        if (!status){
            flag = false;
            reason = "Error: Account " + acctnum + " closed. To retrieve balance. Please re-open the account.";
            recepit = new Recepit(tick,flag,reason,pre,post,date);

        }
        //Sends balance of account to method in main
        else if (status){
            flag = true;
            reason = "Successful Transaction";
            pre = balance;
            post = balance;
            recepit = new Recepit(tick,flag,reason,pre,post,date);

        }

        //Adds recepit to ArrayList of recepits
         recepits.add(recepit);
         Recepit copy = new Recepit(recepit);
         return copy;
         
    }
    
    /* Method makeDeposit:
     * Input:
     * tick - TransactionTicket object
     * Process:
     * checks to see if account is open or closed and then error checks
     * deposits amount into account
     * Output:
     */
     public Recepit makeDeposit(Ticket tick){

         //Variable and Object declarations
         Recepit recepit = new Recepit();
         boolean flag;
         String reason = new String();
         double pre=0;
         double post=0;
         double amount = tick.getamountofTransaction();
         Calendar date = tick.getdateofTransaction();

         try {
         //Error checking and status check
         if(!status || amount < 0){
             flag = false;
             if(!status){
                 reason = "This account is closed. To make a deposit, please re-open the account.";
             }
             else if(amount < 0){
                throw new InvalidAmountException(0);
             }
             pre = balance;
             post = balance;
             recepit = new Recepit(tick,flag,reason,pre,post,date);

         }
         else if (status){

                 flag = true;
                 reason = "Successful Transaction";
                 pre = balance;
                 post = pre + amount;
                 if (type.equals("Savings")){
                     Bank.addSavings(amount);
                 }
                 else if(type.equals("Checking"))
                     Bank.addChecking(amount);
                 balance = post;
                 recepit = new Recepit(tick,flag,reason,pre,post,date);
             }
         }
         catch(InvalidAmountException e) {
        	 flag = true;
        	 pre = balance;
             post = balance;
             recepit = new Recepit(tick,flag,reason,pre,post,date);
         }

         //Adds recepit to ArrayList of recepits
         recepits.add(recepit);
         Recepit copy = new Recepit(recepit);
         return copy;
     }
    
    
     /* Method makewithdrawl:
      * Input:
      * tick - TransactionTicket object
      * Process:
      * checks to see if account is open or closed, then error checks
      * withdraws from account
      * Output:
      */
      public Recepit makeWithdrawal(Ticket tick){

          //Object and variable declaration
          Recepit recepit = new Recepit();
          boolean flag;
          String reason = new String();
          double pre=0;
          double post=0;
          double amount = tick.getamountofTransaction();
          Calendar date = tick.getdateofTransaction();
          try 
          {
          //Error checks
          if(!status || tick.getamountofTransaction() < 0 || amount>balance){
              flag = false;
              if(!status){
                  reason = "Error: Account "+ acctnum + " is closed. To make a withdrawl, please re-open the account.";
              }
              else if(tick.getamountofTransaction() < 0){
                  throw new InvalidAmountException(tick.getamountofTransaction());
              }
              else if(amount > balance){
                  throw new InsufficentFundsException(true);
              }
              pre =balance;
              post =balance; 
              recepit = new Recepit(tick,flag,reason,pre,post,date);

          }
          else if (status){
        	  	flag = true;
                reason = "Successful Transaction";
                pre = balance;
                post = pre - amount;
                if (type.equals("Savings")){
                    Bank.subSavings(amount);
                }
                else if (type.equals("Checking")){
                    Bank.subChecking(amount);
                }
                balance = post;
                recepit = new Recepit(tick,flag,reason,pre,post,date);
          }
          }
          catch(InsufficentFundsException e) {
        	  flag = false;
        	  reason = e.getMessage();
        	  pre =balance;
        	  post = balance;
        	  recepit = new Recepit(tick,flag,reason,pre,post,date);
          }
          catch(InvalidAmountException e) {
        	  flag = false;
        	  reason = e.getMessage();
        	  pre =balance;
        	  post = balance;
        	  recepit = new Recepit(tick,flag,reason,pre,post,date);
          }
         
          //Adds recepit to ArrayList of recepits
          recepits.add(recepit);
          Recepit copy = new Recepit(recepit);
          return copy;
          
      }
    
      
      //
      public Recepit clearCheck(Check ch) {
    	
    	  //Variable declaration
          Recepit recepit = new Recepit();
          Calendar date = ch.getdateOfCheck();
          boolean flag;
          Calendar cash = Calendar.getInstance();
          cash.add(Calendar.MONTH, 1);//added one month to get date proper month
          String reason = new String();
          double prebalance =0;
          double postbalance=0;
         
          //if(!type.contains("Checking")){
              flag = false;
             
               if(!type.contains("Checking")){
                  reason = "Error: Checks can only be cleared for Checkings accounts";
                  prebalance =balance;
                  postbalance =balance;
              }
             
              date = Calendar.getInstance();
              recepit = new Recepit(ch,flag,reason,prebalance,postbalance,date);
          //}
          recepits.add(recepit);
          Recepit copy = new Recepit(recepit);
          return copy;
      }
      
      
      /* Method closeAcct:
       * Input:
       * tick - TransactionTicket object
       * Process:
       * Checks to see if account is already closed. If not fills TransactionRecepit object
       * closes account and adds new TransactionRecepit object to arraylist of TransactionRecepit objects
       * Output:
       */
       public Recepit closeAcct(Ticket tick){

           //Object and variable declaration
           Recepit recepit = new Recepit();
           boolean flag;
           String reason = new String();
           double pre=balance;
           double post=balance;
           double amount = tick.getamountofTransaction();
           Calendar date = tick.getdateofTransaction();

           if(!status){
               flag = false;
               reason = "Error: Account "+acctnum+" is already closed";
               recepit = new Recepit(tick,flag,reason,pre,pre,date);

           }
           else if (status){
               flag = true;
               reason= "Account "+ acctnum+ " closed";
               status = false;
               recepit = new Recepit(tick,flag,reason,pre,pre,date);
           }

           recepits.add(recepit);
           Recepit copy = new Recepit(recepit);
           return copy;
       }
       
       
       /* Method creopenAcct:
       * Input:
       * tick - TransactionTicket object
       * Process:
       * Checks to see if account is already open. If not fills TransactionRecepit object
       * opens account and adds new TransactionRecepit object to arraylist of TransactionRecepit objects
       * Output:
       */
       public Recepit reopenAcct(Ticket tick){

           //Object and variable declaration
           Recepit recepit = new Recepit();
           boolean flag;
           String reason = new String();
           double pre=balance;
           double post=balance;
           double amount = tick.getamountofTransaction();
           Calendar date = tick.getdateofTransaction();

           if(status){
               flag = false;
               reason = "Error: Account "+acctnum+" is already open";
               recepit = new Recepit(tick,flag,reason,pre,pre,date);

           }
           else if (!status){
               flag = true;
               reason= "Account "+ acctnum+ " is now open";
               status = true;
               recepit = new Recepit(tick,flag,reason,pre,pre,date);
           }

           recepits.add(recepit);
           Recepit copy = new Recepit(recepit);
           return copy;



       }
       
      
      
      
     
    
    //setters
    public void setBalance(double amount) {
    	balance = amount;
    }
   
    public void setdep(Depositor d) {
    	dep = d;
    }
    
    public void setAcctNum(int acct) {
    	acctnum = acct;
    }
    
    public void setaccttype(String typeofAcct) {
    	type = typeofAcct;
    }
    
    public void setBalanceofAcct(double bal) {
    	balance = bal;
    }
    
    public void setAcctStatus(boolean f) {
    	status = f;
    }
    
    
    
    
    //Getters
    public ArrayList<Recepit> getTransactionHistory(Ticket tick){
        ArrayList<Recepit> copy = new ArrayList<>(recepits);
    	
        return copy;
    }
    
    
    //Special getTransactionHistory for clearCheck in CheckingAccounts subclass
    public ArrayList<Recepit> getTransactionHistory(Check ch){
        
        return recepits;
    }
    
    
    public Depositor getdep(){
        Depositor copy = new Depositor(dep);
        return copy;
    }
    
    public int getAcctNum(){
        return acctnum;
    }
    
    public String getaccttype(){
        return type;
    }
    
   
    public void addTransaction(Recepit recep){
        recepits.add(recep);
    }
    
    public double getBalanceofAcct(){
        return balance;
    }
    public boolean getAcctStatus(){
        return status;
    }
    
     public String toString(){
        String stat;
        if(status){
            stat = "Open";
        }
        else{
            stat = "Closed";
        }
        String str = String.format("%-26s %-6d %-13s %-6s $%-6.2f",dep,acctnum,type,stat,balance);
        return str;
    }
   
     //Equals
    public boolean equals(Accounts other){
        if(dep.equals(other.dep) && acctnum==other.acctnum && type.equals(other.type) && status==other.status && balance==other.balance){
            return true;
        }
        return false;
    }
}
