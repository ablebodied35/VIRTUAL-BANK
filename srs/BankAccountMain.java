import java.io.*;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
public class HW8 {

	public static void main(String[] args){
		
		//Variable declaration. Files, Scanners, PrintWriters and objects of classes all declared
		File input;
		File output;
		Scanner read,kybd;
		PrintWriter out;
		
       try {
    	input = new File("account.txt");
    	RandomAccessFile in;
        output = new File("output.txt");
        read = new Scanner(input);
       	kybd = new Scanner(System.in);
       	out = new PrintWriter(output);
       	ArrayList<Accounts> acc = new ArrayList<>();
        Bank bank = new Bank(acc);
        boolean notdone = true;
        char selection;
         
        readData(bank,acc,read);
        printAccts(bank,acc,out);
        
       
        
     
        do
        	{
            menu(); //Prints Menu
             
            selection = kybd.next().charAt(0);//Allows Transaction Selection
            try {
            switch(selection){ //Allows Menu selection
            	

            	//Selection to quit
                case 'q':
                case 'Q':
                    notdone = false;
                    printAccts(bank,acc,out);
                    break;

                //Selection for balance
                case 'b':
                case 'B':
                    Balance(bank,acc,kybd,out);
                    break;

                //Selection for Deposit
                case 'd':
                case 'D':
                    Deposit(bank,acc,kybd,out);
                    break;

                //Selection to withdraw balances
                case 'w':
                case 'W':
                    Withdrawal(bank,acc,kybd,out);
                    break;

                //Selection to create new accounts
                case 'n':
                case 'N':
                    openNewAcct(bank,acc,kybd,out);//Reassigns value to numaccts because a new account is created
                    break;

                //Selection to delete accounts
                case 'x':
                case 'X':
                    deleteAcct(bank,acc,kybd,out);//Reassigns value to numaccts because an account was deleted
                    break;

                //Selection for account info
                case 'I':
                case 'i':
                    acctInfo(bank,acc,kybd,out);
                    break;

                case 'C':
                case 'c':
                    clearCheck(bank,acc,kybd,out);
                    break;

                case 'S':
                case 's':
                    closeAcct(bank,acc,kybd,out);
                    break;

                case 'R':
                case 'r':
                    reopenAcct(bank,acc,kybd,out);
                    break;

                case 'H':
                case 'h':
                    acctTransactionInfo(bank,acc,kybd,out);
                    break;
                //Invalid selections
            	default :
            		throw new InvalidMenuSelectionException(selection);
            } 
            	}
            catch(InvalidMenuSelectionException e)
          	{
         	   out.println(e.getMessage());
         	   out.println();
            }
            }while(notdone);
        	
       
       
		out.close();
		read.close();
       	
       }
      
       catch(Exception e) {
    	   System.out.println(e.getMessage());
       }
       
      
       	
      
		
	}

	public static void readData(Bank bank, ArrayList<Accounts> acc, Scanner read) {
		
	 	String line;
	 	Accounts acco = new Accounts();
        boolean status = true;
        
        while (read.hasNext()){
            line = read.nextLine();
            String tokens[] = line.split(" ");
            Name myname = new Name(tokens[0],tokens[1]);
            Depositor dep = new Depositor(myname, tokens[2]);
            int acctnum = Integer.parseInt(tokens[3]);
            double balance = Double.parseDouble(tokens[5]);
            if(tokens[6].equals("Open")) {
            	status = true;
            }
            else {
            	status = false;
            }
            ArrayList<Recepit> rece = new ArrayList<>();
            
            
            //Making new Accounts based on what type of account they are 
            if(tokens[4].equals("Checking")) {
            	acco = new CheckingAccount(dep,acctnum,tokens[4],status,balance,rece);
            }
            else if(tokens[4].equals("Savings")) {
            	acco = new SavingsAccount(dep,acctnum,tokens[4],status,balance,rece);
            }
            else if(tokens[4].contains("CD")){
            	acco = new CDAccounts(dep,acctnum,tokens[4],status,balance,rece);
            }
            
            if (tokens[6].equals("Open")){
                status = true;
            }
            else if (tokens[6].equals("Close")){
                status = false;
            }
            if (tokens[4].equals("Savings")){
                Bank.addSavings(balance);
            }
            else if(tokens[4].equals("Checking")){
                Bank.addChecking(balance);
            }
            else if(tokens[4].contains("CD")){
                Bank.addCD(balance);
            }
            
            acc.add(acco);
            }
}

public static void printAccts(Bank bank, ArrayList<Accounts> acc, PrintWriter out){
    
    	out.println("DATEBASE OF ACCOUNTS");
    	out.printf("%-9s %-8s %-9s %-6s %-13s %-6s %-5s","First","Last","SSN","Number","Type","Status","Balance");
   		out.println();
   		out.println("--------------------------------------------------------------------------");
    
   		for(int i = 0; i<acc.size();i++){
   			out.println(acc.get(i));
   		}
   		
   		out.println(bank);
   		out.println();
     
}

