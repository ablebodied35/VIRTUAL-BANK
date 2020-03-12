import java.util.ArrayList;
import java.util.Calendar;

public class CDAccounts extends SavingsAccount {

private Calendar maturityDate;
	
	
	//No-Arg Constructor
	public CDAccounts() {
		super();
		maturityDate = Calendar.getInstance();
	}
	
	//Arg Constructor
	public CDAccounts(Depositor depositor,int acct,String typeofacct,boolean acctstatus, double bal,ArrayList<Recepit> rece) {
		super(depositor,acct,typeofacct,acctstatus,bal,rece);
		maturityDate = Calendar.getInstance();
		maturityDate.clear();
		String[] tokens = typeofacct.split("/");
        maturityDate.set(Integer.parseInt(tokens[3]),Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
	}
	
	
	
	//Special method for makeDeposit for the CDAccounts subclass
	public Recepit makeDeposit(Ticket tick) {
		
		//Variable and Object declarations
        Recepit recepit = new Recepit();
        boolean flag;
        boolean status = getAcctStatus();
        String reason = new String();
        double pre=0;
        double post=0;
        double amount = tick.getamountofTransaction();
        Calendar date = tick.getdateofTransaction();
		
		//Error checking and status check
        try
        {
        if(!status || amount < 0){
            flag = false;
            if(!status){
                reason = "This account is closed. To make a deposit, please re-open the account.";
            }
            else if(amount < 0){
                throw new InvalidAmountException(0);
            }
            pre = getBalanceofAcct();
            post = getBalanceofAcct();
            recepit = new Recepit(tick,flag,reason,pre,post,date);

        }
        else if (status){
        	//Error check for maturity date for CD accounts
            if(tick.getTermofCD() > 0 && maturityDate.after(date)){
            	throw new CDMaturityDateException();
                
            }

            //Processes deposit for CD accounts if maturity date is met
            else if (tick.getTermofCD() > 0 && maturityDate.before(date)){
                flag = true;
                reason = "Sucessful Transaction";
                pre = getBalanceofAcct();
                post = pre + amount;
                Bank.addCD(amount);
                setBalance(post);
                Calendar newDate = maturityDate;
                newDate.add(Calendar.MONTH, tick.getTermofCD());
                recepit = new Recepit(tick,flag,reason,pre,post,newDate);
            }
        	
        }
        }
        catch(InvalidAmountException | CDMaturityDateException e) {
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
	
	//Special method for makeDeposit for the CDAccounts subclass
		public Recepit makeWithdrawal(Ticket tick) {
			
			//Variable and Object declarations
	        Recepit recepit = new Recepit();
	        boolean flag;
	        boolean status = getAcctStatus();
	        String reason = new String();
	        double pre=0;
	        double post=0;
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

	            //Error check for Maturity Date of CD accounts
	            if(tick.getTermofCD() > 0 && maturityDate.after(date)){
	            	throw new CDMaturityDateException();
	            }

	            //Processes withdrawal for CD accounts if maturity date is met
	            else if (tick.getTermofCD() > 0 && maturityDate.before(date)){
	                flag = true;
	                reason = "Successful Transaction";
	                pre = getBalanceofAcct();
	                post = pre - amount;
	                setBalance(post);
	                Bank.subCD(amount);
	                Calendar newDate = maturityDate;
	                newDate.add(Calendar.MONTH, tick.getTermofCD());
	                recepit = new Recepit(tick,flag,reason,pre,post,newDate);
	            }
		
	        }	
	      }
	      catch(InsufficentFundsException | InvalidAmountException | CDMaturityDateException e ) {
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
}
