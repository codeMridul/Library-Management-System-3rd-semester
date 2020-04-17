/*
@author : Mridul Mishra
*/
import java.sql.*;
//import java.io.*;
import java.util.*;

public class LMS 
{
    static Connection con=null;
    Scanner in= new Scanner(System.in);
    public void login()
    {
        System.out.print("Enter your UserId: ");
        int uId= in.nextInt();
        System.out.println(("\nEnter the Password: "));
        String psd= in.nextLine();
        String fetchData="SELECT password from studentData where userId= '"+Integer.toString(uId)+"'"; 
        try
        {
            PreparedStatement pstmt = con.prepareStatement(fetchData);
            ResultSet rSet=pstmt.executeQuery();
            if (rSet.getString("password")==psd)
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                System.out.println("Login Successful\n\n");
            }
            else
            {
                System.out.println("Wrong Password, Enter Again.  :)");
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                login();
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("jhg");
        }

    }
    public void signup()
    {

    }
    
}
class Main extends LMS
{
    public static void connect()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            con=DriverManager.getConnection("jdbc:sqlite:LMS.db");
            System.out.println("\n\nConnected to the Liabrary Database.....\n\n");
        }
        catch(Exception e)
        {
            System.out.println(e);
            System.out.println("\n\n\n\nConnection to the DATABASE could not be established...\n\n\n\n");
            System.exit(0);
        }
    }
    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
    public static void main(String[] args) 
    {
        System.out.print("Connecting");
        for(int i=1;i<=4;i++)
        {
            wait(1000);
            System.out.print(".");
        }
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        connect();

        Main ob=new Main();
        Scanner in=new Scanner(System.in);

        System.out.println("Student Portal :)\n ");
        System.out.println("Press 1\n     To login   \nPress 2\n     To Create New Account");
        int ch= in.nextInt();

        String createTable="CREATE TABLE IF NOT EXISTS studentData(\n"
        + " id integer PRIMARY KEY,\n"
        + " userId integer NOT NULL,\n"
        + " password text NOT NULL\n"
        + ");";
        /*Statement stmt = con.createStatement();  
        stmt.execute(createTable);  */
        

        try
        {
            System.out.println("bjbdvfbdkb");
            Statement stmt = con.createStatement();  
            stmt.execute(createTable);
            if(ch==1)
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                ob.login();
            }
            else if(ch==2)
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                ob.signup();
            }
            else
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                System.out.println("You don't wish to login/signup\nGood bye\n\n");
            }  
        }
        catch(Exception e)
        {
            System.out.println("fds");
            System.out.println(e.getMessage());
        }

        


    }
}