 /* Method balance:
    * Input:
    * bank - Bank object
    * acc - arraylist of accounts
    * out - reference to output file
    * kybd - used for input
    * Process:
    * checks to see if account exists
    * if it does then send a transactionticket object to getBalance in Account class
    * Output:
    * If the account exists, the balance is printed
    * Otherwise, an error message is printed
    */
     public static void Balance(Bank bank, ArrayList<Accounts> acc, Scanner kybd,PrintWriter out){
        
    	 
         //Variables and prompt
          System.out.println("Please enter your account number");
          int acct = kybd.nextInt();
          int index = bank.findAcct(acct);//finds index
          Calendar date =Calendar.getInstance();//Todays date
          date.add(Calendar.MONTH, 1);//Increment month by 1 to get proper month

          //Objects to be tossed in and out of different classes
          Recepit recepit;
          Ticket ticket;
          Accounts account;

          //Areas to be filled out for our TransactionTicket object
          String type;
          double amount;
          
         
          //If account not found
        if (index == -1){
              out.println("Transaction Requested: Balance Inquiry");
              out.println("Date: " + date.get(Calendar.MONTH)+"/"+date.get(Calendar.DATE)+"/"+date.get(Calendar.YEAR) );
              out.println("Error: Account "+ acct + " not found");
          }
          //If account found
         else  {

              //Finally fills out TransactionTicket object
              
            	  type = "Transaction Requested: Balance Inquiry";
              
              amount =-12;
              ticket = new Ticket(date,type,amount,0);
              account = bank.getAcct(index);//Gets the account we need to make the transaction on
              recepit = account.getBalance(ticket);//Access getBalance in account class to fill TransactionRecepit object out

              //flag = recepit.getflag();//Gets success flag
              
              out.println("Account Number: "+acct);
              out.println(recepit);
              out.println();
              }
    	 }
             
    
     
    
     /* Method Deposit:
      * Input:
      * bank - Bank class object holding our accounts and number of accounts
      * acc - arraylist of Accounts.
      * kybd - Scanner for input from console
      * out - PrintWriter for writing to output file
      * Process:
      * checks to see if account exists
      * if it does calls makeDeposit in Account class
      * Output:
      * For a valid deposit, the transaction is printed
      * Otherwise, prints error message
      */
        public static void Deposit(Bank bank, ArrayList<Accounts> acc, Scanner kybd, PrintWriter out){
        	
            //Variables and prompt
            System.out.println("Please enter your account number");
            int acct = kybd.nextInt();
            int index = bank.findAcct(acct);//finds index
            Calendar date =Calendar.getInstance();//Todays date
            date.add(Calendar.MONTH, 1);//Increment month by 1 to get proper month

            //Objects to be tossed in and out of different classes
            Recepit recepit;
            Ticket ticket;
            Accounts account;

            //Areas to be filled out for our TransactionTicket object
            String type;
            double amount = 0;
            boolean flag;
            int terms;

            if(index == -1){
                out.println("Transaction Requested: Make Deposit");
                out.println("Date: " + date.get(Calendar.MONTH)+"/"+date.get(Calendar.DATE)+"/"+date.get(Calendar.YEAR));
                out.println("Error: Account " + acct + " not found");
            }
            else{
                type = "Transaction Requested: Make Deposit";
                System.out.println("Please enter the amount you wish to deposit");
               
            	   amount = kybd.nextDouble();
            	   if (acc.get(index).getaccttype().contains("CD")){
                       System.out.println("Please enter the terms of CD-6/12/18/24");
                       terms = kybd.nextInt();
                   }
                   else {
                        terms = 0;
                   }
                   account = bank.getAcct(index);
                   ticket = new Ticket(date,type,amount,terms);
                   recepit = acc.get(index).makeDeposit(ticket);
                   out.println("Account: " + acct);
                   out.println(recepit);
                   out.println();
               }
        	}
     
       
        
