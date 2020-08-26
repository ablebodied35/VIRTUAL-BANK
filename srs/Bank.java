import java.util.ArrayList;
import java.util.Calendar;

public class Bank {

	private ArrayList<Accounts> accounts = new ArrayList<>();
    private static double totalSavings;
    private static double totalChecking;
    private static double totalCD;
    private static double totalAll;
    


   public Bank(){
       accounts = new ArrayList<>();
   }   
   
   public Bank(ArrayList<Accounts> acc){
       accounts = acc;
   }
   
   
   
   /* Input:
    * tick - TransactionTicket object
    * acct - account number entered by user in main
    * dep - Dpeositor object containing name and SSN
    * accttype - type of account to be created
    * Process:
    * Error checks for improper forms for SSN and account or if account exists
    * if passed all error checks, creats accounts obejct, adds it to arraylist
    * adds the TransactionReceput to new accounts TransactionRecepit arraylist
    * Output:
    */
    public Recepit openNewAcct(Ticket tick, Depositor dep, String accttype, int acct){

        //Object and Variable declaration
        Recepit recepit = new Recepit();
        ArrayList<Recepit> rece = new ArrayList<>();
        Accounts newacct = new Accounts();
        int index = findAcct(acct);
        String reason = new String();
        boolean flag;
        double pre = 0;
        double post = 0;
        boolean acctstatus = true;
        Calendar date = tick.getdateofTransaction();

        //Error Checking
        if(index != -1 || dep.getSSN().length() != 9 || acct < 99999 || acct > 999999){
            flag = false;
            if (index !=-1){
                reason = "Error: Account " +acct+ " already exists";
            }
            else if(dep.getSSN().length()!=9){
                reason = "Error: SSN "+ dep.getSSN() + " is invalid";
            }
            else if (acct<99999 || acct > 999999){
                reason = "Error: Account number "+ acct + " is invalid";
            }
            recepit = new Recepit(tick,flag,reason,pre,pre,date);
        }
        //Account creation for CD accounts
        else if (accttype.contains("CD")) {
            flag = true;
            reason = "CD account creation. Deposited $" + tick.getamountofTransaction();
            pre = tick.getamountofTransaction();
            post = tick.getamountofTransaction();
            newacct = new CDAccounts(dep,acct,accttype,acctstatus,post,rece);//Creating new CDAccount
            accounts.add(accounts.size(),newacct);
            recepit = new Recepit(tick,flag,reason,pre,pre,date);
            Bank.addCD(tick.getamountofTransaction());
            newacct.addTransaction(recepit);
        }
        //Account creation for checking or savings accounts
        else {
            flag = true;
            reason = "Account " + acct + " created";
            
            if(accttype.equals("Checking")){//Creates new Checking Account
            newacct = new CheckingAccount(dep,acct,accttype,acctstatus,post,rece);
            }
            else if (accttype.equals("Savings")) {//Creates new Savings
            newacct = new SavingsAccount(dep,acct,accttype,acctstatus,post,rece);
            }
            accounts.add(accounts.size(),newacct);
            recepit = new Recepit(tick,flag,reason,pre,pre,date);
            newacct.addTransaction(recepit);
        }
         Recepit copy = new Recepit(recepit);
         return copy;
    }
   
   
   
    /* Method deleteAcct:
     * Input:
     * tick - TransactionTicket object
     * acct - account number entered by user in main
     * Process:
     * Error checks for non-zero balance or if account exists
     * if passed all error checks, deletes account
     * Output:
     */
     public Recepit deleteAcct(Ticket tick, int acct){

         //Object and variable declaration
         Recepit recepit = new Recepit();
         int index = findAcct(acct);
         String reason = new String();
         boolean flag;
         
         double pre = 0;
         double post = 0;
         Calendar date = tick.getdateofTransaction();

         //Error check for non-zero balance accounts and if account exists
         if(index == -1 || accounts.get(index).getBalanceofAcct() != 0){
             flag = false;
             if(index == -1){
                 reason = "Error: Account " + acct + " not found.";
             }
             else if (accounts.get(index).getBalanceofAcct() != 0){
                 reason = "Error: Account balance is non-zero. Withdraw balance to $0 to delete the account.";
                  pre = accounts.get(index).getBalanceofAcct();
                  post = accounts.get(index).getBalanceofAcct();
             }
             recepit = new Recepit(tick,flag,reason,pre,pre,date);
         }

         //Deletes account
         else {
             flag = true;
             accounts.remove(index);
             reason = "Account "+acct+" has been removed";
             pre = accounts.get(index).getBalanceofAcct();
             post = accounts.get(index).getBalanceofAcct();
             recepit = new Recepit(tick,flag,reason,pre,pre,date);
         }
         Recepit copy = new Recepit(recepit);
         return copy;

     }
   
   
     /*Method - AccountInfo
      *Input
      *tick - TransactionTicket object
      *SSN - SSN string
      *Process:
      *Performs error checks for SSN and account number form then looks through
      *accounts arraylist for accounts with SSN sent to method, extracts it
      *into a new account object which is then put into a new ArrayList
      *which is in turn fed into a TransactionRecepi object.
      *Then send to main to output
      *Output:
      */
     public Recepit AccountInfo(Ticket tick, String SSN){

        //Variable and object delcaration
        Recepit recepit;
        double postbalance=0;
        double prebalance = 0;
        Calendar date = Calendar.getInstance();
        String reason =new String();
        boolean flag=true;
        boolean foundSSN = false;//if we dont find the SSN then this will never be turned on and we can manipulate our flag
      
        //Objects to be filled up with account info if the entered SSN are found
        Accounts foundacc = new Accounts();
        ArrayList<Accounts>  acc = new ArrayList<>();


        //For loops runs through the accounts array in Bank and checks to see
        //if the entered SSN sent by user and the existing SSN in each accounts match
        //If they do then it fills up the new account array acc with apprpriate fields.
        //This them is sent back to main in a TransactionRecepit through a constructor
        //specifically made to hold acc and arraycount
        for(int i =0; i<accounts.size();i++){
            if(SSN.equals(accounts.get(i).getdep().getSSN())){
                flag = true;
                foundSSN = true;
                foundacc = new Accounts(accounts.get(i));
                acc.add(foundacc);
            }
        }
           //if foundSSN was never made true in the loop this will manipulate our flag
           //and help us print appropriate messages
           if (!foundSSN) {
                flag = false;
                reason = "No accounts found for SSN: " + SSN;
                prebalance = 0;
                postbalance = 0;
                }
           else{
               reason = "Successful Transaction";
               prebalance = foundacc.getBalanceofAcct();
               postbalance = foundacc.getBalanceofAcct();
               
           }
        recepit = new Recepit (tick,acc,flag,reason,prebalance,postbalance,date);
        Recepit copy = new Recepit(recepit,"Account Info");
        return copy;
    }
     
     
     
