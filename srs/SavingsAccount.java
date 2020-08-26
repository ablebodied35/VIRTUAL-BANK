import java.util.ArrayList;
import java.util.Calendar;

public class SavingsAccount extends Accounts{


	public SavingsAccount(){
		super();
	}
	
	public SavingsAccount(Depositor depositor,int acct,String typeofacct,boolean acctstatus, double bal,ArrayList<Recepit> rece) {
		super(depositor,acct,typeofacct,acctstatus,bal,rece);
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
         boolean status = getAcctStatus();
         String reason = new String();
         double pre=0;
         double post=0;
         double balance = getBalanceofAcct();
         double amount = tick.getamountofTransaction();
         Calendar date = tick.getdateofTransaction();


         //Error checking and status check
         try {
         if(!status || amount < 0 ){
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
                 setBalance(post);
                 Bank.addChecking(amount);
                 balance = post;
                 recepit = new Recepit(tick,flag,reason,pre,post,date);
            
         }
         }
         catch(InvalidAmountException e) {
       	  flag = false;
       	  reason = e.getMessage();
       	  pre =getBalanceofAcct();
       	  post = getBalanceofAcct();
       	  recepit = new Recepit(tick,flag,reason,pre,post,date);
	      }

         //Adds recepit to ArrayList of recepits
         getTransactionHistory(tick).add(recepit);
         Recepit copy = new Recepit(recepit);
         return copy;
     }
	
	
		//Special method for makeWithdrawal for the Savings subclass
		public Recepit makeWithdrawal(Ticket tick){
			
			//Variable and Object declarations
	        Recepit recepit = new Recepit();
	        boolean flag;
	        boolean status = getAcctStatus();
	        String reason = new String();
	        double pre;
	        double post;
	        double amount = tick.getamountofTransaction();
	        Calendar date = tick.getdateofTransaction();
	        date.add(Calendar.MONTH,-1);
	        
	        
	      //Error checks
	        try {
	        if(!status || tick.getamountofTransaction() < 0 || amount>getBalanceofAcct()){
	            flag = false;
	            if(!status){
	                reason = "Error: Account "+ getAcctNum() + " is closed. To make a withdrawl, please re-open the account.";
	            }
	            else if(tick.getamountofTransaction() < 0){
	                throw new InvalidAmountException(tick.getamountofTransaction());
	            }
	            else if(amount > getBalanceofAcct()){
	                //reason = "Error: Insufficient funds";
	            	throw new InsufficentFundsException(amount > getBalanceofAcct());
	            }
	            pre =getBalanceofAcct();
	            post =getBalanceofAcct(); 
	            recepit = new Recepit(tick,flag,reason,pre,post,date);

	        }
	        else if (status){
	        flag = true;	        	
	        reason = "Successful Transaction";
	        pre = getBalanceofAcct();
            post = pre - amount;
            setBalance(post);
            Bank.subChecking(post);
            recepit = new Recepit(tick,flag,reason,pre,post,date);
	        }
	        }
	        catch(InsufficentFundsException e) {
	        	  flag = false;
	        	  reason = e.getMessage();
	        	  pre =getBalanceofAcct();
	        	  post = getBalanceofAcct();
	        	  recepit = new Recepit(tick,flag,reason,pre,post,date);
	        }
	        catch(InvalidAmountException e) {
	        	  flag = false;
	        	  reason = e.getMessage();
	        	  pre =getBalanceofAcct();
	        	  post = getBalanceofAcct();
	        	  recepit = new Recepit(tick,flag,reason,pre,post,date);
	        }
	        getTransactionHistory(tick).add(recepit);
	        Recepit copy = new Recepit(recepit);
	        return copy;
		}
  
}