        /* Method withdrawl:
         * Input:
         * bank - Bank class object holding our accounts and number of accounts
         * acc - arraylist of Accounts. Needed to access our makeWithdrawal method in Account class
         * out - PrintWriter for writing to output file
         * kybd - Scanner for input from console
         * Process:
         * checks to see if account exists
         * if it does calls makeWithdrawal in Account class
         * Output:
         * For a valid withdrawl, the transaction is printed
         * Otherwise, prints error message
         */
            public static void Withdrawal(Bank bank, ArrayList<Accounts> acc, Scanner kybd, PrintWriter out){
            	
            
               //Variables and prompt
               System.out.println("Please enter your account number");
               int acct = kybd.nextInt();
               int index = bank.findAcct(acct);//finds index
               Calendar date =Calendar.getInstance();//Todays date
               date.add(Calendar.MONTH, 1);//Increment month by 1 to get proper month

               //Objects to be tossed in and out of different classes
               Recepit recepit;
               Ticket ticket;
               Accounts account;

               //Areas to be filled out for our TransactionTicket object
               String type;
               double amount=0;
               boolean flag;
               int terms;

               if(index == -1){
                   out.println("Transaction Requested: Make Withdrawal");
                   out.println("Date: " + date.get(Calendar.MONTH)+"/"+date.get(Calendar.DATE)+"/"+date.get(Calendar.YEAR));
                   out.println("Error: Account " + acct + " not found");
               }
               else{
                
            	   type = "Transaction Requested: Make Withdrawal";
                   System.out.println("Please enter the amount you wish to withdraw");
                   amount = kybd.nextDouble();

                   if (acc.get(index).getaccttype().contains("CD")){
                       System.out.println("Please enter the terms of CD-6/12/18/24");
                       terms = kybd.nextInt();
                   }
                   else {
                        terms = 0;
                   }
                   account = bank.getAcct(index);
                   ticket = new Ticket(date,type,amount,terms);
                   recepit = acc.get(index).makeWithdrawal(ticket);
                   out.println("Account: " + acct);
                   out.println(recepit);
                   out.println();
               }
            }
            	
            
            
            /* Method clearCheck:
             * Input:
             * bank - Bank class object holding our accounts and number of accounts
             * acc - array of Accounts. Needed to access our makeWithdrawal method in Account class
             * out - PrintWriter for writing to output file
             * kybd - Scanner for input from console
             * Process:
             * fills out a check object, checks if account exists, fills check object
             * sends it to clearCheck in accounts class for further processing
             * Output:
             * prints TransactionRecepit object based on successflag value in clearCheck
             * method in account class or error messages
             */
               public static void clearCheck(Bank bank, ArrayList<Accounts> acc, Scanner kybd, PrintWriter out){
            	   
            	
                 //Prompt and variable declaration
                 Recepit recepit;
                 Accounts account;
                 Check ch;
                 Calendar todaysdate = Calendar.getInstance();
                 todaysdate.add(Calendar.MONTH,1);
                 boolean flag;
                 //Prompts
                 System.out.println("Please enter the account number");
                 
                 int acct = kybd.nextInt();
                 int index = bank.findAcct(acct);//Grabs index of account
                 
                 System.out.println("Please enter the amount");
                 
                 
               
                 double amount = kybd.nextDouble();
                 System.out.println("Please enter the date in MM/DD/YYYY");
                 String date = kybd.next();
                 String[] tokens = date.split("/");
                 Calendar dateofCheck = Calendar.getInstance();
                 dateofCheck.clear();
                 dateofCheck.set(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));