     /*Method - AccountTransactioninfo
      *Input
      *tick - TransactionTicket object
      *SSN - SSN string
      *Process:
      *Performs error checks for SSN and account number form then looks through
      *accounts arraylist for accounts with SSN sent to method, extracts it
      *into a new account object alongside its TransactionRecepit arraylist which is
      *then put into a new ArrayList to which is in turn fed into a TransactionRecepit
      *object. Then send to main to output
      *Output:
      */
      public Recepit AccountTransactionInfo(Ticket tick, String SSN){

      
        //Variable and object delcaration
        Recepit recepit;
        double postbalance=0;
        double prebalance = 0;
        Calendar date = Calendar.getInstance();
        String reason =new String();
        boolean flag=true;
        boolean foundSSN = false;//if we dont find the SSN then this will never be turned on and we can manipulate our flag
      
        //Objects to be filled up with account info if the entered SSN are found
        Accounts foundacc = new Accounts();
        ArrayList<Accounts>  acc = new ArrayList<>();
        
        

        //For loops runs through the accounts array in Bank and checks to see
        //if the entered SSN sent by user and the existing SSN in each accounts match
        //If they do then it fills up the new account array acc with apprpriate fields.
        //This them is sent back to main in a TransactionRecepit through a constructor
        //specifically made to hold acc and arraycount
        for(int i =0; i<accounts.size();i++){
            if(SSN.equals(accounts.get(i).getdep().getSSN())){
                flag = true;
                foundSSN = true;
                foundacc = new Accounts(accounts.get(i));//copy constructor being used
                
                acc.add(foundacc);
            }
        }
           //if foundSSN was never made true in the loop this will manipulate our flag
           //and help us print appropriate messages
           if (!foundSSN) {
                flag = false;
                reason = "No accounts found for SSN: " + SSN;
                prebalance = 0;
                postbalance = 0;
                }
        recepit = new Recepit (tick,acc,flag,reason,prebalance,prebalance,date);
        Recepit copy = new Recepit(recepit,"Account Info");
        return copy;
    }
   
   
   
   
   
   
   
   
   
   
   
   
   public int findAcct(int acct){
       for (int i =0; i< accounts.size();i++){
          if(accounts.get(i).getAcctNum() == acct)
              return i;
          }
       return -1;
    }
    
    public Accounts getAcct(int index){
        Accounts copy = new Accounts(accounts.get(index));
        return copy;
        
    }
    
    public int getnumAccts(){
        
        return accounts.size();
        
    }
    
    public static void addSavings(double value){
        totalSavings+=value;
        totalAll+=value;
    }
    
    public static void addChecking(double value){
        totalChecking+=value;
        totalAll+=value;
    }
    
    public static void addCD(double value){
        totalCD+=value;
        totalAll+=value;
    }
    
    public static void subSavings(double value){
        totalSavings-=value;
        totalAll-=value;
    }
    
    public static void subChecking(double value){
        totalChecking-=value;
        totalAll-=value;
    }

    public static void subCD(double value){
        totalCD-=value;
        totalAll-=value;
    }

    
    public static double getSavings(){
        return totalSavings;
    }
    
    public static double getChecking(){
        return totalChecking;
    }
    
    public static double getCD(){
        return totalCD;
    }
    
    public static double getAll(){
        return totalAll;
    }
    
    public String toString(){
        for (int i =0; i> accounts.size();i++){
            System.out.println(accounts.get(i));
        }
        String str = String.format("Total Savings: $%-6.2f \r\nTotal Checking: $%-6.2f \r\nTotal CD: $%-6.2f \r\nTotal Amount: $%-8.2f"
                                    ,totalSavings
                                    ,totalChecking
                                    ,totalCD
                                    ,totalAll);
       
        return str;
        
        
    } 
    
    //equals
    public boolean equals(Bank other){
        if (totalSavings==other.totalSavings && totalChecking==other.totalChecking && totalCD==other.totalCD && totalAll==other.totalAll)
            return true;
        return false;
    }
}
