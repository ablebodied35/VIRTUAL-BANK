import java.util.ArrayList;
import java.util.Calendar;

public class CheckingAccount extends Accounts {

	public CheckingAccount() {
		super();
	}
	
	public CheckingAccount(Depositor depositor,int acct,String typeofacct,boolean acctstatus, double bal,ArrayList<Recepit> rece){
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
         if(!status || amount < 0 || amount > balance){
             flag = false;
             if(!status){
                 reason = "This account is closed. To make a deposit, please re-open the account.";
             }
             else if(amount < 0){
                 reason = "Error: Invalid amount";
             }
             else if(amount > balance) {
            	 reason = "Error: Insufficient Balance";
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

         //Adds recepit to ArrayList of recepits
         getTransactionHistory(tick).add(recepit);
         Recepit copy = new Recepit(recepit);
         return copy;
     }
	
	
	/* Method clearCheck:
	    * Input:
	    * ch - Check object
	    * Process:
	    * error checks similarly to withdrawal but with checking accounts only
	    * Checks the date of the check to see if post-dated check or if time limit of
	    * 6 months is past
	    * Output:
	    */
	    public Recepit clearCheck(Check ch){

	        //Variable declaration
	        Recepit recepit = new Recepit();
	        Calendar date = ch.getdateOfCheck();
	        boolean flag;
	        boolean status = getAcctStatus();
	        Calendar cash = Calendar.getInstance();
	        cash.add(Calendar.MONTH, 1);//added one month to get date proper month
	        String reason = new String();
	        double prebalance =0;
	        double postbalance=0;
	       
	        try
	        {
	        if(ch.getcheckAmount() < 0.0 || ch.getcheckAmount() > getBalanceofAcct()|| !status){
	            flag = false;
	            if (ch.getcheckAmount() < 0.0){
	            	throw new InvalidAmountException(0);
	            }
	            else if(ch.getcheckAmount() > getBalanceofAcct()){
	                throw new InsufficentFundsException(true);
	            }
	            else if (!status){
	                reason = "Error: Account is closed";
	                prebalance =getBalanceofAcct();
	                postbalance =getBalanceofAcct();
	            }
	            date = Calendar.getInstance();
	            recepit = new Recepit(ch,flag,reason,prebalance,postbalance,date);
	        }

	       //This if checks to see if the date on the check is before todays date
	       //If it is then it adds 6 months to the check date to its expiriy date
	       //If todays date is after the new date then it is an invalid check
	       //and it fills out a unsuccessful recepit, elsewise it withdraws the amount
	       else if (cash.after(date)){
	            date.add(Calendar.MONTH, 6);
	            if(cash.after(date)){
	            	throw new CheckTooOldException();
	            }
	            else {
	                flag = true;
	                if(getBalanceofAcct() > 2500) {
	  	        		reason = "Successful Transaction";
	                }
	  	        	else {
	  	        		reason = "Successful Transaction. $1.50 fee will apply because the Account balance is below $2500";
	  	        	}
	                prebalance = getBalanceofAcct();
	                if(getBalanceofAcct() > 2500) {
	                	postbalance = getBalanceofAcct()- ch.getcheckAmount();
	                }
	                else {
	                	postbalance = (getBalanceofAcct() - 1.50) - ch.getcheckAmount();
	                }
	                Bank.subChecking(ch.getcheckAmount());
	                setBalance(postbalance);
	                date.add(Calendar.MONTH,-6);//remove 6 months to be able to print proper check date in main
	            }
	            date = Calendar.getInstance();
	            recepit = new Recepit(ch,flag,reason,prebalance,postbalance,date);
	        }

	       //This else if statement checks to see if the date of the check is after todays date
	       //If it is then the check is post dated and an unsuccessful recepit is filled out
	       //The reason I did this method in this way is because if I just moved the date
	       //forward six months and then checked to see if the cashing in date was before the expiry date
	       //the method would also allow post dated checks to be cashed. So I separated the two
	       //and made sure that 6 months are only added if the date on the check is before the cashdate
	       //this way post dated checks could not be cashed
	        else if(cash.before(date)){
	        	throw new PostDatedCheckException();
	        	}
	        }
	        catch(InvalidAmountException | CheckTooOldException | PostDatedCheckException e){
	        	  flag = false;
	        	  reason = e.getMessage();
	        	  prebalance =getBalanceofAcct();
	              postbalance =getBalanceofAcct();
	        	  recepit = new Recepit(ch,flag,reason,prebalance,postbalance,date);
		    }
	        catch (InsufficentFundsException e) {
	        	flag = false;
	        	reason=e.getMessage();
                prebalance = getBalanceofAcct();
                postbalance = getBalanceofAcct() - 2.50;
                Bank.subChecking(2.5);
                setBalance(postbalance);
                recepit = new Recepit(ch,flag,reason,prebalance,postbalance,date);
	        }
	        getTransactionHistory(ch).add(recepit);
	        Recepit copy = new Recepit(recepit);
	        return copy;
	    }
	    

	  //Special method for makeDeposit for the CDAccounts subclass
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
	  	                throw new InsufficentFundsException(true);
	  	            }
	  	            pre =getBalanceofAcct();
	  	            post =getBalanceofAcct(); 
	  	            recepit = new Recepit(tick,flag,reason,pre,post,date);

	  	        }
	  	        else if (status){
	  	        	flag = true;
	  	        	if(getBalanceofAcct() > 2500) {
	  	        		reason = "Successful Transaction";
	                }
	  	        	else {
	  	        		reason = "Successful Transaction. $1.50 fee will apply because the Account balance is below $2500";
	  	        	}
	                pre = getBalanceofAcct();
	               
	                //If statement in case of balance being less than 2500. Cuts $1.50 from balance of acct if true
	                if(getBalanceofAcct() > 2500) {
	                	post = pre - amount;
	                }
	                else {
	                	post = (pre-1.50)-amount;
	                }
	                setBalance(post);
	                Bank.subChecking(post);
	                recepit = new Recepit(tick,flag,reason,pre,post,date);
	  	        }
	  	        }
	  	        catch(InsufficentFundsException e) {
	  	          flag = false;
	          	  reason = "Error: Insufficient Funds";
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