                 //If account not found, error message is printed
                 if ( index == -1){
                 out.println("Transaction Requested: Clear Check");
                 Calendar d = Calendar.getInstance();
                 d.add(Calendar.MONTH, 1);
                 out.println("Date: "+d.get(Calendar.MONTH)+"/"+d.get(Calendar.DATE)+"/"+d.get(Calendar.YEAR));
                 out.println("Error: Account " + acct + " not found");
                 }

                 //Fills check object to then be sent to clearCheck method in account class
                 //Then based on successflag value appropriate messaging is printed out of
                 //TransactionRecepit object
                 else {
                     ch = new Check(acct,amount,dateofCheck);
                     account = acc.get(index);
                     recepit = account.clearCheck(ch);
                     
                     out.println("Transaction Requested: Clear Check");
                     out.println(ch);
                     out.println("Date: " + todaysdate.get(Calendar.MONTH)+"/"+todaysdate.get(Calendar.DATE)+"/"+todaysdate.get(Calendar.YEAR));
                     out.println(recepit.toString(ch));
                     
                 }
                 out.println();
               }
               
               
                 
                 
                       
               
               
               public static void menu(){
                   System.out.println();
                   System.out.println("Select one of the following transactions:");
                   System.out.println("\t****************************");
                   System.out.println("\t List of Choices ");
                   System.out.println("\t****************************");
                   System.out.println("\t W -- Withdrawal");
                   System.out.println("\t D -- Deposit");
                   System.out.println("\t N -- New Account");
                   System.out.println("\t B -- Balance Inquiry");
                   System.out.println("\t X -- Delete Account");
                   System.out.println("\t I -- Account Info");
                   System.out.println("\t C -- Clear Check");
                   System.out.println("\t I -- Account Info");
                   System.out.println("\t H -- Account Info with Transaction History");
                   System.out.println("\t S -- Close Account");
                   System.out.println("\t R -- Re-Open Account");
                   System.out.println("\t Q -- Quit");
                   System.out.println();
                   System.out.print("\tEnter your selection: ");
               }
               
               
               /* Method openNewAcct:
             * Input:
             * bank - Bank reference
             * acc - arraylist of accounts objects
             * out - PrintWriter for writing to output file
             * kybd - Scanner for console input
             * Process:
             * Requests new account number and data fiels for a new account, then fills out TransactionTicket object
             * Output:
             * Message letting the user know a new account was created
             * Otherwise, prints error message
             */
               public static void openNewAcct(Bank bank, ArrayList<Accounts> acc, Scanner kybd, PrintWriter out){
            	
                   //Variable Declaration
                   Ticket ticket;
                   Recepit recepit;
                   Name name;
                   Depositor dep;
                   double CDdeposit=0;
                   double amount;
                   boolean flag;
                   Calendar date = Calendar.getInstance();
                   date.add(Calendar.MONTH,1);

                   
                   //Data Required to create new account
                   System.out.println("Enter your first name");
                   String first = kybd.next();
                   System.out.println("Enter your last name");
                   String last = kybd.next();
                   System.out.println("Enter your SSN");
                   String SSN = kybd.next();
                   System.out.println("Enter your account number");
                   int acct = kybd.nextInt();
                   System.out.println("Enter your account type. If you are creating a CD account please enter in this format CD/MM/DD/YEAR");
                   String accttype = kybd.next();

                   //If we create a CD account, it asks for a deposit amount to grow until Maturity Date.
                   if(accttype.contains("CD")){
                      System.out.println("Please enter a deposit amount for your CD account");
                      CDdeposit = kybd.nextDouble();
                     }
                   name = new Name(first,last);
                   dep = new Depositor(name,SSN);

                   //Sets amount equal to CDdeposit value if account type is CD otherwise sets it to 0
                   if(accttype.contains("CD")){
                      amount = CDdeposit;
                     }
                   else{
                      amount =0.0;
                     }

                   int terms = 0;
                   String type = "Transaction Requested: Open New Account";

                   //Fills out Ticket and Recepit obejects
                   ticket = new Ticket(date,type,amount,terms);
                   recepit = bank.openNewAcct(ticket,dep,accttype,acct);
                   
                   out.println(recepit);
                   out.println();
                   }
                 
                   
               
               
               /* Method deleteAcct:
             * Input:
             * bank - Bank reference
             * acc - arraylist of account references
             * out - PrintWriter for writing to output file
             * kybd - Scanner for console input
             * Process:
             * Requests account number to be delted, then fills out TransactionTicket object
             * Then send to deleteAcct in Bank class
             * Output:
             * Error messages, or success message
             */
               public static void deleteAcct(Bank bank, ArrayList<Accounts> acc, Scanner kybd, PrintWriter out){
            	   
                   //Variables and prompt
                   System.out.println("Please enter your account number");
                   int acct = kybd.nextInt();

                   //Areas to be filled out for our TransactionTicket object
                  
                   String type = "Transaction Requested: Delete Account";
                   double amount= 0;
                   boolean flag;
                   int terms=0;
                   Calendar date = Calendar.getInstance();
                   date.add(Calendar.MONTH, 1);

                   //Objects to be sent to bank class
                  Ticket ticket = new Ticket(date,type,amount,terms);
                  Recepit recepit = bank.deleteAcct(ticket,acct);
                  out.println(recepit);
                  out.println();
                  }
                 
               
               /*Method - acctinfo
              *Input
              *bank - Bank class reference\
              *acc - arraylist of account references
              *out - PrintWriter to write to file
              *kybd - Scanner for console input
              *Process:
              *Asks for Social Security Number
              *fills out TransactionTicket object and send it to acctInfo in Bank class
              *Output:
              *If found, prints info of account related to SSN
              *If no account found prints error message
              */
             public static void acctInfo(Bank bank, ArrayList<Accounts> acc,Scanner kybd, PrintWriter out){
            	 
                 //Prompts and variables
                 System.out.println("Please enter your SSN");
                 String SSN = kybd.next();

                 String type = "Transaction Requested: Account Information";
                 Calendar date = Calendar.getInstance();
                 date.add(Calendar.MONTH, 1);
                 
                 double amount = 0;
                 String acctstat;
                 Ticket ticket = new Ticket(date,type,amount,0);
                 Recepit recepit = bank.AccountInfo(ticket, SSN);
                  boolean flag = recepit.getflag();
                 
                 if (flag){
                 out.println(type);
                 out.println("SSN requested: " + SSN);
                 for(int i = 0; i< recepit.gethistoryofAccts().size();i++){
                         out.println(recepit.gethistoryofAccts().get(i));
                     }
                 }
                 else {
                     out.println(recepit);
                 }
            
                 out.println();
                 }
               
              
              /*Method - acctTransactioninfo
              *Input
              *bank - Bank class reference\
              *acc - arraylist of account references
              *out - PrintWriter to write to file
              *kybd - Scanner for console input
              *Process:
              *Asks for Social Security Number
              *fills out TransactionTicket object and send it to acctInfo in Bank class
              *Output:
              *If found, prints info of account related to SSN along with all transactionRecepits
              *If no account found prints error message
              */
             public static void acctTransactionInfo(Bank bank, ArrayList<Accounts> acc,Scanner kybd, PrintWriter out){
               
            	 
            	 //Prompts and variables
                 System.out.println("Please enter your SSN");
                 String SSN = kybd.next();
                 Accounts acct = new Accounts();
                 int acctnum;
                 String type = "Transaction Requested: Account Information and Transaction History";
                 Calendar date = Calendar.getInstance();
                 date.add(Calendar.MONTH, 1);
                
                 double amount = 0;
                 String acctstat;
                 int terms = 0;
                 Ticket ticket = new Ticket(date,type,amount,terms);
                 Recepit recepit = bank.AccountTransactionInfo(ticket, SSN);
                 ArrayList<Recepit> rece = new ArrayList<>();
                 boolean flag = recepit.getflag();
                 
                 if (!flag){
                     out.println(recepit);
                 }
                 else {
                     out.println(type);
                     out.println("SSN requested: " + SSN);
                     
                     for(int i = 0; i< recepit.gethistoryofAccts().size();i++){
                          out.println(recepit.gethistoryofAccts().get(i));
                         
                          //Retrieving the Transaction Receipts array
                          rece = recepit.gethistoryofAccts().get(i).getTransactionHistory(ticket);
                          
                          out.println("Transaction History");
                          out.println("--------------------");
                          
                          //Printing the Transaction History
                          for(int j =0; j< rece.size();j++){
                             out.println(rece.get(j));
                             out.println("--------------------");
                             }
                     }
                 }
                
                
                 out.println();
                }
              
               

             
             /* Method closeAcct:
              * Input:
              * bank - Bank reference
              * acc - arraylist of accounts objects
              * out - PrintWriter for writing to output file
              * kybd - Scanner for console input
              * Process:
              * Requests account number, searches for index. Then fills data fields for a new accounts object,
              * then fills out TransactionTicket object and send to to reopenAcct in Accounts class
              * Output:
              * Message letting the user know the account they requested was closed
              * Otherwise, prints error message
              */
                public static void closeAcct(Bank bank, ArrayList<Accounts> acc, Scanner kybd, PrintWriter out){

                	
                    //Variables and prompt
                    System.out.println("Please enter your account number");
                    int acct = kybd.nextInt();
                    int index = bank.findAcct(acct);//finds index
                    Calendar date =Calendar.getInstance();//Todays date
                    date.add(Calendar.MONTH, 1);//Increment month by 1 to get proper month

                    //Objects to be tossed in and out of different classes
                    Recepit recepit;
                    Ticket ticket;
                    Accounts account;

                    //Areas to be filled out for our TransactionTicket object
                    String type = "Transaction Requested: Close Account";
                   
                    double amount=0;
                    boolean flag;
                    int terms=0;

                    if (index == -1){
                        out.println(type);
                        out.println("Date: " + date.get(Calendar.MONTH)+"/"+date.get(Calendar.DATE)+"/"+date.get(Calendar.YEAR));
                        out.println("Error: Account number "+acct+" not found");
                    }
                    else{
                        account = bank.getAcct(index);
                        ticket = new Ticket(date,type,amount,terms);
                        recepit = account.closeAcct(ticket);
                        
                        out.println("Account: " + acct);
                        out.println(recepit);
                        
                    }
                    out.println();
                   }
                  
                
                 /* Method reopenAcct:
              * Input:
              * bank - Bank reference
              * acc - arraylist of accounts objects
              * out - PrintWriter for writing to output file
              * kybd - Scanner for console input
              * Process:
              * Requests account number, searches for index. Then fills data fields for a new accounts object,
              * then fills out TransactionTicket object and send to to reopenAcct in Accounts class
              * Output:
              * Message letting the user know the account they requested was re-opened
              * Otherwise, prints error message
              */
                public static void reopenAcct(Bank bank, ArrayList<Accounts> acc, Scanner kybd,PrintWriter out){
                	
                     //Variables and prompt
                    System.out.println("Please enter your account number");
                    int acct = kybd.nextInt();
                    int index = bank.findAcct(acct);//finds index
                    Calendar date =Calendar.getInstance();//Todays date
                    date.add(Calendar.MONTH, 1);//Increment month by 1 to get proper month

                    //Objects to be tossed in and out of different classes
                    Recepit recepit;
                    Ticket ticket;
                    Accounts account;

                    //Areas to be filled out for our TransactionTicket object
                   
                    String type = "Transaction Requested: Re-Open Account";
                    double amount=0;
                    boolean flag;
                    int terms=0;

                    if (index == -1){
                        out.println(type);
                        out.println("Date: " + date.get(Calendar.MONTH)+"/"+date.get(Calendar.DATE)+"/"+date.get(Calendar.YEAR));
                        out.println("Error: Account number "+acct+" not found");
                    }
                    else{
                        account = bank.getAcct(index);
                        ticket = new Ticket(date,type,amount,terms);
                        recepit = account.reopenAcct(ticket);
                        
                        out.println("Account: " + acct);
                        out.println(recepit);
                    }
                    out.println();
                   }
                 }
